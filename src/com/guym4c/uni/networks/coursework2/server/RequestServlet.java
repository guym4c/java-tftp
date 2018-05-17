package com.guym4c.uni.networks.coursework2.server;

import java.net.DatagramPacket;

public abstract class RequestServlet extends Thread {

    private static final int MAX_PAYLOAD_SIZE = 512;

    private DatagramPacket previous;

    public RequestServlet() {
        previous = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE], MAX_PAYLOAD_SIZE);
    }

}
