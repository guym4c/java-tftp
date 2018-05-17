package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ReadRequestServlet extends RequestServlet {


    public ReadRequestServlet(DatagramPacket packet, int tid) throws SocketException {
        super(packet, tid);
    }

    @Override
    public void run() {

    }

    @Override
    void receive(DatagramPacket packet) throws IOException {

    }


}
