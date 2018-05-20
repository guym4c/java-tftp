package com.guym4c.uni.networks.coursework2;

public class TransmissionPacketBuffer extends AbstractPacketBuffer {

    private int block;

    protected TransmissionPacketBuffer(Opcode opcode, int block) {
        super(opcode);
        this.block = block;
    }

    public TransmissionPacketBuffer(int block) {
        super(Opcode.Acknowledgement);
        this.block = block;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addZeroes();
            addInt(block);
        }};
    }

    public TransmissionPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        this.block = getIntFromByte(bytes[3]);
    }
}
