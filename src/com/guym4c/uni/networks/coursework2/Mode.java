package com.guym4c.uni.networks.coursework2;

public enum Mode {
    NetAscii("netascii"),
    Octet("octet"),
    Mail("mail");

    private String value;

    Mode(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static Mode fromString(String s) {
        for (Mode mode : Mode.values()) {
            if (mode.value.equals(s.toLowerCase())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown mode");
    }

}
