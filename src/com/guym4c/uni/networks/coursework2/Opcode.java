package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;

public enum Opcode {

    ReadRequest(1),
    WriteRequest(2),
    Data(3),
    Acknowledgement(4),
    Error(5);

    private int value;

    Opcode(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int toInt() {
        return this.getValue();
    }

    public static Opcode fromInt(int i) {
        for (Opcode opcode : Opcode.values()) {
            if (opcode.value == i) {
                return opcode;
            }
        }
        throw new IllegalArgumentException("No opcode with that index");
    }

    /**
     * @param b An integer stored as a byte.
     * @return The opcode corresponding to integer $b.
     */
    public static Opcode fromByte(byte b) {
        return fromInt(new BigInteger(new byte[] {b}).intValue());
    }

}
