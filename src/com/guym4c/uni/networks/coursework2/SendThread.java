package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Arrays;

public abstract class SendThread extends CommThread {

    protected byte[] file;
    protected int filePointer;

    public SendThread(DatagramPacket packet, int tid) {
        super(packet, tid);
    }

    public SendThread(String address, int port, int tid) {
        super(address, port, tid);
    }

    protected boolean initialiseFile(String filename) {
        try {
            file = Files.readAllBytes(new File(filename).toPath());
        } catch (IOException e) {
            error(ErrorCode.NotFound, "File not found");
            return false;
        }
        return true;
    }

    protected int getNextBlock() {
        return (getPreviousBlockNumber() + 1) % BLOCK_MAX_VALUE;
    }

    @Override
    public void run() {
        while (!destroyable) {
            DatagramPacket packet = new DatagramPacket(new byte[maxPacketSize], maxPacketSize);
            try {
                socket.receive(packet);
                receive(packet);
            } catch (SocketTimeoutException timeout) {
                if (!attemptReSend()) {
                    destroyable = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(conclude(success));
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
