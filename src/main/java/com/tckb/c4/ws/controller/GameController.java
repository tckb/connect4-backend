/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.controller;

import com.tckb.c4.model.intf.Board.ColumnFilledException;
import com.tckb.c4.model.intf.Board.GameFinished;
import com.tckb.c4.model.intf.Board.GameStatus;
import com.tckb.c4.model.intf.Board.IllegalMoveException;
import com.tckb.c4.model.intf.Board.PlayerNotRegisteredException;
import com.tckb.c4.ws.WebServiceResponse;
import com.tckb.c4.ws.impl.GameServiceImpl;
import java.text.MessageFormat;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the game.
 * <p>
 * @author tckb
 */
@RestController
@PropertySource("classpath:message.properties")
public class GameController {

    @Value("${game.register.new}")
    private String newGameMsg;
    @Value("${game.register.old}")
    private String oldGameMsg;
    @Value("${{game.result.win.ai}")
    private String aiWinMsg;
    @Value("${game.result.win.player}")
    private String playerWinMsg;
    @Value("${game.result.tie}")
    private String gameTieMsg;
    @Value("${game.error.invalid-move}")
    private String invalidMoveMsg;
    @Value("${game.error.invalid-move-column}")
    private String invalidMoveColumnMsg;
    @Value("${game.error.invalid-player}")
    private String invalidPlayerMsg;
    @Value("${game.error.invalid-ref}")
    private String invalidPlayerRefMsg;

    @Autowired
    private GameServiceImpl gameService;

    @RequestMapping("/start")
    public WebServiceResponse startGame(HttpSession thisSession) {
        WebServiceResponse welcomeResponse = new WebServiceResponse();

        if (thisSession.isNew()) {
            String chipColor = gameService.registerAndStartGame(thisSession.getId());
            welcomeResponse.getResponseObject().setMessage(newGameMsg);
            welcomeResponse.getResponseObject().setChip("We have selected a " + chipColor + " chip.");

        } else {
            welcomeResponse.getResponseObject().setMessage(oldGameMsg);
        }
        welcomeResponse.getResponseMetaData().success();
        welcomeResponse.getResponseObject().setReference(thisSession.getId());

        return welcomeResponse;
    }

    @RequestMapping("/hello")
    public WebServiceResponse sayHello() {
        WebServiceResponse hello = new WebServiceResponse();
        hello.getResponseMetaData().success();
        return hello;
    }

    @RequestMapping("/place_chip/{boardColumn}")
    public WebServiceResponse placeChip(@PathVariable String boardColumn,
            @RequestParam(required = true) String playerRef, HttpSession thisSession) {
        WebServiceResponse response = new WebServiceResponse();
        if (playerRef.isEmpty()) {
            response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), invalidPlayerRefMsg);
        } else {
            try {
                String[] boardMoves = gameService.placeBoardPiece(playerRef, boardColumn);

                if (boardMoves != null) {
                    response.getResponseObject().setHumanTurn(boardMoves[0]);
                    response.getResponseObject().setAiTurn(boardMoves[1]);
                    response.getResponseObject().setGameStatus(GameStatus.GAME_STARTED);
                    response.getResponseMetaData().success();
                } else {
                    response.getResponseMetaData().failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Oppss, something went wrong, please retry.");
                }
            } catch (GameFinished ge) {
                thisSession.invalidate();
                response.getResponseObject().setGameStatus(ge.getGameStatus());

                if (!ge.getGameStatus().equals(GameStatus.GAME_TIED)) {
                    try {
                        if (!gameService.isWinningPlayer(playerRef)) {
                            response.getResponseObject().setMessage(aiWinMsg);
                        } else {
                            response.getResponseObject().setMessage(playerWinMsg);
                        }
                    } catch (PlayerNotRegisteredException ex) {
                        // this won't occur as the player is aleady been verfied.
                    }
                } else {
                    response.getResponseObject().setMessage(gameTieMsg);
                }

                response.getResponseMetaData().success();

            } catch (IllegalMoveException e) {
                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), MessageFormat.format(invalidMoveMsg, boardColumn));
            } catch (ColumnFilledException e) {
                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), MessageFormat.format(invalidMoveColumnMsg, boardColumn));
            } catch (PlayerNotRegisteredException e) {
                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), invalidPlayerMsg);
            }
        }
        return response;
    }

    @RequestMapping("/stats")
    public WebServiceResponse gameStatus() {
        WebServiceResponse response = new WebServiceResponse();
        String[] gameStats = gameService.getOverallGameStats();
        response.getResponseObject().setActivePlayer(gameStats[0]);
        response.getResponseMetaData().success();
        return response;
    }
}
