/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameFinished;
import com.tckb.c4.model.exception.GameException.IllegalMoveException;
import com.tckb.c4.model.exception.GameException.MaxPlayerRegisteredException;
import java.util.List;

/**
 * Contract for implementing any game board
 * <p>
 * @author tckb
 */
public interface Board {

    /**
     * Return the string representation of the board.
     * <p>
     * @return
     */
    public String printBoardStatus();

    /**
     * Returns a string array representation of the board pieces.
     * <p>
     * @return board data
     * <p>
     * @see #getWidth()
     * @see #getHeight()
     */
    public String[][] getBoardData();

    /**
     * Game finished status:<code>
     * GAME_IN_PROGRESS,
     * GAME_TIED,
     * GAME_ENDED. </code>
     */
    enum GameStatus {

        /**
         * Game has started.
         */
        GAME_IN_PROGRESS,
        /**
         * Game tie(No player has won).
         */
        GAME_TIED,
        /**
         * Game ended(one player has won.).
         */
        GAME_ENDED
    }

    /**
     *
     * @param player <p>
     * @throws
     * com.tckb.c4.model.exception.GameException.MaxPlayerRegisteredException
     */
    public void registerPlayer(Player player) throws MaxPlayerRegisteredException;

    /**
     * Places a board piece into the column
     * <p>
     * @param playerRef player making the move
     * @param column    column to which the player is placing the board piece
     * <p>
     * @return a string representation of the chip place (column:
     *         <code>column</code>, position: <code>row</code>), such that
     *         <code>1&lt;= row &lt;= {@link  #getHeight()}</code>
     * <br>
     * <p>
     * @throws ColumnFilledException thrown if you try to place on a column
     *                               which is already filled.
     * @throws GameFinished          thrown after the board piece is placed. If
     *                               the game is finished
     * @throws IllegalMoveException  thrown when a illegal move is placed by a
     *                               player. for eg,
     *                               <code>column &gt;= {@link #getWidth()}</code>
     * @see #getWidth()
     */
    public String placeChipOnBoard(String playerRef, Integer column) throws ColumnFilledException;

    /**
     * Returns the winning player. Incase of tie, returns null
     * <p>
     * @return winning player
     * <p>
     * @see GameStatus
     */
    public String getWinner();

    /**
     * Returns the number of players that are allowed for the board game
     * <p>
     * @return
     */
    public Integer getMaxPlayers();

    /**
     * Returns the width of the board.
     * <p>
     * @return width in cells
     */
    public Integer getWidth();

    /**
     * Returns the height of the board
     * <p>
     * @return height in cells
     */
    public Integer getHeight();

    /**
     * Returns all players registered to this board.
     * <p>
     * @return players playing this board game.
     */
    public List<Player> getRegisteredPlayers();

}
