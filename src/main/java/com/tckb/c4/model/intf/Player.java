/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import com.tckb.c4.model.concrete.BoardChip;
import com.tckb.c4.model.exception.GameException.IllegalMoveException;
import com.tckb.c4.model.factory.PlayerFactory.PlayerType;

/**
 * A contract defining the player
 * <p>
 * @author tckb
 */
public interface Player extends Comparable<Player> {

    /**
     * Returns the board chip for this player.
     * <p>
     * @return
     */
    public BoardChip getPlayerChip();

    /**
     * Return some reference to this player.
     * <p>
     * @return
     */
    public String getReference();

    /**
     * Place a chip on the board by AI.
     * <p>
     * NB: Not to be used for human player, else this will raise
     * {@link IllegalMoveException}
     * <p>
     * @param gameBoard <p>
     * @return chip placement on the board.
     * <p>
     * @see PlayerType#AI
     */
    public String placeChipOnBoard(Board gameBoard);

    /**
     * Places a chip on board by human player.
     * <br/>
     * NB: Not to be used for ai player, else this will raise
     * {@link IllegalMoveException}
     * <p>
     * @param gameBoard board which this player is registered to.
     * @param column    column of the board.
     * <p>
     * @return a representation the chip place.
     * <p>
     * @see PlayerType#Human
     */
    public String placeChipOnBoard(Board gameBoard, int column);

}
