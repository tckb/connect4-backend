/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

import java.util.Objects;

/**
 *
 * @author tckb
 */
public abstract class BoardChip extends GameObject {

    public abstract String getColor();

    public static String[] CHIP_COLORS = {
        "BLUE",
        "BLACK",
        "GREEN",
        "RED",
        "WHITE",
        "YELLOW"
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoardChip) {
            return getColor().equals(((BoardChip) obj).getColor());

        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3 * Objects.hashCode(getColor());
        return hash;
    }

}
