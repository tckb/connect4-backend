/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws;

import com.tckb.c4.model.exception.GameNotSetupException;
import com.tckb.c4.model.exception.ColumnFilledException;
import com.tckb.c4.model.exception.PlayerNotRegisteredException;

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
     */
    boolean isWinningPlayer(String playerRef) throws PlayerNotRegisteredException;

    /**
     *
     * @param playerRef
     * @param boardColumn <p>
     * @return <p>
     * @throws com.tckb.c4.model.exception.ColumnFilledException
     */
    public String[] placeBoardPiece(String playerRef, String boardColumn) throws PlayerNotRegisteredException, ColumnFilledException;

    /**
     *
     * @param playerRef
     * @param isMultiplayerGame <p>
     * @return @throws com.tckb.c4.ws.GameNotSetupException
     */
    public String[] registerAndStartGame(String playerRef, boolean isMultiplayerGame) throws GameNotSetupException;

    /**
     *
     * @param configRequst
     */
    public void setupBoard(BoardConfiguration configRequst);

}
