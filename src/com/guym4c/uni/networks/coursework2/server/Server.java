package com.guym4c.uni.networks.coursework2.server;

import com.guym4c.uni.networks.coursework2.GenericTftpPacketBuffer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public class Server {

    private static final int MAX_PAYLOAD_SIZE = 512;
    private static final int SERVER_PORT = 9000;
    private static final int TID_FLOOR = 1024;
    private static final int TID_CEILING = 32767;

    private DatagramSocket socket;

    public Server() throws SocketException {
        socket = new DatagramSocket(SERVER_PORT);
    }

    public void run() throws IOException {
        byte[] inBuffer = new byte[MAX_PAYLOAD_SIZE];

        while (true) {
            DatagramPacket packet = new DatagramPacket(inBuffer, MAX_PAYLOAD_SIZE);
            socket.receive(packet);
            byte[] bytes = packet.getData();
            try {
                GenericTftpPacketBuffer genericBuffer = new GenericTftpPacketBuffer(bytes);
                int tid = new Random().nextInt(TID_CEILING) + TID_FLOOR;
                switch (genericBuffer.getOpcode()) {
                    case ReadRequest:
                        new ReadRequestServlet(packet, tid).start();
                        break;
                    case WriteRequest:
                        new WriteRequestServlet(packet, tid).start();
                        break;
                    default:

                        break;
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
