package com.guym4c.uni.networks.coursework2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ByteArray extends ArrayList<Byte> {

    public void addInt(int i) {
        byte[] bytes = BigInteger.valueOf(i).toByteArray();
        this.add(bytes[bytes.length - 1]);
    }

    public void addString(String s) {
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            this.add(b);
        }
    }

    /**
     * Adds a zero byte to the list.
     */
    public void addZeroes() {
        this.add((byte) 0);
    }

    public void addBytes(byte[] bytes) {
        for (byte b : bytes) {
            this.add(b);
        }
    }

    /**
     * @return This ArrayList as byte[]
     */
    public byte[] toPrimitive() {
        byte[] bytes = new byte[this.size()];
        int i = 0;
        for (byte b : this) {
            bytes[i++] = b;
        }
        return bytes;
    }
}
