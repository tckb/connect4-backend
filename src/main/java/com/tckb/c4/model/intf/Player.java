/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

/**
 * A contract defining the player
 * <p>
 * @author tckb
 */
public interface Player extends Comparable<Player> {

    public BoardChip getPlayerChip();

    /**
     * Places a chip on to the column.
     * <p>
     * @param column on of the following columns (A,B,C,D,E)
     * <p>
     * @return returns <code>false</code> iff the column is full, else
     *         <code>true</code>
     * <p>
     * @see Board
     */
    public String placeChipOnBoard(int column);

    /**
     * Return some reference to this player.
     * <p>
     * @return
     */
    public String getReference();
}
