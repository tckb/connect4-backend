/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws;

import com.tckb.c4.model.intf.Board;

/**
 *
 * @author tckb
 */
public interface GameService {

    /**
     *
     * @return
     */
    String[] getOverallGameStats();

    /**
     *
     * @param playerRef <p>
     * @return <p>
     * @throws com.tckb.c4.model.intf.Board.PlayerNotRegisteredException
     */
    boolean isWinningPlayer(String playerRef) throws Board.PlayerNotRegisteredException;

    /**
     *
     * @param playerRef
     * @param boardColumn <p>
     * @return <p>
     * @throws com.tckb.c4.model.intf.Board.PlayerNotRegisteredException
     * @throws com.tckb.c4.model.intf.Board.ColumnFilledException
     */
    String[] placeBoardPiece(String playerRef, String boardColumn) throws Board.PlayerNotRegisteredException, Board.ColumnFilledException;

    /**
     *
     * @param playerRef
     */
    void registerAndStartGame(String playerRef);

}
