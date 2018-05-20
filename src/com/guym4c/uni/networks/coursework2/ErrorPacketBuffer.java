package com.guym4c.uni.networks.coursework2;

import java.util.Arrays;

public class ErrorPacketBuffer extends AbstractPacketBuffer {

    private static final int STRING_DATA_OFFSET = 4;

    private ErrorCode errorCode;
    private String message;

    public ErrorPacketBuffer(ErrorCode errorCode, String message) {
        super(Opcode.Error);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ByteArray getByteBuffer() {
        return new ByteArray() {{
            addInt(getOpcode().toInt());
            addInt(errorCode.toInt());
            addString(message);
            addZeroes();
        }};
    }

    public ErrorPacketBuffer(byte[] bytes) {
        super(getIntFromByte(bytes[1]));
        this.errorCode = ErrorCode.fromInt(getIntFromByte(bytes[3]));
        this.message = getNullDelimitedStrings(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - STRING_DATA_OFFSET))
                .get(0);
    }
}
