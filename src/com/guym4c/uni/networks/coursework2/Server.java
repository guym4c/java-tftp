package com.guym4c.uni.networks.coursework2;

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

        while (true) {
            byte[] inBuffer = new byte[MAX_PAYLOAD_SIZE + 4];
            DatagramPacket packet = new DatagramPacket(inBuffer, MAX_PAYLOAD_SIZE);
            socket.receive(packet);
            byte[] bytes = packet.getData();
            try {
                GenericPacketBuffer genericBuffer = new GenericPacketBuffer(bytes);
                int tid = new Random().nextInt(TID_CEILING) + TID_FLOOR;
                switch (genericBuffer.getOpcode()) {
                    case ReadRequest:
                        new ReadRequestServlet(packet, tid).start();
                        break;
                    case WriteRequest:
                        new WriteRequestServlet(packet, tid).start();
                        break;
                    default:
                        new RequestServlet(packet, tid) {
                            @Override
                            void receive(DatagramPacket packet) throws IOException {
                                error();
                            }

                            @Override
                            public void run() {
                                ErrorPacketBuffer errorBuffer = new ErrorPacketBuffer(ErrorCode.UnknownError, "Unknown error");
                                try {
                                    send(errorBuffer.getByteBuffer());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        break;
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
