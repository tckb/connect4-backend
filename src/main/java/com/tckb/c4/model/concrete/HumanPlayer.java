/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.concrete;

import com.tckb.c4.model.intf.GameObject;
import com.tckb.c4.model.intf.Player;
import java.util.Random;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tckb
 */
@Document
public class HumanPlayer extends GameObject implements Player {

    @Id
    private final String reference;

    public HumanPlayer(String reference) {
        this.reference = reference;
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public String placeChipOnBoard(int column) {
        return "row: " + new Random().nextInt(7) + ",column: " + column;
    }

}
