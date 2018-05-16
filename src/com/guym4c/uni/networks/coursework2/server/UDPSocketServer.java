package com.guym4c.uni.networks.coursework2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocketServer extends Thread {

    private static final int TFTP_DATA_LENGTH = 512;

    private DatagramSocket socket;

    public UDPSocketServer() throws SocketException {
        this("UDPSocketServer");
    }

    public UDPSocketServer(String name) throws SocketException {
        super(name);
        socket = new DatagramSocket(9000);
    }

    @Override
    public void run() {

        int counter = 0;
        byte[] inBuffer = new byte[TFTP_DATA_LENGTH];

        try {
            while (true) {

                DatagramPacket packet = new DatagramPacket(inBuffer, TFTP_DATA_LENGTH);
                socket.receive(packet);

                byte[] bytes = packet.getData();


                InetAddress address = packet.getAddress();
                int sourcePort = packet.getPort();

                packet.setData(outBuffer);
                packet.setAddress(address);
                packet.setPort(sourcePort);

                socket.send(packet);

                counter++;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPSocketServer().start();
        System.out.println("Time Server Started");
    }

}
