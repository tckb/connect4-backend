/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.repo;

import com.tckb.c4.model.intf.GameObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tckb
 */
@Document(collection = "expired_boards")
public class ExpiredBoardGame implements GameObject {

    @Id
    @Indexed
    private final String boardSessionId;

    private final String winningPlayerRef;

    private final String winningBoardStatus;

    private final String winningMove;

    @PersistenceConstructor
    public ExpiredBoardGame(String boardSessionId, String winningPlayerRef, String winningMove, String winningBoardStatus) {
        this.winningPlayerRef = winningPlayerRef;
        this.boardSessionId = boardSessionId;
        this.winningBoardStatus = winningBoardStatus;
        this.winningMove = winningMove;
    }

    public String getWinningPlayerRef() {
        return winningPlayerRef;
    }

    public String getBoardSessionId() {
        return boardSessionId;
    }

    public String getWinningBoardStatus() {
        return winningBoardStatus;
    }

    public String getWinningMove() {
        return winningMove;
    }
}
