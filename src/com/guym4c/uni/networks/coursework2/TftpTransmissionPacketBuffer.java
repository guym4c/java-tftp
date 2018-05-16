package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;
import java.util.Arrays;

public class TftpTransmissionPacketBuffer extends GenericTftpPacketBuffer {

    private int block;
    private String data;

    public TftpTransmissionPacketBuffer(TftpOpcode opcode) {
        super(opcode);
        block = 0;
        data = "";
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public ArrayList<Byte> getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addZeroes();
            addInt(block);
            if (data.length() > 0) addString(data);
        }};
    }
}
