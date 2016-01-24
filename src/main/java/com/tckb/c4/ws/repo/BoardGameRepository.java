/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author tckb
 */
public interface BoardGameRepository extends MongoRepository<BoardGame, String> {

    public BoardGame findByGameSessionId(String gameSessionId);

}
