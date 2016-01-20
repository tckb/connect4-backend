/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.AbstractPlayer;
import java.util.Random;

/**
 *
 * @author tckb
 */
public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(String reference) {
        super(reference);
    }

    @Override
    public String placeChipOnBoard(int column) {

        return "row: " + new Random().nextInt(7) + ",column: " + column;
    }

}
