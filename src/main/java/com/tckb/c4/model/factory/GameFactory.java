/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.factory;

/**
 *
 * @author tckb
 */
public class GameFactory {

    /**
     *
     */
    public enum FactoryType {

        BOARD,
        PLAYER
    }

    public static AbstractGameFactory getFactory(FactoryType type) {
        switch (type) {
            case BOARD:
                return BoardFactory.instance();
            case PLAYER:
                return PlayerFactory.instance();
        }
        return null;
    }
}
