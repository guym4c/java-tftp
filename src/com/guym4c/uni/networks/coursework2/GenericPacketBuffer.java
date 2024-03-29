package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GenericPacketBuffer {

    private Opcode opcode;

    public GenericPacketBuffer(Opcode opcode) {
        this.opcode = opcode;
    }

    public GenericPacketBuffer(int opcode) {
        this.opcode = Opcode.fromInt(opcode);
    }

    public GenericPacketBuffer(byte[] bytes) {
        this.opcode = Opcode.fromInt(bytes[1]);
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public void setOpcode(Opcode opcode) {
        this.opcode = opcode;
    }

    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
        }};
    }

    protected static ArrayList<String> getNullDelimitedStrings(byte[] bytes) {
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        int base = 0;
        while (i < bytes.length) {
            while (i < bytes.length && bytes[i] != 0) {
                i++;
            }
            results.add(getStringFromBytes(Arrays.copyOfRange(bytes, base, i)));
            base = ++i;
        }
        return results;
    }

    protected static String getStringFromBytes(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8)
                .replace("\n", System.lineSeparator());
    }

    protected static int getIntFromByte(byte b) {
        return new BigInteger(new byte[] {b}).intValue();
    }

    @Override
    public String toString() {
        return "GenericPacketBuffer{\n" +
                "\topcode=" + opcode + "\n" +
                "}\n";
    }
}
