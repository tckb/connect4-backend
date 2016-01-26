/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.exception;

import com.tckb.c4.model.intf.Board;

/**
 * A collections of all game exceptions
 * <p>
 * @author tckb
 */
public interface GameException {

    /**
     * An exception to be thrown when a column is filled.
     */
    class ColumnFilledException extends RuntimeException {

        public ColumnFilledException() {
            super();
        }

    }

    /**
     * An exception to be thrown when an illegal/expired board session is feed.
     */
    class InvalidGameSessionException extends Exception {

        public InvalidGameSessionException(String msg) {
            super(msg);
        }

    }

    /**
     * An exception to be thrown when a piece placement causes a game to finish.
     * <p>
     */
    class GameFinished extends RuntimeException {

        private final Board.GameStatus gameStatus;
        private final String winningMove;
        private final String winningPlayerRef;
        private final String boardData;

        public GameFinished(Board.GameStatus status, String winningPlayerRef, String winningMove, String boardData) {
            this.gameStatus = status;
            this.winningPlayerRef = winningPlayerRef;
            this.winningMove = winningMove;
            this.boardData = boardData;
        }

        public Board.GameStatus getGameStatus() {
            return gameStatus;
        }

        public String getWinningMove() {
            return winningMove;
        }

        public String getBoardData() {
            return boardData;
        }

        public String getWinningPlayerRef() {
            return winningPlayerRef;
        }

    }

    /**
     * An exception to be thrown when player tries to start a game before it has
     * been setup.
     */
    class GameNotSetupException extends Exception {

        public GameNotSetupException(String msg) {
            super(msg);
        }

    }

    /**
     * An exception to be throwned when on illegal move by a player.
     */
    class IllegalMoveException extends RuntimeException {

        public IllegalMoveException() {
            super();
        }

        public IllegalMoveException(String msg) {
            super(msg);
        }

    }

    /**
     * An exception to be throwned when the maximum player limit is reached.
     */
    class MaxPlayerRegisteredException extends Exception {

        public MaxPlayerRegisteredException() {
            super();
        }

        public MaxPlayerRegisteredException(String msg) {
            super(msg);
        }

    }

    /**
     * An exception to be throwned when a non-registered player tries to place a
     * board piece.
     */
    class PlayerNotRegisteredException extends RuntimeException {

        public PlayerNotRegisteredException() {
            super();
        }

        public PlayerNotRegisteredException(String msg) {
            super(msg);
        }

    }

}
