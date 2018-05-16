package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class TftpRequestPacketBuffer extends GenericTftpPacketBuffer {

    private String filename;
    private String mode;

    public TftpRequestPacketBuffer(TftpOpcode opcode, String filename, String mode) {
        super(opcode);
        this.filename = filename;
        this.mode = mode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public ArrayList<Byte> getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addString(filename);
            addZeroes();
            addString(mode);
            addZeroes();
        }};
    }

    public TftpRequestPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        ArrayList<String> data = getZeroDelimitedData(Arrays.copyOfRange(bytes, 2, bytes.length - 2));
        this.filename = data.get(0);
        this.mode = data.get(1);
    }
}
