/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws;

import com.tckb.c4.model.exception.GameException;
import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameNotSetupException;
import com.tckb.c4.model.exception.GameException.InvalidGameSessionException;
import com.tckb.c4.model.exception.GameException.PlayerNotRegisteredException;
import com.tckb.c4.ws.impl.GameServiceImpl.GameOverAllStats;
import org.springframework.stereotype.Service;

/**
 *
 * @author tckb
 */
@Service
public interface GameService {

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
    String[] createNewGameSession(boolean isMultiplayerGame) throws GameNotSetupException;

    /**
     * Get the entire game stats.
     * <p>
     * @return game board stats
     */
    GameOverAllStats getOverallGameStats();

    /**
     * Places a board piece.
     * <p>
     * @param gameSessionId game reference.
     * @param playerRef     player reference.
     * @param boardColumn   board column.
     * <p>
     * @return a string array of {thisPlayer move, opponent move, thisPlayer
     *         chip color, game board}
     * <p>
     * @throws GameException.PlayerNotRegisteredException
     * @throws GameException.ColumnFilledException
     * @throws GameException.InvalidGameSessionException
     */
    String[] placeBoardPiece(String gameSessionId, String playerRef, String boardColumn) throws PlayerNotRegisteredException, ColumnFilledException, InvalidGameSessionException;

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
    String[] registerAndJoinGame(String gameSessionId) throws GameException.InvalidGameSessionException, GameException.MaxPlayerRegisteredException;

    /**
     * setup the board game.
     * <p>
     * @param configRequst
     */
    void setupBoard(BoardConfiguration configRequst);

}
