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



}
