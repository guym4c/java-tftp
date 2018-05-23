package com.guym4c.uni.networks.coursework2;

import java.net.DatagramPacket;

public class WriteRequestServlet extends ReceiveThread {

    public WriteRequestServlet(DatagramPacket packet, int tid) {
        super(packet, tid);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(getBytesFromPacket(packet));

        System.out.println("Received by " + this.getName());
        System.out.println(requestBuffer);

        if (initialiseFile(requestBuffer.getFilename())) {
            TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(0);
            send(acknowledgementBuffer);
        } else {
            destroyable = true;
            success = false;
        }
    }
}
