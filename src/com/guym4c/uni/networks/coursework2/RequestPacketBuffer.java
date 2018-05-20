package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;
import java.util.Arrays;

public class RequestPacketBuffer extends AbstractPacketBuffer {

    private static final int STRING_DATA_OFFSET = 2;

    private String filename;
    private Mode mode;

    public RequestPacketBuffer(Opcode opcode, String filename, Mode mode) {
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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addString(filename);
            addZeroes();
            addString(mode.toString());
            addZeroes();
        }};
    }

    public RequestPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        ArrayList<String> data = getNullDelimitedStrings(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - STRING_DATA_OFFSET));
        this.filename = data.get(0);
        this.mode = Mode.fromString(data.get(1));
    }
}
