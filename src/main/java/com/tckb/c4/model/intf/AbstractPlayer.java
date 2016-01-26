/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import com.tckb.c4.model.concrete.BoardChip;
import java.util.Objects;
import org.springframework.data.annotation.Id;

/**
 * An abstract helper class for implementing common methods for players
 * <p>
 * @author tckb
 */
public abstract class AbstractPlayer implements GameObject, Player {

    @Id
    protected final String reference;
    protected final BoardChip chip;

    public AbstractPlayer(String reference, BoardChip chip) {
        this.reference = reference;
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
        return Objects.equals(this.reference, other.reference);
    }

}
