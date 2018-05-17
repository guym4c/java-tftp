package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;

public enum TftpOpcode {

    ReadRequest(1),
    WriteRequest(2),
    Data(3),
    Acknowledgement(4),
    Error(5);

    private int value;

    TftpOpcode(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int toInt() {
        return this.getValue();
    }

    public static TftpOpcode fromInt(int i) {
        for (TftpOpcode opcode : TftpOpcode.values()) {
            if (opcode.value == i) {
                return opcode;
            }
        }
        throw new IllegalArgumentException("No opcode with that index");
    }

    public static TftpOpcode fromByte(byte b) {
        return fromInt(new BigInteger(new byte[] {b}).intValue());
    }

}
