package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;

public abstract class SendThread extends CommThread {

    protected byte[] file;
    protected int filePointer;

    public SendThread(DatagramPacket packet, int tid, String filename) {
        super(packet, tid);
        initialiseSendThread(filename);
    }

    public SendThread(String address, int port, int tid, String filename) {
        super(address, port, tid);
        initialiseSendThread(filename);
    }

    private void initialiseSendThread(String filename) {
        try {
            file = Files.readAllBytes(new File(filename).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePointer = 0;
    }

    protected int getNextBlock() {
        return (getPreviousBlockNumber() + 1) % BLOCK_MAX_VALUE;
    }

}
