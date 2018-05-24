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

    /**
     * @param packet The packet that, upon send or receipt, creates/ed this thread
     * @param tid The TID of this thread
     */
    public CommThread(DatagramPacket packet, int tid) {
        super(THREAD_NAME_PREFIX + tid);
        sendAddress = packet.getAddress();
        sendPort = packet.getPort();
        initialiseCommThread(tid);
    }

    /**
     * @param address The remote address to send to.
     * @param port The remote port.
     * @param tid The TID of this thread
     */
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

    /**
     * Initialises fields common to the two constructors.
     */
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

    /**
     * Send an error packet.
     * @param errorCode The relevant error code.
     * @param message Any String error message.
     */
    protected void error(ErrorCode errorCode, String message) {
        ErrorPacketBuffer errorPacketBuffer = new ErrorPacketBuffer(errorCode, message);
        send(errorPacketBuffer);
    }

    /**
     * Sends an UnknownError.
     */
    protected void error() {
        error(ErrorCode.UnknownError, "Unknown Error");
    }

    /**
     * @param buffer The buffer class to be sent.
     */
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

    /**
     * Attempt to re-send the last send packet.
     * @return True if re-send was successful, false if the packet cannot be re-sent (e.g. if the maximum amount of retries has been reached)
     */
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

    /**
     * @return The block number of the previously sent packet (rules from getBlockNumber() apply)
     */
    protected int getPreviousBlockNumber() {
        return getBlockNumber(sent);
    }

    /**
     * @param buffer The packet buffer to extract the block number from.
     * @return $buffer's block number. If a request, this will be 0. If an error, this will be -1.
     */
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

    /**
     * @param success True if the operation completed successfully, false otherwise
     * @return The concluding message as stored in SUCCESS_MESSAGE or FAIL_MESSAGE.
     */
    protected static String conclude(boolean success) {
        return success ? SUCCESS_MESSAGE : FAIL_MESSAGE;
    }

    /**
     * @param packet A DataGram packet object
     * @return The raw byte data of the packet, excluding trailing nulls
     */
    protected byte[] getBytesFromPacket(DatagramPacket packet) {
        return Arrays.copyOfRange(packet.getData(), 0, packet.getLength() + 1);
    }

    abstract void receive(DatagramPacket packet);

}
