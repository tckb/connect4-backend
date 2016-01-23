/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import com.tckb.c4.model.factory.PlayerFactory.PlayerType;
import com.tckb.c4.model.exception.GameFinished;
import com.tckb.c4.model.intf.Board.GameStatus;
import com.tckb.c4.model.exception.IllegalMoveException;

/**
 * A contract defining the player
 * <p>
 * @author tckb
 */
public interface Player extends Comparable<Player> {

    public BoardChip getPlayerChip();

    /**
     * Places a chip on to the board by a Player.
     * <p>
     * NB: Not to be used for AI player, else this will raise
     * {@link IllegalMoveException}
     * <p>
     * @param column on of the following columns.
     * <p>
     * @return chip placement on the board.
     * <p>
     * @see Board#getWidth()
     * @throws IllegalMoveException on invalid move
     * @throws GameFinished         when the game arrives to a conclusion
     * @see GameStatus
     * @see PlayerType#Human
     */
    public String placeChipOnBoard(int column);

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
    public String placeChipOnBoard();

    /**
     * Return some reference to this player.
     * <p>
     * @return
     */
    public String getReference();
}
