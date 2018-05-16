package com.guym4c.uni.networks.coursework2;

import java.util.ArrayList;

public class ByteArray extends ArrayList<Byte> {

    public void addInt(int i) {
        this.add((byte) i);
    }

    public void addString(String s) {
        for (byte b : s.getBytes()) {
            this.add(b);
        }
    }

    public void addZeroes() {
        this.addInt(0);
    }
}
