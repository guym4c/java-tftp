package com.guym4c.uni.networks.coursework2.server;

import com.guym4c.uni.networks.coursework2.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public abstract class RequestServlet extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int TIMEOUT = 200;

    protected DatagramPacket sent;
    protected GenericTftpPacketBuffer received;
    protected int tid;
    protected DatagramSocket socket;
    protected InetAddress address;
    protected int port;

    public RequestServlet(DatagramPacket packet, int tid) throws SocketException {
        super("Servlet" + tid);
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4);
        received = new TftpRequestPacketBuffer(packet.getData());
        socket = new DatagramSocket(tid);
        address = packet.getAddress();
        port = packet.getPort();
    }

    protected void error(DatagramPacket received, TftpErrorCode errorCode, String message) throws IOException {
        TftpErrorPacketBuffer errorPacketBuffer = new TftpErrorPacketBuffer(errorCode, message);
        send(errorPacketBuffer.getByteBuffer());
    }

    protected void error(DatagramPacket received) throws IOException {
        error(received, TftpErrorCode.UnknownError, "Unknown Error");
    }

    protected void send(ByteArray toSend) throws IOException {
        byte[] bytes = toSend.toPrimitive();
        DatagramPacket send = new DatagramPacket(bytes, bytes.length);
        send(send);
    }

    protected void send(DatagramPacket packet) throws IOException {
        packet.setAddress(address);
        packet.setPort(port);
        socket.setSoTimeout(TIMEOUT);
        socket.send(packet);
        sent = packet;
    }

    abstract void receive(DatagramPacket packet) throws IOException;

}
