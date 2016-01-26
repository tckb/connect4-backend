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
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author tckb
 */
@Document(collection = "current_boards")
public class CurrentBoardGame implements GameObject {

    @Indexed
    @Id
    @Field("game_session_id")
    private String gameSessionId;

    @Field("game_board")
    private Board gameBoard;

    @Field("next_turn")
    private String nextTurn;

    @PersistenceConstructor
    public CurrentBoardGame(String gameSessionId, Board gameBoard, String nextTurn) {
        this.gameBoard = gameBoard;
        this.gameSessionId = gameSessionId;
        this.nextTurn = nextTurn;
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
        return "{" + this.gameSessionId + "}";
    }

    public String getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(String nextTurn) {
        this.nextTurn = nextTurn;
    }

}
