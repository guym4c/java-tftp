package com.guym4c.uni.networks.coursework2.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketClient {

    // the client will take the IP Address of the server (in dotted decimal format as an argument)
    // given that for this tutorial both the client and the server will run on the same machine, you can use the loopback address 127.0.0.1
    public static void main(String[] args) throws IOException {

        DatagramSocket socket;
        DatagramPacket packet;

        if (args.length != 1) {
            System.out.println("the hostname of the server is required");
            return;
        }

        int len = 256;
        byte[] buffer = new byte[len];

        socket = new DatagramSocket(4000);

        InetAddress address = InetAddress.getByName(args[0]);

        packet = new DatagramPacket(buffer, len);
        packet.setAddress(address);
        packet.setPort(9000);

        socket.send(packet);

        socket.receive(packet);

        // display response
        String received = new String(packet.getData());
        System.out.println("Today's date: " + received.substring(0, packet.getLength()));
        socket.close();
    }

}
