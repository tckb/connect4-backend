/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for holding all expired boards.
 * <p>
 * @author tckb
 */
@Repository
public interface ExpiredBoardGameRepository extends MongoRepository<ExpiredBoardGame, String> {

}
