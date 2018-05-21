package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ReadRequestServlet extends SendThread {


    public ReadRequestServlet(DatagramPacket packet, int tid, String filename) {
        super(packet, tid);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(packet.getData());

        System.out.println("Received by " + this.getName());
        System.out.println(requestBuffer);

        if (initialiseFile(filename)) {
            TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(0);
            send(acknowledgementBuffer);
        } else {
            destroyable = true;
            success = false;
        }


    }


    @Override
    void receive(DatagramPacket packet) {

    }


}
