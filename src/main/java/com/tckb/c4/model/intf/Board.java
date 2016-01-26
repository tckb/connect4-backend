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
 *
 * @author tckb
 */
public interface Board {

    public String printBoardStatus();

    /**
     *
     * @return
     */
    public String[][] getBoardData();

    /**
     * Game finished status: One of
     * <p>
     * GAME_IN_PROGRESS,
     * <br/>
     * GAME_TIED,
     * <br/>
     * GAME_ENDED.
     */
    public enum GameStatus {

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
     * @return the exact piece placement on the board (for eg,
     *         <code>A1, A2</code> )
     * <p>
     * <code>null</code> if the column is already full.
     * <p>
     * @throws ColumnFilledException thrown if you try to place on a column
     *                               which is already filled.
     * @throws GameFinished          thrown after the board piece is placed. If
     *                               the game is finished
     * @throws IllegalMoveException  thrown when a illegal move is placed by a
     *                               player.
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
