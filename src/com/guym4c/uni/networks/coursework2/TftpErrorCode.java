package com.guym4c.uni.networks.coursework2;

public enum TftpErrorCode {

    UnknownError(0),
    NotFound(1),
    AccessViolation(2),
    AllocationExceeded(3),
    IllegalOperation(4),
    UnknownTid(5),
    AlreadyExists(6),
    UserNotFound(7);

    private int value;

    TftpErrorCode(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int toInt() {
        return this.getValue();
    }

    public static TftpErrorCode fromInt(int i) {
        for (TftpErrorCode errorCode : TftpErrorCode.values()) {
            if (errorCode.value == i) {
                return errorCode;
            }
        }
        return TftpErrorCode.UnknownError;
    }

}
