package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GenericTftpPacketBuffer {

    private TftpOpcode opcode;

    public GenericTftpPacketBuffer(TftpOpcode opcode) {
        this.opcode = opcode;
    }

    public GenericTftpPacketBuffer(int opcode) {
        this.opcode = TftpOpcode.fromInt(opcode);
    }

    public GenericTftpPacketBuffer(byte[] bytes) {
        this.opcode = TftpOpcode.fromInt(bytes[1]);
    }

    public TftpOpcode getOpcode() {
        return opcode;
    }

    public void setOpcode(TftpOpcode opcode) {
        this.opcode = opcode;
    }

    public ArrayList<Byte> getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
        }};
    }


    protected static ArrayList<String> getZeroDelimitedData(byte[] bytes) {
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        int base = 0;
        while (i < bytes.length) {
            while (bytes[i] != 0) {
                i++;
            }
            results.add(getStringFromBytes(Arrays.copyOfRange(bytes, base, i)));
            base = i;
        }
        return results;
    }

    protected static String getStringFromBytes(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected static int getIntFromByte(byte b) {
        return new BigInteger(new byte[] {b}).intValue();
    }

}
