/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.exception;

/**
 * An exception to be throwned when a non-registered player tries to place a
 * board piece.
 */
public class PlayerNotRegisteredException extends RuntimeException {

    public PlayerNotRegisteredException() {
        super();
    }

    public PlayerNotRegisteredException(String msg) {
        super(msg);
    }

}
