package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class WriteRequestClient extends SendThread {

    public WriteRequestClient(String address, int port, String filename, int tid) {
        super(address, port, tid);
        initialiseFile(filename);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.WriteRequest, filename, DEFAULT_MODE);
        send(requestBuffer);
    }

    @Override
    void receive(DatagramPacket packet) {

        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(getBytesFromPacket(packet));

        System.out.println("Received by " + this.getName());
        System.out.println(acknowledgementBuffer);

        if (acknowledgementBuffer.getBlock() != getPreviousBlockNumber() || terminated) {
            destroyable = true;
            success = true;
        } else {
            if (acknowledgementBuffer.getBlock() == 0) {
                sendPort = packet.getPort();
            }
            DataPacketBuffer dataBuffer = new DataPacketBuffer(getNextBlock(), getNextFileHunk());
            send(dataBuffer);
            received = acknowledgementBuffer;
        }
    }

    private byte[] getNextFileHunk() {
        byte[] hunk;
        if (file.length - filePointer < MAX_PAYLOAD_SIZE) {
            hunk = Arrays.copyOfRange(file, filePointer, file.length);
            terminated = true;
        } else {
            hunk = Arrays.copyOfRange(file, filePointer, filePointer + MAX_PAYLOAD_SIZE);
        }
        filePointer += hunk.length;
        return hunk;
    }
}
