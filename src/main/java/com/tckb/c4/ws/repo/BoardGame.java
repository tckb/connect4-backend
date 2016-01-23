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
@Document(collection = "GameBoard")
public class BoardGame extends GameObject {

    @Indexed
    @Id
    @Field(value = "game_ref")
    private String playerRef;

    @Field(value = "game_board")
    private Board board;

    @Field(value = "game_session_id")
    private String boardRef;

    @PersistenceConstructor
    public BoardGame(String playerRef, Board board, String boardRef) {
        this.playerRef = playerRef;
        this.board = board;
        this.boardRef = boardRef;
    }

    public String getPlayerRef() {
        return playerRef;
    }

    public void setPlayerRef(String playerRef) {
        this.playerRef = playerRef;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getBoardRef() {
        return boardRef;
    }

    public void setBoardRef(String boardRef) {
        this.boardRef = boardRef;
    }

}
