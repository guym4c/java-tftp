package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ReadRequestClient extends RequestClient {

    public ReadRequestClient(String filename, int tid) throws SocketException {
        super(tid);
    }

    @Override
    public void run() {

    }

    @Override
    void receive(DatagramPacket packet) throws IOException {

    }
}
