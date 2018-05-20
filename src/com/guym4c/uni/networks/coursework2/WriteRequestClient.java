package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class WriteRequestClient extends SendThread {

    public WriteRequestClient(String address, int port, String filename, int tid) {
        super(address, port, tid, filename);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.WriteRequest, filename, DEFAULT_MODE);

        System.out.println("Sent by " + this.getName());
        System.out.println(requestBuffer);

        send(requestBuffer);
    }

    @Override
    public void run() {
        while (!destroyable) {
            DatagramPacket packet = new DatagramPacket(new byte[maxPacketSize], maxPacketSize);
            try {
                socket.receive(packet);
                receive(packet);
            } catch (SocketTimeoutException timeout) {
                resend();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void receive(DatagramPacket packet) {
        byte[] bytes = packet.getData();
        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(bytes);

        System.out.println("Received by " + this.getName());
        System.out.println(acknowledgementBuffer);

        if (acknowledgementBuffer.getBlock() != getPreviousBlockNumber() || terminated) {
            destroyable = true;
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
            hunk = Arrays.copyOfRange(file, filePointer, file.length - 1);
            terminated = true;
        } else {
            hunk = Arrays.copyOfRange(file, filePointer, filePointer + MAX_PAYLOAD_SIZE);
        }
        filePointer += hunk.length;
        return hunk;
    }
}
