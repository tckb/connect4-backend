/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.exception;

/**
 * An exception to be throwned when the maximum player limit is reached.
 */
public class MaxPlayerRegisteredException extends Exception {

    public MaxPlayerRegisteredException() {
        super();
    }

    public MaxPlayerRegisteredException(String msg) {
        super(msg);
    }

}
