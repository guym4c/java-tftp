package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.*;

public abstract class CommThread extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int MAX_META_SIZE = 4;
    protected static final int TIMEOUT = 20 * 1000; //ms
    protected static final int BLOCK_MAX_VALUE = 127;
    protected int maxPacketSize;

    protected DatagramPacket sent;
    protected GenericPacketBuffer received;
    protected DatagramSocket socket;
    protected InetAddress sendAddress;
    protected int sendPort;
    protected int recentBlock;

    public CommThread(DatagramPacket packet, int tid) throws SocketException {
        super("CommThread" + tid);
        initialiseCommThread(packet, tid);
    }

    public CommThread(DatagramPacket packet, int tid, String name) throws SocketException {
        super(name + tid);
        initialiseCommThread(packet, tid);
    }

    public CommThread(String address, int port, int tid, String name) throws SocketException {
        super(name + tid);
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE], MAX_PAYLOAD_SIZE + 4);
        socket = new DatagramSocket(tid);
        try {
            sendAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            // do nothing
        }
        sendPort = port;
    }

    private void initialiseCommThread(DatagramPacket packet, int tid) throws SocketException {
        maxPacketSize = MAX_PAYLOAD_SIZE + MAX_META_SIZE;
        sent = new DatagramPacket(new byte[maxPacketSize], maxPacketSize);
        received = new RequestPacketBuffer(packet.getData());
        socket = new DatagramSocket(tid);
        sendAddress = packet.getAddress();
        sendPort = packet.getPort();
        recentBlock = 0;
    }

    protected void error(ErrorCode errorCode, String message) throws IOException {
        ErrorPacketBuffer errorPacketBuffer = new ErrorPacketBuffer(errorCode, message);
        send(errorPacketBuffer.getByteBuffer());
    }

    protected void error() {
        try {
            error(ErrorCode.UnknownError, "Unknown Error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void send(ByteArray toSend) {
        byte[] bytes = toSend.toPrimitive();
        DatagramPacket send = new DatagramPacket(bytes, bytes.length);
        send(send);
    }

    protected void send(DatagramPacket packet) {
        packet.setAddress(sendAddress);
        packet.setPort(sendPort);
        try {
            socket.setSoTimeout(TIMEOUT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sent = packet;
    }

    protected void resend() {
        send(sent);
    }

    protected int incrementBlock() {
        recentBlock = ++recentBlock % BLOCK_MAX_VALUE;
        return recentBlock;
    }

    abstract void receive(DatagramPacket packet);

}
