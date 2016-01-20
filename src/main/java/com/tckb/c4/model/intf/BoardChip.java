/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tckb.c4.model.intf;

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
}
