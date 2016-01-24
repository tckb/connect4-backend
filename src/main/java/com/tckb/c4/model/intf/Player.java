/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import com.tckb.c4.model.exception.GameException.IllegalMoveException;
import com.tckb.c4.model.factory.PlayerFactory.PlayerType;

/**
 * A contract defining the player
 * <p>
 * @author tckb
 */
public interface Player extends Comparable<Player> {

    /**
     *
     * @return
     */
    public BoardChip getPlayerChip();

    /**
     * Place a chip on the board by AI.
     * <p>
     * NB: Not to be used for human player, else this will raise
     * {@link IllegalMoveException}
     * <p>
     * <p>
     * @return chip placement on the board.
     * <p>
     * @see PlayerType#AI
     */
    public int autoPlaceChipColumn();

    /**
     * Return some reference to this player.
     * <p>
     * @return
     */
    public String getReference();

}
