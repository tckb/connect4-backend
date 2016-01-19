/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import com.tckb.c4.model.concrete.C4Board7X6;
import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.GameObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 *
 * @author tckb
 */
public class PlayerBoard extends GameObject {

    @Indexed
    @Id
    private String playerRef;
    private Board board;

    public PlayerBoard(String playerRef, Board board) {
        this.playerRef = playerRef;
        this.board = board;
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

    public void setBoard(C4Board7X6 board) {
        this.board = board;
    }

}
