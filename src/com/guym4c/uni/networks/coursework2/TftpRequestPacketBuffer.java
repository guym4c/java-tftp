package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;

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
}
