package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;

public abstract class CommThread extends Thread {

    protected static final int MAX_PAYLOAD_SIZE = 512;
    protected static final int MAX_META_SIZE = 4;
    protected static final int TIMEOUT = 5 * 1000; //ms
    protected static final int BLOCK_MAX_VALUE = 127;
    protected static final Mode DEFAULT_MODE = Mode.Octet;
    protected static final String THREAD_NAME_PREFIX = "CommThread";
    protected static final String SUCCESS_MESSAGE = "Completed successfully";
    protected static final String FAIL_MESSAGE = "Task failed";
    protected int maxPacketSize;
    private static final int MAX_RETRIES = 4;

    protected GenericPacketBuffer sent;
    protected GenericPacketBuffer received;
    protected DatagramSocket socket;
    protected InetAddress sendAddress;
    protected int sendPort;
    protected boolean terminated;
    protected boolean destroyable;
    protected boolean success;

    private HashMap<Integer, Integer> retries;

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
        success = false;
        retries = new HashMap<>();
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

    protected boolean attemptReSend() {
        int previousBlock = getPreviousBlockNumber();
        retries.putIfAbsent(previousBlock, 0);
        retries.put(previousBlock, retries.get(previousBlock) + 1);
        if (retries.get(previousBlock) > MAX_RETRIES) {
            return false;
        } else {
            System.out.println("Resend:");
            send(sent);
            return true;
        }

    }

    protected int getPreviousBlockNumber() {
        return getBlockNumber(sent);
    }

    protected static int getBlockNumber(GenericPacketBuffer buffer) {
        switch (buffer.getOpcode()) {
            case ReadRequest:
            case WriteRequest:
                return 0;
            case Data:
            case Acknowledgement:
                TransmissionPacketBuffer transmissionBuffer = (TransmissionPacketBuffer) buffer;
                return transmissionBuffer.getBlock();
            default:
                return -1;
        }
    }

    protected static String conclude(boolean success) {
        return success ? SUCCESS_MESSAGE : FAIL_MESSAGE;
    }

    protected byte[] getBytesFromPacket(DatagramPacket packet) {
        return Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
    }

    abstract void receive(DatagramPacket packet);

}
