package com.guym4c.uni.networks.coursework2.server;

import com.guym4c.uni.networks.coursework2.TftpRequestPacketBuffer;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public abstract class RequestServlet extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    private static final int TID_FLOOR = 1024;
    private static final int TID_CEILING = 32767;

    private DatagramPacket sent;
    private TftpRequestPacketBuffer received;
    private int tid;
    private DatagramSocket socket;

    public RequestServlet(byte[] bytes) throws SocketException {
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE], MAX_PAYLOAD_SIZE);
        received = new TftpRequestPacketBuffer(bytes);
        tid = new Random().nextInt(TID_CEILING) + TID_FLOOR;
        socket = new DatagramSocket(tid);
    }

    protected DatagramPacket getSent() {
        return sent;
    }

    protected void setSent(DatagramPacket sent) {
        this.sent = sent;
    }

    protected TftpRequestPacketBuffer getReceived() {
        return received;
    }

    protected void setReceived(TftpRequestPacketBuffer received) {
        this.received = received;
    }

    protected int getTid() {
        return tid;
    }

    protected void setTid(int tid) {
        this.tid = tid;
    }
}
