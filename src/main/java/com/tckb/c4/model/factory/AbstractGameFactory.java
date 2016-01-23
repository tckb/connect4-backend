/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.factory;

import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.GameObjectType;

/**
 * An abstract factory for creating game instance.
 * <p>
 * @author tckb
 */
public abstract class AbstractGameFactory {

    /**
     * Create a Game instance from this factory.
     * <p>
     * @param type   type of instance to create.
     * @param params add any optional params.
     * <p>
     * @return the game instance.
     */
    public abstract GameObject createInstance(GameObjectType type, Object... params);

}
