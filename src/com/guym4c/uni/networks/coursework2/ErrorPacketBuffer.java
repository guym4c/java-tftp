package com.guym4c.uni.networks.coursework2;

import java.util.Arrays;

public class ErrorPacketBuffer extends GenericPacketBuffer {

    private static final int STRING_DATA_OFFSET = 4;

    private TftpErrorCode errorCode;
    private String message;

    public ErrorPacketBuffer(TftpErrorCode errorCode, String message) {
        super(TftpOpcode.Error);
        this.errorCode = errorCode;
        this.message = message;
    }

    public TftpErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(TftpErrorCode errorCode) {
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
        this.errorCode = TftpErrorCode.fromInt(getIntFromByte(bytes[3]));
        this.message = getZeroDelimitedData(Arrays.copyOfRange(bytes, STRING_DATA_OFFSET, bytes.length - STRING_DATA_OFFSET))
                .get(0);
    }
}