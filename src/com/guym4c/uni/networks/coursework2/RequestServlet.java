package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;
import java.net.SocketException;

public abstract class RequestServlet extends CommThread {

    public RequestServlet(DatagramPacket packet, int tid) throws SocketException {
        super(packet, tid, "Servlet");
    }

}
