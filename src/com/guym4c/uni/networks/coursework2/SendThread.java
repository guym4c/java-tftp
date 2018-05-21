package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;

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

}
