/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import java.util.Objects;
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

    public BoardChip getChip() {
        return chip;
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

    @Override
    public int compareTo(Player anotherPlayer) {
        return this.reference.compareTo(anotherPlayer.getReference());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.reference);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractPlayer other = (AbstractPlayer) obj;
        if (!Objects.equals(this.reference, other.reference)) {
            return false;
        }
        return true;
    }

}
