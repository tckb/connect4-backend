/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws.controller;

import com.tckb.c4.model.exception.GameException.ColumnFilledException;
import com.tckb.c4.model.exception.GameException.GameFinished;
import com.tckb.c4.model.exception.GameException.GameNotSetupException;
import com.tckb.c4.model.exception.GameException.IllegalMoveException;
import com.tckb.c4.model.exception.GameException.InvalidGameSessionException;
import com.tckb.c4.model.exception.GameException.MaxPlayerRegisteredException;
import com.tckb.c4.model.exception.GameException.PlayerNotRegisteredException;
import com.tckb.c4.model.intf.Board.GameStatus;
import com.tckb.c4.ws.BoardConfiguration;
import com.tckb.c4.ws.WebServiceResponse;
import com.tckb.c4.ws.impl.GameServiceImpl;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the game service.
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

    protected static final Logger thisLogger = Logger.getLogger(GameController.class.getName());

    @RequestMapping("/start")
    public WebServiceResponse startGame(@RequestParam(required = false, defaultValue = "false", name = "multi_player") boolean multiPlayer) {
        WebServiceResponse welcomeResponse = new WebServiceResponse();

        try {
            String[] gameRefs = gameService.createNewGameSession(multiPlayer);
            thisLogger.log(Level.INFO, "Start game request with refs: {0}", Arrays.toString(gameRefs));
            welcomeResponse.getResponseObject().setBoardSession(gameRefs[0]);
            welcomeResponse.getResponseObject().setPlayerRef(gameRefs[1]);
            welcomeResponse.getResponseObject().setChipColor(gameRefs[2]);
            welcomeResponse.getResponseObject().setMessage(newGameMsg);
            welcomeResponse.getResponseMetaData().success();

        } catch (GameNotSetupException ex) {
            welcomeResponse.getResponseMetaData().failure(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        }
        return welcomeResponse;
    }

    @RequestMapping("/setup")
    public WebServiceResponse setupGame(@Valid @RequestBody BoardConfiguration setup, BindingResult result) {
        WebServiceResponse resp = new WebServiceResponse();
        if (!result.hasErrors()) {
            gameService.setupBoard(setup);
            resp.getResponseMetaData().success();
        } else {
            resp.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
        return resp;
    }

    @RequestMapping("/join")
    public WebServiceResponse joinGame(@RequestParam(required = true, name = "session") String boardSessionId) {
        WebServiceResponse response = new WebServiceResponse();

        thisLogger.log(Level.INFO, "New join request, ref: {0} with board session id: {1} .", new Object[]{boardSessionId});

        try {
            String[] gameRefs = gameService.registerAndJoinGame(boardSessionId);
            response.getResponseObject().setMessage(newGameMsg);
            response.getResponseObject().setBoardSession(gameRefs[0]);
            response.getResponseObject().setPlayerRef(gameRefs[1]);
            response.getResponseObject().setChipColor(gameRefs[2]);
            response.getResponseMetaData().success();
        } catch (InvalidGameSessionException | MaxPlayerRegisteredException ex) {
            response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }

        return response;
    }

    @RequestMapping("/place_chip/{boardColumn}")
    public WebServiceResponse placeChip(@PathVariable String boardColumn,
            @RequestParam(required = true) String playerRef, @RequestParam(required = true) String boardSessionId) {
        WebServiceResponse response = new WebServiceResponse();
        if (playerRef.isEmpty() || boardSessionId.isEmpty()) {
            response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), invalidPlayerRefMsg);
        } else {
            try {
                String[] boardMoves = gameService.placeBoardPiece(boardSessionId, playerRef, boardColumn);

                if (boardMoves != null && boardMoves.length >= 3) {
                    response.getResponseObject().setYourTurn(boardMoves[0]);
                    response.getResponseObject().setOpponentTurn(boardMoves[1]);
                    response.getResponseObject().setChipColor(boardMoves[2]);
                    response.getResponseObject().setBoardData(boardMoves[3]);
                    response.getResponseObject().setGameStatus(GameStatus.GAME_IN_PROGRESS);
                    response.getResponseMetaData().success();
                } else {
                    response.getResponseMetaData().failure(HttpStatus.FORBIDDEN.value(), "Sorry, I'm still waiting for other players to join.");
                }
            } catch (GameFinished ge) {
                response.getResponseObject().setGameStatus(ge.getGameStatus());
                if (!ge.getGameStatus().equals(GameStatus.GAME_TIED)) {
                    try {
                        if (ge.getWinningMove().equals(playerRef)) {
                            response.getResponseObject().setMessage(aiWinMsg);
                            response.getResponseObject().setBoardData(ge.getBoardData());

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

            } catch (IllegalMoveException | InvalidGameSessionException | PlayerNotRegisteredException e) {
                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            } catch (ColumnFilledException e) {
                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), MessageFormat.format(invalidMoveColumnMsg, boardColumn));
            }
        }
        return response;
    }

    @RequestMapping("/stats")
    public WebServiceResponse gameStatus() {
        WebServiceResponse response = new WebServiceResponse();
        response.getResponseObject().setGameStats(gameService.getOverallGameStats());
        response.getResponseMetaData().success();
        return response;
    }

}
