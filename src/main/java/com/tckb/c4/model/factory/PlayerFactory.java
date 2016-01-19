/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.factory;

import com.tckb.c4.model.concrete.AiPlayer;
import com.tckb.c4.model.concrete.HumanPlayer;
import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.GameObject.GameObjectType;

/**
 * A factory for creating connect4 boards
 * <p>
 * @author tckb
 */
public class PlayerFactory extends AbstractGameFactory {

    @Override
    public GameObject createInstance(GameObjectType type, String... params) {
        if (type instanceof PlayerType) {

            switch ((PlayerType) type) {
                case Human:
                    return new HumanPlayer(params[0]);
                case AI:
                    return new AiPlayer("AIPlayer");
            }
        }
        return null;
    }

    /**
     * type of player to create.
     */
    public enum PlayerType implements GameObject.GameObjectType {

        /**
         * Human player.
         */
        Human,
        /**
         * computer player.
         */
        AI
    }

    /**
     * A Initialization-on-demand holder pattern for implementing Singleton
     */
    private static class SingletonHolder {

        private static final PlayerFactory INSTANCE = new PlayerFactory();
    }

    /**
     * Returns the instance of this factory
     * <p>
     * @return instance of this factory
     */
    public static PlayerFactory instance() {
        return SingletonHolder.INSTANCE;
    }

    private PlayerFactory() {
    }

}
