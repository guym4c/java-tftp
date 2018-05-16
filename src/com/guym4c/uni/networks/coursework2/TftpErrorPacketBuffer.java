package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;

public class TftpErrorPacketBuffer extends GenericTftpPacketBuffer {

    private TftpErrorCode errorCode;
    private String message;

    public TftpErrorPacketBuffer(TftpOpcode opcode, TftpErrorCode errorCode, String message) {
        super(opcode);
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
    public ArrayList<Byte> getByteBuffer() {
        return new ByteArray() {{
            addInt(getOpcode().toInt());
            addInt(errorCode.toInt());
            addString(message);
            addZeroes();
        }};
    }
}