/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import com.tckb.c4.model.intf.GameObject;
import java.io.Serializable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * A generic game repository for storing all game related information
 * <p>
 * @author tckb
 * @param <T> data to be stored.
 * @param <V> key for mapping the data.
 */
public interface GameRepository<T extends GameObject, V extends Serializable> extends MongoRepository<T, V> {

}
