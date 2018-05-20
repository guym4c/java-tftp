package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ReadRequestClient extends ReceiveThread {

    public ReadRequestClient(String address, int port, String filename, int tid) throws SocketException {
        super(address, port, tid);
    }

    @Override
    public void run() {

    }

    @Override
    void receive(DatagramPacket packet) {

    }
}
