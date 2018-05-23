package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;

public class ReadRequestServlet extends SendThread {

    public ReadRequestServlet(DatagramPacket packet, int tid) {
        super(packet, tid);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(getBytesFromPacket(packet));

        System.out.println("Received by " + this.getName());
        System.out.println(requestBuffer);

        if (!initialiseFile(requestBuffer.getFilename())) {
            destroyable = true;
            success = false;
        }

        DataPacketBuffer dataBuffer = new DataPacketBuffer(1, getNextFileHunk());
        send(dataBuffer);
    }
}
