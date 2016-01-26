/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameFinished;
import com.tckb.c4.model.exception.GameException.IllegalMoveException;
import com.tckb.c4.model.exception.GameException.MaxPlayerRegisteredException;
import com.tckb.c4.model.exception.GameException.PlayerNotRegisteredException;
import com.tckb.c4.model.intf.Board;
import com.tckb.c4.model.intf.BoardChip;
import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * A Connect-n game board.
 * <p>
 * @author tckb
 */
public class ConnectNBoard implements GameObject, Board {

    private final Integer boardWidth;
    private final Integer boardHeight;
    private final Integer maxPlayers;
    private final Integer maxConnections;
    private final BoardChip[][] boardData;
    private final List<Player> registeredPlayers;
    private Integer nrPlayersJoined;
    private String winningPlayer;
    protected static final Logger thisLogger = Logger.getLogger(Board.class.getName());

    @PersistenceConstructor
    public ConnectNBoard(Integer boardWidth, Integer boardHeight, Integer maxConnections) {
        thisLogger.log(Level.INFO, "Initializing board with width: {0} height: {1}", new Object[]{boardWidth, boardHeight});
        maxPlayers = 2;
        nrPlayersJoined = 0;
        registeredPlayers = new ArrayList<>();
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.maxConnections = maxConnections;
        boardData = new BoardChip[boardWidth][boardHeight];
        winningPlayer = null;
    }

    /**
     * Test if the board is completely filled
     * <p>
     * @return true if the board is completely filled with board pieces
     */
    private boolean isBoardFull() {
        Integer colsFilled = 0;
        for (Integer r = 0; r < boardHeight; r++) {
            for (Integer c = 0; c < boardWidth; c++) {
                if (boardData[c][r] != null) {
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
    private String checkforConnect4() {

        thisLogger.info("Checking rows...");
        //Check Rows
        for (int rows = 0; rows < boardHeight; rows++) {
            for (int columns = 0; columns <= boardWidth - maxConnections; columns++) {
                boolean player1Connect = true, player2Connect = true;

                for (int o = 0; o < maxConnections; o++) {

                    if (player1Connect
                            && !Objects.equals(boardData[columns + o][rows], registeredPlayers.get(0).getPlayerChip())) {
                        player1Connect = false;

                    }
                    if (player2Connect
                            && !Objects.equals(boardData[columns + o][rows], registeredPlayers.get(1).getPlayerChip())) {
                        player2Connect = false;

                    }
                }

                if (player1Connect) {
                    return registeredPlayers.get(0).getReference();

                }
                if (player2Connect) {
                    return registeredPlayers.get(1).getReference();

                }

            }
        }

        thisLogger.info("Checking columns...");

        //Check Columns
        for (int columns = 0; columns < boardWidth; columns++) {
            for (int rows = 0; rows <= boardHeight - maxConnections; rows++) {

                boolean player1Connect = true, player2Connect = true;

                for (int o = 0; o < maxConnections; o++) {

                    if (player1Connect
                            && !Objects.equals(boardData[columns][rows + o], registeredPlayers.get(0).getPlayerChip())) {
                        player1Connect = false;

                    }
                    if (player2Connect
                            && !Objects.equals(boardData[columns][rows + o], registeredPlayers.get(1).getPlayerChip())) {
                        player2Connect = false;

                    }
                }
                if (player1Connect) {
                    return registeredPlayers.get(0).getReference();

                }
                if (player2Connect) {
                    return registeredPlayers.get(1).getReference();

                }
            }
        }

        thisLogger.info("Checking diagonals (SW-NE)...");
        //Check Diagonals (SW-NE)
        for (int rows = 0; rows <= boardHeight - maxConnections; rows++) {
            for (int columns = 0; columns < boardWidth - maxConnections - 1; columns++) {
                boolean player1Connect = true, player2Connect = true;

                for (int o = 0; o < maxConnections; o++) {

                    if (player1Connect
                            && !Objects.equals(boardData[columns + o][rows + o], registeredPlayers.get(0).getPlayerChip())) {
                        player1Connect = false;

                    }
                    if (player2Connect
                            && !Objects.equals(boardData[columns + o][rows + o], registeredPlayers.get(1).getPlayerChip())) {
                        player2Connect = false;

                    }
                }

                if (player1Connect) {
                    return registeredPlayers.get(0).getReference();

                }
                if (player2Connect) {
                    return registeredPlayers.get(1).getReference();

                }
            }
        }

        thisLogger.info("Checking diagonals (NE-SW)...");
        //Check Diagonals (NE-SW)
        for (int rows = 0; rows < boardHeight - maxConnections - 1; rows++) {
            for (int columns = maxConnections - 1; columns < boardWidth; columns++) {
                boolean player1Connect = true, player2Connect = true;

                for (int o = 0; o < maxConnections; o++) {

                    if (player1Connect
                            && !Objects.equals(boardData[columns - o][rows + o], registeredPlayers.get(0).getPlayerChip())) {
                        player1Connect = false;

                    }
                    if (player2Connect
                            && !Objects.equals(boardData[columns - o][rows + o], registeredPlayers.get(1).getPlayerChip())) {
                        player2Connect = false;

                    }
                }

                if (player1Connect) {
                    return registeredPlayers.get(0).getReference();

                }
                if (player2Connect) {
                    return registeredPlayers.get(1).getReference();

                }
            }
        }

        return null;

    }

    @Override
    public void registerPlayer(Player player) throws MaxPlayerRegisteredException {
        if (nrPlayersJoined < getMaxPlayers()) {
            nrPlayersJoined++;
            addPlayer(player);
        } else {
            throw new MaxPlayerRegisteredException("Board is full, max players already reached.");
        }
    }

    @Override
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    private boolean checkIfLegalMove(Integer column) {
        thisLogger.log(Level.INFO, "Checking if {0} has a legal move", column);
        if (column > getWidth()) {
            throw new IllegalMoveException();
        }
        return true;
    }

    @Override
    public String placeChipOnBoard(String playerRef, Integer column) throws ColumnFilledException {

        Player playerToMove = null;
        for (Player player : getRegisteredPlayers()) {
            if (player.getReference().equals(playerRef)) {
                playerToMove = player;
            }
        }

        if (playerToMove == null) {
            throw new PlayerNotRegisteredException("player: " + playerRef + " is not registered with this board.");
        }
        BoardChip playerChip = playerToMove.getPlayerChip();
        String chipPlace = null;
        if (checkIfLegalMove(column)) {
            thisLogger.log(Level.INFO, "placing a chip on column: {0}", column);
            chipPlace = placeBoardChip(playerChip, column);
            GameStatus currentGameStatus = getCurrentGameStatus();
            thisLogger.log(Level.INFO, "Game Status: {0}", currentGameStatus);
            thisLogger.log(Level.INFO, "Board Status:\n\n{0}", gridStatus());

            if (!currentGameStatus.equals(GameStatus.GAME_IN_PROGRESS)) {
                throw new GameFinished(currentGameStatus, playerRef, chipPlace, gridStatus());
            }
        }

        return chipPlace;
    }

    private void addPlayer(Player player) {
        registeredPlayers.add(player);
    }

    private GameStatus getCurrentGameStatus() {
        this.winningPlayer = checkforConnect4();
        if (this.winningPlayer != null) {
            thisLogger.log(Level.INFO, "Winning player: {0}", winningPlayer);
            return GameStatus.GAME_ENDED;
        } else {
            if (isBoardFull()) {
                return GameStatus.GAME_TIED;
            }
        }
        return GameStatus.GAME_IN_PROGRESS;
    }

    private String placeBoardChip(BoardChip playerChip, Integer column) throws ColumnFilledException {
        for (Integer y = 0; y < getHeight(); y++) {
            if (boardData[column][y] == null) {
                boardData[column][y] = playerChip;
                return "column: " + (column + 1) + ", position: " + (y + 1);
            }
        }
        throw new ColumnFilledException();
    }

    @Override
    public Integer getHeight() {
        return this.boardHeight;
    }

    @Override
    public Integer getWidth() {
        return this.boardWidth;
    }

    @Override
    public String getWinner() {
        return this.winningPlayer;
    }

    @Override
    public List<Player> getRegisteredPlayers() {
        return Collections.unmodifiableList(this.registeredPlayers);
    }

    public Integer getBoardWidth() {
        return boardWidth;
    }

    public Integer getBoardHeight() {
        return boardHeight;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    @Override
    public String[][] getBoardData() {
        String[][] boardDataString = new String[boardWidth][boardHeight];

        for (int rows = 0; rows < boardHeight; rows++) {
            for (int columns = 0; columns < boardWidth; columns++) {
                BoardChip chip = boardData[columns][rows];
                if (chip != null) {
                    boardDataString[columns][rows] = chip.getChipColor();
                } else {
                    boardDataString[columns][rows] = "X";
                }
            }
        }
        return boardDataString;
    }

    public Integer getNrPlayersJoined() {
        return nrPlayersJoined;
    }

    public void setNrPlayersJoined(Integer nrPlayersJoined) {
        this.nrPlayersJoined = nrPlayersJoined;
    }

    public String gridStatus() {
        StringBuilder board = new StringBuilder();
        for (int rows = boardHeight - 1; rows >= 0; rows--) {
            board.append("row: ").append(rows + 1).append("|\t");
            for (int columns = 0; columns < boardWidth; columns++) {
                BoardChip chip = boardData[columns][rows];
                if (chip != null) {
                    for (Player player : getRegisteredPlayers()) {
                        if (chip.equals(player.getPlayerChip())) {
                            board.append("[").append(chip.getChipColor()).append("]");
                        }
                    }

                } else {
                    board.append("[..........]");
                }
                board.append(" ");
            }
            board.append("\n");
        }
        return board.toString();
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(String winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    @Override
    public String printBoardStatus() {
        return gridStatus();
    }
}
