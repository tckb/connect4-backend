/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.exception;

/**
 * An exception to be throwned when on illegal move by a player.
 */
public class IllegalMoveException extends RuntimeException {

    public IllegalMoveException() {
        super();
    }

    public IllegalMoveException(String msg) {
        super(msg);
    }

}
