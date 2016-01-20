/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.AbstractPlayer;
import java.util.Random;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tckb
 */
@Document
public class AiPlayer extends AbstractPlayer {

    public AiPlayer(String reference) {
        super(reference);
    }

    @Override
    public String placeChipOnBoard(int column) {
        return "row: " + new Random().nextInt(7) + ",column: " + getNextStrategicMove();
    }

    private int getNextStrategicMove() {
        return new Random().nextInt(7);
    }

}
