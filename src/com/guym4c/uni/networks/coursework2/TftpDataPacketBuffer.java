package com.guym4c.uni.networks.coursework2;

import java.util.Arrays;

public class TftpDataPacketBuffer extends TftpTransmissionPacketBuffer {

    private static final int STRING_DATA_OFFSET = 4;

    private String data;

    public TftpDataPacketBuffer(TftpOpcode opcode, int block, String data) {
        super(opcode, block);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TftpDataPacketBuffer(byte[] bytes) {
        super(bytes);
        this.data = getZeroDelimitedData(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - STRING_DATA_OFFSET))
                .get(0);
    }
}
