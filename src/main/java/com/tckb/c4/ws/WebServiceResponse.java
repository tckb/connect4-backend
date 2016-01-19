/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.ws;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tckb.c4.model.intf.Board.GameStatus;
import org.springframework.http.HttpStatus;

/**
 * A generic webservice response
 * <p>
 * @author tckb
 */
@JsonInclude(Include.NON_NULL)
public class WebServiceResponse {

    /**
     * the meta data to response
     */
    @JsonProperty(value = "meta_data")
    private ResponseMetaData responseMetaData;

    /**
     * the payload to the response
     */
    @JsonProperty(value = "game_response")
    private ServerResponse responseObject;

    /**
     * create generic webresponse
     */
    public WebServiceResponse() {
        this.responseMetaData = new ResponseMetaData(WebServiceStatuses.SUCCESS);
        this.responseObject = new ServerResponse();
    }

    /**
     * Returns the response meta data
     * <p>
     * @return metadata
     */
    public ResponseMetaData getResponseMetaData() {
        return responseMetaData;
    }

    /**
     * Sets the response meta data
     * <p>
     * @param responseMetaData
     */
    public void setResponseMetaData(ResponseMetaData responseMetaData) {
        this.responseMetaData = responseMetaData;
    }

    /**
     * Return the response payload
     * <p>
     * @return
     */
    public ServerResponse getResponseObject() {
        return responseObject;
    }

    /**
     * Set the response payload
     * <p>
     * @param responseObject
     */
    public void setResponseObject(ServerResponse responseObject) {
        this.responseObject = responseObject;
    }

    /**
     * The POJO representing response meta data
     */
    @JsonInclude(Include.NON_NULL)
    public class ResponseMetaData {

        /**
         * status of the response
         */
        @JsonProperty(value = "status")
        private String status;

        /**
         * Success message
         */
        @JsonProperty(value = "success_message")
        private String successMessage;

        /**
         * Error specific message
         */
        @JsonProperty(value = "error_message")
        private String errorMessage;

        /**
         * HTTP status for the request
         */
        @JsonProperty(value = "http_status")
        private int httpStatus;

        /**
         * Initialize the response meta data
         * <p>
         * @param status
         */
        public ResponseMetaData(WebServiceStatuses status) {
            this(status, null, null);
        }

        /**
         * Initialize response meta data
         * <p>
         * @param status
         * @param successMessage
         * @param errorMessage
         */
        public ResponseMetaData(WebServiceStatuses status, String successMessage, String errorMessage) {
            this.status = status.name();
            this.successMessage = successMessage;
            this.errorMessage = errorMessage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStatus(WebServiceStatuses webServiceStatuses) {
            this.status = webServiceStatuses.name();
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * Set the response to failure with 'httpStatus' and specific
         * 'errorMessage'
         * <p>
         * @param httpStatus
         * @param errorMessage
         */
        public void failure(int httpStatus, String errorMessage) {
            this.httpStatus = httpStatus;
            this.status = WebServiceStatuses.FAILURE.name();
            this.errorMessage = errorMessage;
        }

        public void success(int httpStatus, String successMessage) {
            this.status = WebServiceStatuses.SUCCESS.name();
            this.httpStatus = httpStatus;
            this.successMessage = successMessage;
        }

        /**
         * Set the response to Success with the specific HTTP status
         * <p>
         * @param httpStatus
         */
        public void success(int httpStatus) {
            success(httpStatus, null);
        }

        /**
         * Set the response to success with HTTP Status 'OK' - 200
         */
        public void success() {
            success(HttpStatus.OK.value());
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
        }
    }

    @JsonInclude(Include.NON_NULL)
    public class ServerResponse {

        @JsonProperty(value = "your_move")
        private String humanTurn;
        @JsonProperty(value = "computer_move")
        private String aiTurn;
        @JsonProperty(value = "game_status")
        private GameStatus gameStatus;
        @JsonProperty(value = "help_text")
        private String message;
        @JsonProperty(value = "player_ref")
        private String reference;
        @JsonProperty(value = "active_players")
        private String activePlayers;

        public String getHumanTurn() {
            return humanTurn;
        }

        public void setHumanTurn(String humanTurn) {
            this.humanTurn = humanTurn;
        }

        public String getAiTurn() {
            return aiTurn;
        }

        public void setAiTurn(String aiTurn) {
            this.aiTurn = aiTurn;
        }

        public GameStatus getGameStatus() {
            return gameStatus;
        }

        public void setGameStatus(GameStatus gameStatus) {
            this.gameStatus = gameStatus;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public void setActivePlayer(String activePlayers) {
            this.activePlayers = activePlayers;
        }

        public String getActivePlayers() {
            return activePlayers;
        }

    }

    public static enum WebServiceStatuses {

        SUCCESS,
        FAILURE
    }

}
