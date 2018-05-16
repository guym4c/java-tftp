package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;

public abstract class GenericTftpPacketBuffer {

    private TftpOpcode opcode;

    public GenericTftpPacketBuffer(TftpOpcode opcode) {
        this.opcode = opcode;
    }

    public TftpOpcode getOpcode() {
        return opcode;
    }

    public void setOpcode(TftpOpcode opcode) {
        this.opcode = opcode;
    }

    public abstract ArrayList<Byte> getByteBuffer();

    static ArrayList<String> getZeroDelimitedData(byte[] bytes) {
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        int base = 0;
        while (i < bytes.length) {
            while (bytes[i] != 0) {
                i++;
            }
            results.add(new String(Arrays.copyOfRange(bytes, base, i), StandardCharsets.UTF_8));
            base = i;
        }

        return results;
    }

    static int getIntFromByte(byte b) {
        return new BigInteger(new byte[] {b}).intValue();
    }

}
