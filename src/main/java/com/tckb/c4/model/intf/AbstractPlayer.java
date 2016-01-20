/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import org.springframework.data.annotation.Id;

/**
 *
 * @author tckb
 */
public abstract class AbstractPlayer extends GameObject implements Player {

    @Id
    private final String reference;
    private BoardChip chip;

    public AbstractPlayer(String reference) {
        this.reference = reference;
    }

    public void setChip(BoardChip chip) {
        this.chip = chip;
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public abstract String placeChipOnBoard(int column);

    @Override
    public BoardChip getPlayerChip() {
        return this.chip;
    }

}
