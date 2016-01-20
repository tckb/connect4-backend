/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.impl;

import com.tckb.c4.model.concrete.AiPlayer;
import com.tckb.c4.model.concrete.HumanPlayer;
import com.tckb.c4.model.factory.BoardFactory.BoardType;
import com.tckb.c4.model.factory.GameFactory;
import com.tckb.c4.model.factory.GameFactory.FactoryType;
import com.tckb.c4.model.factory.PlayerFactory.PlayerType;
import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.Player;
import com.tckb.c4.ws.GameService;
import com.tckb.c4.ws.repo.GameRepository;
import com.tckb.c4.ws.repo.PlayerBoard;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of GameService
 * <p>
 * @author tckb
 */
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository repo;
    protected static final Logger thisLogger = Logger.getLogger(GameServiceImpl.class.getName());

    /**
     *
     * @param playerRef <p>
     * @return
     */
    @Override
    public String registerAndStartGame(String playerRef) {
        thisLogger.log(Level.FINE, "Registering new player with reference: {0}", playerRef);
        try {

            Board playerBoard = (Board) GameFactory.getFactory(FactoryType.BOARD).createInstance(BoardType.C4_7X6);
            Player humanPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.Human, playerRef);
            Player aiPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.AI);

            playerBoard.registerPlayer(humanPlayer);
            playerBoard.registerPlayer(aiPlayer);

            repo.save(new PlayerBoard(playerRef, playerBoard));

            thisLogger.log(Level.FINE, "BoardPlayer saved in db successfully.");
            return humanPlayer.getPlayerChip().getColor();

        } catch (Board.MaxPlayerRegisteredException ex) {
            thisLogger.log(Level.SEVERE, "Registration failed for reference: " + playerRef, ex);
        }
        return null;
    }

    /**
     *
     * @param playerRef <p>
     * @return <p>
     * @throws com.tckb.c4.model.intf.Board.PlayerNotRegisteredException
     */
    @Override
    public boolean isWinningPlayer(String playerRef) throws Board.PlayerNotRegisteredException {
        thisLogger.log(Level.FINE, "Checking if player with reference: {0} is a winner", playerRef);

        PlayerBoard playerBoard = (PlayerBoard) repo.findOne(playerRef);
        if (playerBoard != null) {
            Player winningPlayer = playerBoard.getBoard().getWinner();
            return winningPlayer.getReference().equals(playerRef);
        }
        throw new Board.PlayerNotRegisteredException();
    }

    /**
     *
     * @param playerRef
     * @param boardColumn <p>
     * @return <p>
     * @throws com.tckb.c4.model.intf.Board.PlayerNotRegisteredException
     * @throws com.tckb.c4.model.intf.Board.ColumnFilledException
     */
    @Override
    public String[] placeBoardPiece(String playerRef, String boardColumn) throws Board.PlayerNotRegisteredException, Board.ColumnFilledException {
        PlayerBoard playerBoard = (PlayerBoard) repo.findOne(playerRef);
        if (playerBoard != null) {
            Player humanPlayer = null, aiPlayer = null;

            for (Player boardPlayer : playerBoard.getBoard().getRegisteredPlayers()) {
                if (boardPlayer instanceof HumanPlayer && boardPlayer.getReference().equals(playerRef)) {
                    humanPlayer = boardPlayer;
                }
                if (boardPlayer instanceof AiPlayer) {
                    aiPlayer = boardPlayer;
                }
            }
            if (humanPlayer != null && aiPlayer != null) {
                String chip1 = humanPlayer.placeChipOnBoard(Integer.parseInt(boardColumn));
                if (chip1 != null) {
                    String chip2 = aiPlayer.placeChipOnBoard(-1);
                    return new String[]{chip1, chip2};
                } else {
                    throw new Board.ColumnFilledException();
                }
            } else {
                thisLogger.log(Level.SEVERE, "Dangling player reference found, player ref:{0} has board, but player is not registered with this board!", playerRef);
                return null;
            }
        }
        throw new Board.PlayerNotRegisteredException();
    }

    /**
     *
     * @return
     */
    @Override
    public String[] getOverallGameStats() {
        return new String[]{Long.toString(repo.count())};
    }

}
