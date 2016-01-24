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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
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

    protected static final Logger thisLogger = Logger.getLogger(GameController.class.getName());

    @RequestMapping("/start")
    public WebServiceResponse startGame(HttpSession thisSession,
            @RequestParam(required = false, defaultValue = "false", name = "multi_player") boolean multiPlayer) {
        WebServiceResponse welcomeResponse = new WebServiceResponse();

        thisLogger.log(Level.INFO, "Start game request with ref: {0}", thisSession.getId());

        boolean isRegistered = gameService.checkIfRegisteredByRef(thisSession.getId());

        try {
            if (!isRegistered) {
                String[] gameRefs = gameService.registerAndStartGame(thisSession.getId(), multiPlayer);

                welcomeResponse.getResponseObject().setMessage(newGameMsg);
                welcomeResponse.getResponseObject().setChipColor(gameRefs[0]);
                welcomeResponse.getResponseObject().setBoardSession(gameRefs[1]);

            } else {

                String boardSession = gameService.getBoardSession(thisSession.getId());

                if (boardSession != null) {
                    welcomeResponse.getResponseObject().setMessage(oldGameMsg);
                    welcomeResponse.getResponseObject().setBoardSession(boardSession);
                } else {
                    // seems like session expired!
                }

            }
            welcomeResponse.getResponseMetaData().success();
            welcomeResponse.getResponseObject().setReference(thisSession.getId());

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
    public WebServiceResponse joinGame(HttpSession thisSession, @RequestParam(required = true, name = "session") String boardSessionId) {
        WebServiceResponse response = new WebServiceResponse();
        boolean alreadyStarted = gameService.checkIfRegisteredByRef(thisSession.getId());
        boolean alreadyJoined = gameService.checkIfRegisteredBySessionId(boardSessionId, thisSession.getId());

        if (alreadyStarted) {
            thisLogger.log(Level.INFO, "Ref: {0} aleady started game.", thisSession.getId());
            response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), "Hey there, you have already started a game. Can not join new game.");
        } else {
            if (alreadyJoined) {
                thisLogger.log(Level.INFO, "Ref: {0} with board session id: {1} already joined game.", new Object[]{thisSession.getId(), boardSessionId});

                response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), "Hey there, you have already joined the game. start placing a move.");
            } else {
                thisLogger.log(Level.INFO, "New join request, ref: {0} with board session id: {1} .", new Object[]{thisSession.getId(), boardSessionId});

                try {
                    String[] gameRefs = gameService.registerAndJoinGame(thisSession.getId(), boardSessionId);
                    response.getResponseObject().setMessage(newGameMsg);
                    response.getResponseObject().setChipColor(gameRefs[0]);
                    response.getResponseObject().setReference(gameRefs[1]);
                    response.getResponseMetaData().success();
                } catch (InvalidGameSessionException | MaxPlayerRegisteredException ex) {
                    response.getResponseMetaData().failure(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
                }
            }
        }

        return response;
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

                if (boardMoves != null && boardMoves.length == 2) {
                    response.getResponseObject().setHumanTurn(boardMoves[0]);
                    response.getResponseObject().setAiTurn(boardMoves[1]);
                    response.getResponseObject().setGameStatus(GameStatus.GAME_IN_PROGRESS);
                    response.getResponseMetaData().success();
                } else {
                    response.getResponseMetaData().failure(HttpStatus.FORBIDDEN.value(), "Sorry, I'm still waiting for other players to join.");
                }
            } catch (GameFinished ge) {
                thisSession.invalidate();
                response.getResponseObject().setGameStatus(ge.getGameStatus());

                if (!ge.getGameStatus().equals(GameStatus.GAME_TIED)) {
                    try {
                        if (ge.getWiningPlayerRef().equals(playerRef)) {
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
