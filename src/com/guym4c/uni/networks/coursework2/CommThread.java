package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.*;

public abstract class CommThread extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int MAX_META_SIZE = 4;
    protected static final int TIMEOUT = 20 * 1000; //ms
    protected static final int BLOCK_MAX_VALUE = 127;
    protected static final Mode DEFAULT_MODE = Mode.Octet;
    protected static final String THREAD_NAME_PREFIX = "CommThread";
    protected int maxPacketSize;

    protected GenericPacketBuffer sent;
    protected GenericPacketBuffer received;
    protected DatagramSocket socket;
    protected InetAddress sendAddress;
    protected int sendPort;
    protected boolean terminated;
    protected boolean destroyable;

    public CommThread(DatagramPacket packet, int tid) {
        super(THREAD_NAME_PREFIX + tid);
        sendAddress = packet.getAddress();
        sendPort = packet.getPort();
        initialiseCommThread(tid);
    }

    public CommThread(String address, int port, int tid) {
        super(THREAD_NAME_PREFIX + tid);
        try {
            sendAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            // do nothing
        }
        sendPort = port;
        initialiseCommThread(tid);
    }

    private void initialiseCommThread(int tid) {
        System.out.println("Starting " + this.getClass().getSimpleName() + " on " + tid + "\n");
        try {
            socket = new DatagramSocket(tid);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        maxPacketSize = MAX_PAYLOAD_SIZE + MAX_META_SIZE;
        terminated = false;
        destroyable = false;
    }

    protected void error(ErrorCode errorCode, String message) {
        ErrorPacketBuffer errorPacketBuffer = new ErrorPacketBuffer(errorCode, message);
        send(errorPacketBuffer);
    }

    protected void error() {
        error(ErrorCode.UnknownError, "Unknown Error");
    }

    protected void send(GenericPacketBuffer buffer) {
        byte[] bytes = buffer.getByteBuffer().toPrimitive();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        packet.setAddress(sendAddress);
        packet.setPort(sendPort);
        try {
            socket.setSoTimeout(TIMEOUT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sent = buffer;
        System.out.println("Sent by " + this.getName());
        System.out.println(buffer);
    }

    protected void resend() {
        System.out.println("Resend:");
        send(sent);
    }

    abstract void receive(DatagramPacket packet);

}
