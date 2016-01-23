/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.exception.ColumnFilledException;
import com.tckb.c4.model.exception.GameFinished;
import com.tckb.c4.model.exception.IllegalMoveException;
import com.tckb.c4.model.exception.MaxPlayerRegisteredException;
import com.tckb.c4.model.exception.PlayerNotRegisteredException;
import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.BoardChip;
import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Connect-n game board.
 * <p>
 * @author tckb
 */
public class ConnectNBoard extends GameObject implements Board {

    @Field(value = "board_width")
    private int boardWidth;
    @Field(value = "board_height")
    private int boardHeight;
    @Field(value = "max_players")
    private int maxPlayers;
    @Field(value = "max_win_con")
    private int maxConnections;
    @Field(value = "board_data")
    private BoardChip[][] boardData;
    @Field(value = "registered_players")
    private List<Player> registeredPlayers;
    @Field(value = "players_joined")
    private int nrPlayersJoined;
    @Field(value = "winning_player")
    private Player winningPlayer;

    @PersistenceConstructor
    public ConnectNBoard(Integer width, Integer height, Integer winingConnections) {
        maxPlayers = 2;
        nrPlayersJoined = 0;
        registeredPlayers = new ArrayList<>();
        this.boardWidth = width;
        this.boardHeight = height;
        this.maxConnections = winingConnections;
        boardData = new BoardChip[boardWidth][boardHeight];
    }

    /**
     * Test if the board is completely filled
     * <p>
     * @return true if the board is completely filled with board pieces
     */
    private boolean isBoardFull() {
        int colsFilled = 0;
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (boardData[r][c] != null) {
                    colsFilled++;
                }
            }
        }
        return colsFilled == (getHeight() * getWidth());
    }

    /**
     * Check if any 4 board pieces are in-line.
     * <p>
     * @return
     */
    private Player checkforConnect4() {

        Map<Player, Integer> playerConnections = new HashMap<>();

        checkAndResetCounter(playerConnections);

        Player winner = null;

        // check vertical connections (|)
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y <= getHeight() - maxConnections; y++) {
                for (int o = 0; o < maxConnections; o++) {
                    BoardChip chip = boardData[x][y + o];
                    if (chip != null) {
                        for (Player player : getRegisteredPlayers()) {
                            if (chip.equals(player.getPlayerChip())) {
                                playerConnections.put(player, playerConnections.get(player) + 1);
                            }
                        }
                    }

                }
            }

            winner = checkAndResetCounter(playerConnections);

            if (winner != null) {
                return winner;
            }

            // check horizontal connections  (--)
            for (int y = 0; y < getHeight(); y++) {
                for (x = 0; x <= getWidth() - maxConnections; x++) {
                    for (int o = 0; o < maxConnections; o++) {
                        BoardChip chip = boardData[x][y + o];
                        if (chip != null) {
                            for (Player player : getRegisteredPlayers()) {
                                if (chip.equals(player.getPlayerChip())) {
                                    playerConnections.put(player, playerConnections.get(player) + 1);

                                }
                            }
                        }

                    }
                }

            }

            winner = checkAndResetCounter(playerConnections);

            if (winner != null) {
                return winner;
            }

            // check right diagonal (/)
            for (x = 0; x <= getWidth() - maxConnections; x++) {
                for (int y = 0; y <= getHeight() - maxConnections; y++) {

                    for (int o = 0; o < maxConnections; o++) {
                        BoardChip chip = boardData[x][y + o];
                        if (chip != null) {
                            for (Player player : getRegisteredPlayers()) {
                                if (chip.equals(player.getPlayerChip())) {
                                    playerConnections.put(player, playerConnections.get(player) + 1);

                                }
                            }
                        }

                    }
                }

            }
        }

        winner = checkAndResetCounter(playerConnections);

        if (winner != null) {
            return winner;
        }
        // check left diagonal (\)
        for (int x = getWidth() - 1; x >= maxConnections - 1; x--) {
            for (int y = 0; y <= getHeight() - maxConnections; y++) {
                for (int o = 0; o < maxConnections; o++) {
                    BoardChip chip = boardData[x][y + o];
                    if (chip != null) {
                        for (Player player : getRegisteredPlayers()) {
                            if (chip.equals(player.getPlayerChip())) {
                                playerConnections.put(player, playerConnections.get(player) + 1);

                            }
                        }
                    }

                }

            }
        }

        winner = checkAndResetCounter(playerConnections);

        return winner;
    }

    @Override
    public void registerPlayer(Player player) throws MaxPlayerRegisteredException {
        if (nrPlayersJoined <= getMaxPlayers()) {
            nrPlayersJoined++;
            addPlayer(player);
        } else {
            throw new MaxPlayerRegisteredException("Board is full, max players already reached.");
        }
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    private boolean checkIfLegalMove(int column) {
        if (column > getWidth()) {
            throw new IllegalMoveException();
        }
        return true;
    }

    private Player checkAndResetCounter(Map<Player, Integer> playerConnections) {
        for (Player player : getRegisteredPlayers()) {
            if (playerConnections.containsKey(player) && playerConnections.get(player) == maxConnections) {
                // bingo! there is a winner
                return player;
            }
            playerConnections.put(player, 0);
        }
        return null;
    }

    @Override
    public String moveBoardPiece(Player player, int column) throws ColumnFilledException {

        if (!getRegisteredPlayers().contains(player)) {
            throw new PlayerNotRegisteredException("player: " + player.getReference() + " is not registered with this board.");
        }
        BoardChip playerChip = player.getPlayerChip();
        String chipPlace = null;
        if (checkIfLegalMove(column)) {
            chipPlace = placeBoardChip(playerChip, column);
            GameStatus currentGameStatus = getCurrentGameStatus();
            if (!currentGameStatus.equals(GameStatus.GAME_IN_PROGRESS)) {
                throw new GameFinished(currentGameStatus, chipPlace);
            }
        }

        return chipPlace;
    }

    public void addPlayer(Player player) {
        registeredPlayers.add(player);
    }

    public GameStatus getCurrentGameStatus() {
        this.winningPlayer = checkforConnect4();
        if (this.winningPlayer != null) {
            return GameStatus.GAME_ENDED;
        } else {
            if (isBoardFull()) {
                return GameStatus.GAME_TIED;
            }
        }
        return GameStatus.GAME_IN_PROGRESS;
    }

    public String placeBoardChip(BoardChip playerChip, int column) throws ColumnFilledException {
        for (int y = 0; y <= getHeight(); y++) {
            if (boardData[column][y] == null) {
                boardData[column][y] = playerChip;
                return "column: " + column + ", position: " + y;
            }
        }
        throw new ColumnFilledException();
    }

    @Override
    public int getHeight() {
        return this.boardHeight;
    }

    @Override
    public int getWidth() {
        return this.boardWidth;
    }

    @Override
    public Player getWinner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getRegisteredPlayers() {
        return Collections.unmodifiableList(this.registeredPlayers);
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public BoardChip[][] getBoardData() {
        return boardData;
    }

    public void setBoardData(BoardChip[][] boardData) {
        this.boardData = boardData;
    }

    public int getNrPlayersJoined() {
        return nrPlayersJoined;
    }

    public void setNrPlayersJoined(int nrPlayersJoined) {
        this.nrPlayersJoined = nrPlayersJoined;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(Player winningPlayer) {
        this.winningPlayer = winningPlayer;
    }
}
