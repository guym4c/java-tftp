package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.*;

public abstract class CommThread extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int TIMEOUT = 20 * 1000; //ms

    protected DatagramPacket sent;
    protected GenericPacketBuffer received;
    protected DatagramSocket socket;
    protected InetAddress sendAddress;
    protected int sendPort;

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
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4);
        socket = new DatagramSocket(tid);
        try {
            sendAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {

        }
        sendPort = port;
    }

    private void initialiseCommThread(DatagramPacket packet, int tid) throws SocketException {
        sent = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4);
        received = new RequestPacketBuffer(packet.getData());
        socket = new DatagramSocket(tid);
        sendAddress = packet.getAddress();
        sendPort = packet.getPort();
    }

    protected void error(ErrorCode errorCode, String message) throws IOException {
        ErrorPacketBuffer errorPacketBuffer = new ErrorPacketBuffer(errorCode, message);
        send(errorPacketBuffer.getByteBuffer());
    }

    protected void error() throws IOException {
        error(ErrorCode.UnknownError, "Unknown Error");
    }

    protected void send(ByteArray toSend) throws IOException {
        byte[] bytes = toSend.toPrimitive();
        DatagramPacket send = new DatagramPacket(bytes, bytes.length);
        send(send);
    }

    protected void send(DatagramPacket packet) throws IOException {
        packet.setAddress(sendAddress);
        packet.setPort(sendPort);
        socket.setSoTimeout(TIMEOUT);
        socket.send(packet);
        sent = packet;
    }

    protected void resend() throws IOException {
        send(sent);
    }

    abstract void receive(DatagramPacket packet) throws IOException;

}
