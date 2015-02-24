package com.lutron.sir_wheelium_ii;

/**
 * Created by tbalch on 2/21/2015.
 */
public enum Command {
    AdjustSpeed((byte)0x02),
    AdjustAngle((byte)0x03),
    LaunchBalls((byte)0x04);

    private byte numVal;

    Command(byte numVal) {
        this.numVal = numVal;
    }

    public byte getNumVal() {
        return numVal;
    }
}

