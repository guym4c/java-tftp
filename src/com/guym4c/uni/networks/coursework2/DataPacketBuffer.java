package com.guym4c.uni.networks.coursework2;

import java.util.Arrays;

public class DataPacketBuffer extends TransmissionPacketBuffer {

    private static final int PAYLOAD_DATA_OFFSET = 4;
    private static final int MAX_PAYLOAD_SIZE = 512;

    private byte[] data;
    private boolean terminating;

    public DataPacketBuffer(Opcode opcode, int block, byte[] data) {
        super(opcode, block);
        this.data = data;
        this.terminating = data.length < MAX_PAYLOAD_SIZE;
    }

    public DataPacketBuffer(int block, byte[] data) {
        super(Opcode.Data, block);
        this.data = data;
        this.terminating = data.length < MAX_PAYLOAD_SIZE;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
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
            addBytes(data);
        }};
    }

    public DataPacketBuffer(byte[] bytes) {
        super(bytes);
        this.data = getPayloadBytes(Arrays.copyOfRange(bytes, PAYLOAD_DATA_OFFSET, bytes.length));

        this.terminating = bytes.length - 1 < MAX_PAYLOAD_SIZE + 4;
    }

    @Override
    public String toString() {
        return "DataPacketBuffer{" +
                "\topcode=" + getOpcode().toString() + "\n" +
                "\tblock=" + getBlock() + "\n" +
                "\tdata=\'" + getStringFromBytes(data) + "\'\n" +
                "\tterminating=" + terminating + "\n" +
                "}\n";
    }
}
