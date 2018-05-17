package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;
import java.net.SocketException;

public abstract class RequestClient extends CommThread {

    public RequestClient(int tid) throws SocketException {
        super(new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4),
                tid, "Client");
    }
}
