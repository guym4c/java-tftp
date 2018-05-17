package com.guym4c.uni.networks.coursework2;

import java.util.Arrays;

public class DataPacketBuffer extends TransmissionPacketBuffer {

    private static final int STRING_DATA_OFFSET = 4;
    private static final int MAX_PAYLOAD_SIZE = 512;

    private String data;
    private boolean terminating;

    public DataPacketBuffer(TftpOpcode opcode, int block, String data, boolean terminating) {
        super(opcode, block);
        this.data = data;
        this.terminating = terminating;
    }

    public DataPacketBuffer(int block, String data, boolean terminating) {
        super(TftpOpcode.Data, block);
        this.data = data;
        this.terminating = terminating;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isTerminating() {
        return terminating;
    }

    public void setTerminating(boolean terminating) {
        this.terminating = terminating;
    }

    @Override
    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addZeroes();
            addInt(getBlock());
            addString(data);
        }};
    }

    public DataPacketBuffer(byte[] bytes) {
        super(bytes);
        this.data = getStringFromBytes(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - 1));
        this.terminating = bytes.length - STRING_DATA_OFFSET - 1 == MAX_PAYLOAD_SIZE;

    }
}