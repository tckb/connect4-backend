package com.tckb.c4.ws.impl;

import com.tckb.c4.model.concrete.AiPlayer;
import com.tckb.c4.model.concrete.HumanPlayer;
import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameFinished;
import com.tckb.c4.model.exception.GameException.GameNotSetupException;
import com.tckb.c4.model.exception.GameException.InvalidGameSessionException;
import com.tckb.c4.model.exception.GameException.MaxPlayerRegisteredException;
import com.tckb.c4.model.exception.GameException.PlayerNotRegisteredException;
import com.tckb.c4.model.factory.BoardFactory.BoardType;
import com.tckb.c4.model.factory.GameFactory;
import com.tckb.c4.model.factory.GameFactory.FactoryType;
import com.tckb.c4.model.factory.PlayerFactory.PlayerType;
import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.Player;
import com.tckb.c4.ws.BoardConfiguration;
import com.tckb.c4.ws.repo.BoardGame;
import com.tckb.c4.ws.repo.BoardGameRepository;
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
    private BoardGameRepository repo;
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
                return new String[]{humanPlayer.getPlayerChip().getChipColor(), gameSessionId};

            } else {
                throw new GameNotSetupException("Game not configured, use '/setup' to configure the game.");
            }

        } catch (MaxPlayerRegisteredException ex) {
            thisLogger.log(Level.SEVERE, "Registration failed for reference: " + playerRef, ex);
        }
        return null;
    }

    public String[] registerAndJoinGame(String playerRef, String gameSession) throws InvalidGameSessionException, MaxPlayerRegisteredException {
        thisLogger.log(Level.INFO, "Joining new player with reference: {0}", playerRef);

        BoardGame playerBoard = repo.findByGameSessionId(gameSession);
        if (playerBoard != null) {
            thisLogger.log(Level.INFO, "PlayerBoard registered players: {0}", playerBoard.getGameBoard().getRegisteredPlayers().size());

            Player humanPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.Human, playerRef, playerBoard.getGameBoard());
            playerBoard.getGameBoard().registerPlayer(humanPlayer);

            repo.save(playerBoard);

            return new String[]{humanPlayer.getPlayerChip().getChipColor(), playerRef};

        } else {
            throw new InvalidGameSessionException("Game session does not exist,go ahead and create one, use /start to start the game.");
        }
    }

    public String[] placeBoardPiece(String playerRef, String boardColumn) throws PlayerNotRegisteredException, ColumnFilledException {
        BoardGame playerBoard = repo.findOne(playerRef);
        if (playerBoard != null) {
            Board gameBoard = playerBoard.getGameBoard();
            if (gameBoard.getRegisteredPlayers().size() != gameBoard.getMaxPlayers()) {
                return null;
            }
            Player humanPlayer = null, aiPlayer = null;

            for (Player boardPlayer : gameBoard.getRegisteredPlayers()) {
                if (boardPlayer instanceof HumanPlayer && boardPlayer.getReference().equals(playerRef)) {
                    humanPlayer = boardPlayer;
                }
                if (boardPlayer instanceof AiPlayer) {
                    aiPlayer = boardPlayer;
                }
            }
            if (humanPlayer != null && aiPlayer != null) {
                try {
                    String chip1 = gameBoard.placeChipOnBoard(humanPlayer.getReference(), Integer.parseInt(boardColumn));

                    thisLogger.log(Level.INFO, "Human player chip: {0}", chip1);

                    if (chip1 != null) {
                        String chip2 = gameBoard.placeChipOnBoard(aiPlayer.getReference(), aiPlayer.autoPlaceChipColumn());
                        thisLogger.log(Level.INFO, "AI player chip: {0}", chip2);

                        // update the game board
                        playerBoard.setGameBoard(gameBoard);
                        repo.save(playerBoard);
                        return new String[]{chip1, chip2};
                    } else {
                        throw new ColumnFilledException();
                    }
                } catch (GameFinished ge) {
                    repo.delete(playerBoard);
                    throw ge;
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

    /**
     *
     * @param configRequst
     */
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

    /**
     *
     * @param playerRef <p>
     * @return
     */
    public String getBoardSession(String playerRef) {
        BoardGame playerBoard = repo.findOne(playerRef);
        return playerBoard.getGameSessionId();
    }

    /**
     *
     * @param reference <p>
     * @return
     */
    public boolean checkIfRegisteredByRef(String reference) {
        return (repo.findOne(reference) != null);
    }

    /**
     *
     * @param gameSessionId
     * @param playerRef     <p>
     * @return
     */
    public boolean checkIfRegisteredBySessionId(String gameSessionId, String playerRef) {
        BoardGame board = repo.findByGameSessionId(gameSessionId);

        if (board != null) {
            if (board.getGameBoard().getRegisteredPlayers()
                    .stream()
                    .anyMatch((player) -> (player.getReference().equals(playerRef)))) {
                return true;
            }
        }
        return false;
    }

}
