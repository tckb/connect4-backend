/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.factory;

import com.tckb.c4.model.concrete.ConnectNBoard;
import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.GameObjectType;

/**
 * A factory for creating game boards.
 * <p>
 * @author tckb
 */
public class BoardFactory extends AbstractGameFactory {

    /**
     * type of board instance to create.
     */
    public enum BoardType implements GameObjectType {

        /**
         * Connect-n board, expected params(in-order): <br/>
         * <code> boardwidth, boardHeight, maxConnections</code>
         * <p>
         */
        ConnectN,
        /**
         * Chess board
         */
        Chess,
        /**
         * Go board
         */
        GO

    }

    @Override
    public GameObject createInstance(GameObjectType type, Object... params) {
        if (type instanceof BoardType) {
            switch ((BoardType) type) {
                case ConnectN:
                    return new ConnectNBoard((Integer) params[0], (Integer) params[1], (Integer) params[2]);
                default:
                    throw new UnsupportedOperationException("Board is out of stock ;)");
            }

        }
        return null;
    }

    /**
     * A Initialization-on-demand holder pattern for implementing Singleton
     */
    private static class SingletonHolder {

        private static final BoardFactory INSTANCE = new BoardFactory();
    }

    /**
     * Returns the instance of this factory
     * <p>
     * @return instance of this factory
     */
    public static BoardFactory instance() {
        return SingletonHolder.INSTANCE;
    }

    private BoardFactory() {
    }

}
