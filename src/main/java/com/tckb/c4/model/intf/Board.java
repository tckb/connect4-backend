/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

/**
 *
 * @author tckb
 */
public interface Board {

    /**
     * Game finished status
     */
    public enum GameStatus {

        /**
         * Game has started.
         */
        GAME_STARTED,
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
     * @throws com.tckb.c4.model.intf.Board.MaxPlayerRegisteredException
     */
    public void registerPlayer(Player player) throws MaxPlayerRegisteredException;

    /**
     * Places a board piece into the column
     * <p>
     * @param player player making the move
     * @param column column to which the player is placing the board piece
     * <p>
     * @return the exact piece placement on the board (for eg,
     *         <code>A1, A2</code> )
     * <p>
     * <code>null</code> if the column is already full.
     * <p>
     * @throws GameFinished         thrown after the board piece is placed. If
     *                              the game is finished
     * @throws IllegalMoveException thrown when a illegal move is placed by a
     *                              player.
     */
    public String moveBoardPiece(Player player, int column);

    /**
     * Returns the winning player. Incase of tie, returns null
     * <p>
     * @return winning player
     * <p>
     * @see GameStatus
     */
    public Player getWinner();

    /**
     * Returns the number of players that are allowed for the board game
     * <p>
     * @return
     */
    public int getMaxPlayers();

    /**
     * Returns the width of the board.
     * <p>
     * @return width in cells
     */
    public int getWidth();

    /**
     * Returns the height of the board
     * <p>
     * @return height in cells
     */
    public int getHeight();

    /**
     * Returns all players registered to this board.
     * <p>
     * @return players playing this board game.
     */
    public Player[] getRegisteredPlayers();

    /**
     * An exception to be throwned when the maximum player limit is reached.
     */
    public static class MaxPlayerRegisteredException extends Exception {

        public MaxPlayerRegisteredException() {
            super();
        }

        public MaxPlayerRegisteredException(String msg) {
            super(msg);
        }
    }

    /**
     * An exception to be throwned when on illegal move by a player.
     */
    public static class IllegalMoveException extends RuntimeException {

        public IllegalMoveException() {
            super();
        }

        public IllegalMoveException(String msg) {
            super(msg);
        }
    }

    /**
     * An exception to be throwned when a non-registered player tries to place a
     * board piece.
     */
    public static class PlayerNotRegisteredException extends Exception {

        public PlayerNotRegisteredException() {
            super();
        }

        public PlayerNotRegisteredException(String msg) {
            super(msg);
        }
    }

    /**
     * An exception to be throwned when a piece placement causes a game to
     * finish.
     * <p>
     * @see Board#moveBoardPiece(com.tckb.c4.model.intf.Player, int)
     * @see GameStatus
     */
    public static class GameFinished extends RuntimeException {

        private final GameStatus gameStatus;

        public GameFinished(GameStatus status, String msg) {
            super("Game Status: " + status + "; " + msg);
            this.gameStatus = status;
        }

        public GameStatus getGameStatus() {
            return gameStatus;
        }

    }

    /**
     * An exception to be throwned when a column is completely filled.
     */
    public static class ColumnFilledException extends Exception {

        public ColumnFilledException() {
            super();
        }
    }

}
