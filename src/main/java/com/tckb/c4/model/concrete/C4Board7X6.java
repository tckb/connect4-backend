/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.BoardChip;
import com.tckb.c4.model.intf.AbstractC4Board;
import com.tckb.c4.model.intf.Player;
import java.util.HashMap;
import java.util.Map;

/**
 * a concrete implementation of board
 */
public class C4Board7X6 extends AbstractC4Board {

    private final Map<Player, BoardChip> playerAndChip;
    private final BoardChip[][] boardData;

    public C4Board7X6(Map<Player, BoardChip> playerAndChip, BoardChip[][] boardData) {
        this.playerAndChip = playerAndChip;
        this.boardData = boardData;
    }

    public C4Board7X6() {
        playerAndChip = new HashMap<>();
        boardData = new BoardChip[HEIGHT][WIDTH];
    }

    @Override
    public void addPlayer(Player player) {
        playerAndChip.put(player, player.getPlayerChip());
    }

    @Override
    public Player getWinner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String moveBoardPiece(Player player, int column) {

        if (!playerAndChip.containsKey(player)) {
            throw new PlayerNotRegisteredException("player: " + player.getReference() + " is not registered with this board.");
        }

        BoardChip playerChip = player.getPlayerChip();
        return null;
    }

    @Override
    public Player[] getRegisteredPlayers() {
        return playerAndChip.keySet().toArray(new Player[nrPlayersJoined]);
    }

}
