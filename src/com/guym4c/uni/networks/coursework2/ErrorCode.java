package com.guym4c.uni.networks.coursework2;

public enum ErrorCode {

    UnknownError(0),
    NotFound(1),
    AccessViolation(2),
    AllocationExceeded(3),
    IllegalOperation(4),
    UnknownTid(5),
    AlreadyExists(6),
    UserNotFound(7);

    private int value;

    ErrorCode(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int toInt() {
        return this.getValue();
    }

    public static ErrorCode fromInt(int i) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.value == i) {
                return errorCode;
            }
        }
        return ErrorCode.UnknownError;
    }

}
