/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

/**
 * A partial implementation of 7X6 Connect4 game board.
 * <p>
 * @author tckb
 */
public abstract class AbstractC4Board extends GameObject implements Board {

    protected final int MAX_PLAYERS = 2;
    protected final int WIDTH = 7;
    protected final int HEIGHT = 6;
    protected int nrPlayersJoined = 0;

    @Override
    public void registerPlayer(Player player) throws MaxPlayerRegisteredException {
        if (nrPlayersJoined <= MAX_PLAYERS) {
            nrPlayersJoined++;
            addPlayer(player);
        } else {
            throw new MaxPlayerRegisteredException("Board is full, max players already reached.");
        }
    }

    @Override
    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    public abstract void addPlayer(Player player);

}
