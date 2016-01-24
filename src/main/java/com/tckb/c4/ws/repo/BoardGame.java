/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.GameObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tckb
 */
@Document
public class BoardGame implements GameObject {

    @Indexed
    @Id
    private String id;

    private Board gameBoard;

    private String gameSessionId;

    @PersistenceConstructor
    public BoardGame(String id, Board gameBoard, String gameSessionId) {
        this.id = id;
        this.gameBoard = gameBoard;
        this.gameSessionId = gameSessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    @Override
    public String toString() {
        return "{" + this.id + ":" + this.gameSessionId + "}";
    }

}
