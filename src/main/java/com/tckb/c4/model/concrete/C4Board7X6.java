/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.AbstractC4Board;
import com.tckb.c4.model.intf.BoardChip;
import com.tckb.c4.model.intf.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * a concrete implementation of board
 */
public class C4Board7X6 extends AbstractC4Board {

    private final List<Player> registeredPlayers;
    private final BoardChip[][] boardData;

    public C4Board7X6(List<Player> registeredPlayers, BoardChip[][] boardData) {
        this.registeredPlayers = registeredPlayers;
        this.boardData = boardData;
    }

    public C4Board7X6() {
        registeredPlayers = new ArrayList<>();
        boardData = new BoardChip[HEIGHT][WIDTH];
    }

    @Override
    public void addPlayer(Player player) {
        registeredPlayers.add(player);
    }

    @Override
    public Player getWinner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String moveBoardPiece(Player player, int column) {

        if (!registeredPlayers.contains(player)) {
            throw new PlayerNotRegisteredException("player: " + player.getReference() + " is not registered with this board.");
        }

        BoardChip playerChip = player.getPlayerChip();
        return null;
    }

    @Override
    public Player[] getRegisteredPlayers() {
        return registeredPlayers.toArray(new Player[nrPlayersJoined]);
    }

}
