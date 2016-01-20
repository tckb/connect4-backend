/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.factory;

import com.tckb.c4.model.concrete.AiPlayer;
import com.tckb.c4.model.concrete.HumanPlayer;
import com.tckb.c4.model.intf.BoardChip;
import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.GameObject.GameObjectType;
import java.util.Random;

/**
 * A factory for creating connect4 players
 * <p>
 * @author tckb
 */
public class PlayerFactory extends AbstractGameFactory {

    @Override
    public GameObject createInstance(GameObjectType type, String... params) {
        if (type instanceof PlayerType) {
            /**
             * create a random colored chip for the player
             */
            BoardChip randomChip = new BoardChip() {

                @Override
                public String getColor() {
                    return BoardChip.CHIP_COLORS[new Random().nextInt(BoardChip.CHIP_COLORS.length)];
                }
            };

            switch ((PlayerType) type) {

                case Human:
                    HumanPlayer player = new HumanPlayer(params[0]);
                    player.setChip(randomChip);

                    return player;

                case AI:
                    AiPlayer aiPlayer = new AiPlayer("aiPlayer");
                    aiPlayer.setChip(randomChip);
                    return aiPlayer;
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
