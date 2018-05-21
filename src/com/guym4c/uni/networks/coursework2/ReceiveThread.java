package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;

public abstract class ReceiveThread extends CommThread {

    public ReceiveThread(DatagramPacket packet, int tid) {
        super(packet, tid);
    }

    public ReceiveThread(String address, int port, int tid) {
        super(address, port, tid);
    }

    protected boolean isChronologicalBlock(int block) {
        int previousBlock = 0;
        if (received instanceof DataPacketBuffer) {
            DataPacketBuffer previousData = (DataPacketBuffer) received;
            previousBlock = previousData.getBlock();
        }
        if (previousBlock == BLOCK_MAX_VALUE && block == 0) {
            return true;
        } else return previousBlock + 1 == block;
    }

}
