package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;
import java.util.Arrays;

public class RequestPacketBuffer extends GenericPacketBuffer {

    private static final int STRING_DATA_OFFSET = 2;

    private String filename;
    private String mode;

    public RequestPacketBuffer(Opcode opcode, String filename, String mode) {
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
    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addZeroes();
            addInt(getOpcode().toInt());
            addString(filename);
            addZeroes();
            addString(mode);
            addZeroes();
        }};
    }

    public RequestPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        ArrayList<String> data = getZeroDelimitedData(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - STRING_DATA_OFFSET));
        this.filename = data.get(0);
        this.mode = data.get(1);
    }
}
