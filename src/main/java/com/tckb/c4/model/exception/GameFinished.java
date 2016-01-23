/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.exception;

import com.tckb.c4.model.intf.Board;

/**
 * An exception to be throwned when a piece placement causes a game to
 * finish.
 * <p>
 * @see Board#moveBoardPiece(com.tckb.c4.model.intf.Player, int)
 * @see GameStatus
 */
public class GameFinished extends RuntimeException {
    private final Board.GameStatus gameStatus;

    public GameFinished(Board.GameStatus status, String chipPlace) {
        super("Game Status: " + status + ";winning move: " + chipPlace);
        this.gameStatus = status;
    }

    public Board.GameStatus getGameStatus() {
        return gameStatus;
    }

}
