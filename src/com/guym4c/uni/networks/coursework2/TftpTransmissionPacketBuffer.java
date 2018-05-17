package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;

public class TftpTransmissionPacketBuffer extends GenericTftpPacketBuffer {

    private int block;

    public TftpTransmissionPacketBuffer(TftpOpcode opcode, int block) {
        super(opcode);
        this.block = block;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public ArrayList<Byte> getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addZeroes();
            addInt(block);
        }};
    }

    public TftpTransmissionPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        this.block = getIntFromByte(bytes[3]);
    }
}
