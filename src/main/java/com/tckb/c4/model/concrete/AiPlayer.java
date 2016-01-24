/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.AbstractPlayer;
import com.tckb.c4.model.intf.BoardChip;
import java.util.Random;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tckb
 */
@Document
public class AiPlayer extends AbstractPlayer {

    public AiPlayer(String reference, BoardChip chip) {
        super(reference, chip);
    }

    private int getNextStrategicMove() {
        return new Random().nextInt(7);
    }

    @Override
    public int autoPlaceChipColumn() {
        return getNextStrategicMove();
    }

}
