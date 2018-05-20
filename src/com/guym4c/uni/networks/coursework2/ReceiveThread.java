package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;

public abstract class ReceiveThread extends CommThread {

    public ReceiveThread(DatagramPacket packet, int tid) {
        super(packet, tid);
    }

    public ReceiveThread(String address, int port, int tid) {
        super(address, port, tid);
    }

}
