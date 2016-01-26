package com.tckb.c4.ws.impl;

import com.tckb.c4.model.concrete.AiPlayer;
import com.tckb.c4.model.concrete.HumanPlayer;
import com.tckb.c4.model.exception.GameException;
import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameFinished;
import com.tckb.c4.model.exception.GameException.GameNotSetupException;
import com.tckb.c4.model.exception.GameException.IllegalMoveException;
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
import com.tckb.c4.ws.repo.CurrentBoardGame;
import com.tckb.c4.ws.repo.CurrentBoardGameRepository;
import com.tckb.c4.ws.repo.ExpiredBoardGame;
import com.tckb.c4.ws.repo.ExpiredBoardGameRepository;
import java.util.ArrayList;
import java.util.List;
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
    private CurrentBoardGameRepository currentRepo;

    @Autowired
    private ExpiredBoardGameRepository expiredRepo;

    protected static final Logger thisLogger = Logger.getLogger(GameServiceImpl.class.getName());
    private BoardConfiguration boardConfig;

    /**
     * Creates new game for the user.
     * <p>
     * @param isMultiplayerGame is this a multi player game. If yes, waits for
     *                          new player to join the game.
     * <p>
     * @return boardSessionId,playerRefId and chipColor in that order.
     * <p>
     * @throws GameNotSetupException if the game is not setup previously setup.
     */
    public String[] createNewGameSession(boolean isMultiplayerGame) throws GameNotSetupException {
        try {

            if (boardConfig != null) {
                String playerRef = generateRandomId();
                String gameSessionId = generateRandomId();

                Board playerBoard = (Board) GameFactory.getFactory(FactoryType.BOARD).createInstance(BoardType.C4, boardConfig.getBoardWidth(), boardConfig.getBoardHeight(), boardConfig.getWinningConnections());

                Player humanPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.Human, playerRef);
                playerBoard.registerPlayer(humanPlayer);

                if (!isMultiplayerGame) {
                    Player aiPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.AI);
                    playerBoard.registerPlayer(aiPlayer);
                }

                currentRepo.save(new CurrentBoardGame(gameSessionId, playerBoard, playerRef));

                thisLogger.log(Level.FINE, "BoardPlayer saved in db successfully.");
                return new String[]{gameSessionId, playerRef, humanPlayer.getPlayerChip().getChipColor()};

            } else {
                throw new GameNotSetupException("Game not configured, use '/setup' to configure the game.");
            }

        } catch (MaxPlayerRegisteredException ex) {
            thisLogger.log(Level.SEVERE, "Can not create new game! ", ex);
        }
        return null;
    }

    /**
     * Joins game created from a different user.
     * <p>
     * @param gameSessionId game session id
     * <p>
     * @return boardSessionId,playerRefId and chipColor in that order.
     * <p>
     * @throws GameException.InvalidGameSessionException  if an invalid/expired
     *                                                    game sessions is
     *                                                    given.
     * @throws GameException.MaxPlayerRegisteredException if the board is
     *                                                    already full.
     */
    public String[] registerAndJoinGame(String gameSessionId) throws GameException.InvalidGameSessionException, GameException.MaxPlayerRegisteredException {

        CurrentBoardGame playerBoard = currentRepo.findOne(gameSessionId);
        if (playerBoard != null) {
            thisLogger.log(Level.INFO, "PlayerBoard registered players: {0}", playerBoard.getGameBoard().getRegisteredPlayers().size());

            if (playerBoard.getGameBoard().getRegisteredPlayers().size() < playerBoard.getGameBoard().getMaxPlayers()) {
                String playerRef = generateRandomId();
                Player humanPlayer = (Player) GameFactory.getFactory(FactoryType.PLAYER).createInstance(PlayerType.Human, playerRef);
                playerBoard.getGameBoard().registerPlayer(humanPlayer);

                currentRepo.save(playerBoard);
                return new String[]{gameSessionId, playerRef, humanPlayer.getPlayerChip().getChipColor()};

            } else {
                throw new MaxPlayerRegisteredException("This board allows only a maximum of  " + playerBoard.getGameBoard().getMaxPlayers() + "! Board is full, try creating one?");
            }
        } else {
            String errorMsg;
            ExpiredBoardGame expiredBoard = expiredRepo.findOne(gameSessionId);

            errorMsg = (expiredBoard != null) ? "Sorry, this game already ended! player: " + expiredBoard.getWinningPlayerRef() + " won the game :-)"
                    : "Game session does not exist, go ahead and create one, use /start to start the game.";

            throw new InvalidGameSessionException(errorMsg);
        }
    }

    public String[] placeBoardPiece(String gameSessionId, String playerRef, String boardColumn) throws PlayerNotRegisteredException, ColumnFilledException, InvalidGameSessionException {
        CurrentBoardGame playerBoard = currentRepo.findOne(gameSessionId);
        if (playerBoard != null) {
            Board gameBoard = playerBoard.getGameBoard();
            if (gameBoard.getRegisteredPlayers().size() != gameBoard.getMaxPlayers()) {
                return null;
            }

            Player playerMakingTheMove = null;
            List<Player> otherPlayers = new ArrayList<>();

            for (Player boardPlayer : gameBoard.getRegisteredPlayers()) {
                if (boardPlayer instanceof HumanPlayer && boardPlayer.getReference().equals(playerRef)) {
                    playerMakingTheMove = boardPlayer;
                } else {
                    otherPlayers.add(boardPlayer);
                }

            }
            if (playerMakingTheMove == null) {
                throw new PlayerNotRegisteredException("Sorry, you are not registered to play with this board");
            } else {
                // if this is this players turn
                if (playerMakingTheMove.getReference().equals(playerBoard.getNextTurn())) {
                    try {
                        String chip1 = playerMakingTheMove.placeChipOnBoard(gameBoard, Integer.parseInt(boardColumn) - 1);

                        thisLogger.log(Level.INFO, "Human player chip: {0}", chip1);

                        if (chip1 != null) {
                            String chip2 = null;
                            for (Player otherPlayer : otherPlayers) {
                                if (otherPlayer instanceof AiPlayer) {
                                    chip2 = otherPlayer.placeChipOnBoard(gameBoard);
                                    playerBoard.setNextTurn(playerMakingTheMove.getReference());
                                    thisLogger.log(Level.INFO, "AI player chip: {0}", chip2);
                                }
                                if (otherPlayer instanceof HumanPlayer) {
                                    playerBoard.setNextTurn(otherPlayer.getReference());
                                }
                            }

                            // update the game board
                            playerBoard.setGameBoard(gameBoard);
                            currentRepo.save(playerBoard);
                            return new String[]{chip1, chip2, gameBoard.printBoardStatus()};

                        } else {
                            throw new ColumnFilledException();
                        }
                    } catch (GameFinished ge) {
                        currentRepo.delete(playerBoard);
                        expiredRepo.save(new ExpiredBoardGame(gameSessionId, ge.getWinningPlayerRef(), ge.getWinningMove(), ge.getBoardData()));
                        throw ge;
                    }
                } else {
                    throw new IllegalMoveException("It is not yet your turn! let the other player make a move");
                }
            }

        } else {
            String errorMsg;
            ExpiredBoardGame expiredBoard = expiredRepo.findOne(gameSessionId);
            errorMsg = (expiredBoard != null) ? "Oh-oh! player: " + expiredBoard.getWinningPlayerRef() + " already won the game with move: " + expiredBoard.getWinningMove() + " :-/ wanna bet another one?"
                    : "Game session does not exist, go ahead and create one, use /start to start the game.";
            throw new InvalidGameSessionException(errorMsg);
        }
    }

    /**
     *
     * @return
     */
    public String[] getOverallGameStats() {
        return new String[]{Long.toString(currentRepo.count())};
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
     * Generate a random Id
     * <p>
     * @return
     */
    protected String generateRandomId() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

}
