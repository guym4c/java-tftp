package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class RequestClient extends CommThread {



    public RequestClient(String address, int port, int tid) throws SocketException {
        super(address, port, tid, "Client");
    }
}
