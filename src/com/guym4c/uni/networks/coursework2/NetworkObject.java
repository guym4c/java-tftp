package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public abstract class NetworkObject {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final String SERVER_IP = "127.0.0.1";
    protected static final int SERVER_PORT = 9000;
    protected static final int TID_FLOOR = 1024;
    protected static final int TID_CEILING = 32767;

    protected DatagramSocket socket;

    public NetworkObject(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    protected static int generateTid() {
        return new Random().nextInt(TID_CEILING) + TID_FLOOR;
    }
    
    public abstract void run() throws IOException;
    
}
