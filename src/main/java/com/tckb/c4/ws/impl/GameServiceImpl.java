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
import com.tckb.c4.model.exception.ColumnFilledException;
import com.tckb.c4.model.exception.MaxPlayerRegisteredException;
import com.tckb.c4.model.intf.Player;
import com.tckb.c4.model.exception.PlayerNotRegisteredException;
import com.tckb.c4.ws.BoardConfiguration;
import com.tckb.c4.model.exception.GameNotSetupException;
import com.tckb.c4.ws.repo.BoardGame;
import com.tckb.c4.ws.repo.GameRepository;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of GameService
 * <p>
 * @author tckb
 */
@Service
public class GameServiceImpl {

    @Autowired
    private GameRepository repo;
    protected static final Logger thisLogger = Logger.getLogger(GameServiceImpl.class.getName());
    private BoardConfiguration boardConfig;

    public String[] registerAndStartGame(String playerRef, boolean isMultiplayerGame) throws GameNotSetupException {
        thisLogger.log(Level.FINE, "Registering new player with reference: {0}", playerRef);
        try {

            if (boardConfig != null) {
                String gameSessionId = generateGameSessionId();

                Board playerBoard = (Board) GameFactory.getFactory(FactoryType.BOARD).createInstance(BoardType.C4, boardConfig.getBoardWidth(), boardConfig.getBoardHeight(), boardConfig.getWinningConnections());
                Player humanPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.Human, playerRef);
                playerBoard.registerPlayer(humanPlayer);

                if (!isMultiplayerGame) {
                    Player aiPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.AI);
                    playerBoard.registerPlayer(aiPlayer);
                }

                repo.save(new BoardGame(playerRef, playerBoard, gameSessionId));

                thisLogger.log(Level.FINE, "BoardPlayer saved in db successfully.");
                return new String[]{humanPlayer.getPlayerChip().getColor(), gameSessionId};

            } else {
                throw new GameNotSetupException("Game not configured, use '/setup' to configure the game.");
            }

        } catch (MaxPlayerRegisteredException ex) {
            thisLogger.log(Level.SEVERE, "Registration failed for reference: " + playerRef, ex);
        }
        return null;
    }

    public boolean isWinningPlayer(String playerRef) throws PlayerNotRegisteredException {
        thisLogger.log(Level.FINE, "Checking if player with reference: {0} is a winner", playerRef);

        BoardGame playerBoard = (BoardGame) repo.findOne(playerRef);
        if (playerBoard != null) {
            Player winningPlayer = playerBoard.getBoard().getWinner();
            return winningPlayer.getReference().equals(playerRef);
        }
        throw new PlayerNotRegisteredException();
    }

    public String[] placeBoardPiece(String playerRef, String boardColumn) throws PlayerNotRegisteredException, ColumnFilledException,
            ColumnFilledException {
        BoardGame playerBoard = (BoardGame) repo.findOne(playerRef);
        if (playerBoard != null) {

            if (playerBoard.getBoard().getRegisteredPlayers().size() != playerBoard.getBoard().getMaxPlayers()) {
                return null;
            }
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
                    String chip2 = aiPlayer.placeChipOnBoard();
                    return new String[]{chip1, chip2};
                } else {
                    throw new ColumnFilledException();
                }
            } else {
                thisLogger.log(Level.SEVERE, "Dangling player reference found, player ref:{0} has board, but player is not registered with this board!", playerRef);
                return null;
            }
        }
        throw new PlayerNotRegisteredException();
    }

    /**
     *
     * @return
     */
    public String[] getOverallGameStats() {
        return new String[]{Long.toString(repo.count())};
    }

    public void setupBoard(BoardConfiguration configRequst) {
        if (configRequst != null) {
            this.boardConfig = configRequst;
        }
    }

    /**
     * Generate a random game session Id
     * <p>
     * @return
     */
    protected String generateGameSessionId() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    public String getBoardSession(String playerRef) {
        BoardGame playerBoard = (BoardGame) repo.findOne(playerRef);
        return playerBoard.getBoardRef();
    }

    public boolean isPlayerAlreadyRegistered(String playerRef) {
        return repo.findOne(playerRef) != null;
    }

}
