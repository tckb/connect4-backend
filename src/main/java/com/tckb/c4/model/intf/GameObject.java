/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A marker abstract class for the objects in game.
 * <p>
 * @author tckb
 */
@Document
public abstract class GameObject implements Serializable {

    /**
     * A marker interface for defining the type of game objects.
     */
    public static interface GameObjectType {
    }

}
