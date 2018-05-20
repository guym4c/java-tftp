package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ReadRequestServlet extends SendThread {


    public ReadRequestServlet(DatagramPacket packet, int tid, String filename) {
        super(packet, tid, filename);
    }

    @Override
    public void run() {

    }

    @Override
    void receive(DatagramPacket packet) {

    }


}
