package com.guym4c.uni.networks.coursework2.server;

import com.guym4c.uni.networks.coursework2.TftpRequestPacketBuffer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public abstract class RequestServlet extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int TIMEOUT = 200;
    private static final int TID_FLOOR = 1024;
    private static final int TID_CEILING = 32767;


    protected DatagramPacket sent;
    protected TftpRequestPacketBuffer received;
    protected int tid;
    protected DatagramSocket socket;

    public RequestServlet(DatagramPacket packet) throws SocketException {
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4);
        received = new TftpRequestPacketBuffer(packet.getData());
        tid = new Random().nextInt(TID_CEILING) + TID_FLOOR;
        socket = new DatagramSocket(tid);
    }

    abstract void processReceived(DatagramPacket packet) throws IOException;

}
