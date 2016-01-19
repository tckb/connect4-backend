/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.AbstractC4Board;
import com.tckb.c4.model.intf.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * a concrete implementation of board
 */
public class C4Board7X6 extends AbstractC4Board {

    private final List<Player> players;
    private final BoardChip[][] boardData;

    public C4Board7X6() {
        players = new ArrayList<>();
        boardData = new BoardChip[HEIGHT][WIDTH];
    }

    public C4Board7X6(List<Player> players, BoardChip[][] boardData) {
        this.players = players;
        this.boardData = boardData;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public Player getWinner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String moveBoardPiece(Player player, int column) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player[] getRegisteredPlayers() {
        return players.toArray(new Player[super.nrPlayersJoined]);
    }

}
