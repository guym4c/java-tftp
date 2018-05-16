package com.guym4c.uni.networks.coursework2.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketClient {

    private static final int TFTP_DATA_LENGTH = 512;
    private static final String LOCAL_SERVER_IP = "127.0.0.1";

    public static void main(String[] args) throws IOException {

        DatagramSocket socket;
        DatagramPacket packet;

        byte[] buffer = new byte[TFTP_DATA_LENGTH];
        socket = new DatagramSocket(4000);
        InetAddress address = InetAddress.getByName(LOCAL_SERVER_IP);

        packet = new DatagramPacket(buffer, TFTP_DATA_LENGTH);
        packet.setAddress(address);
        packet.setPort(9000);

        socket.send(packet);

        socket.receive(packet);

        String received = new String(packet.getData());
        System.out.println("Today's date: " + received.substring(0, packet.getLength()));
        socket.close();
    }

}
