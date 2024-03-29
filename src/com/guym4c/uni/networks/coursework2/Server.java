package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Server extends NetworkObject {

    public Server() {
        super(SERVER_PORT);
    }

    @Override
    public void run() {
        boolean terminate = false;
        while (!terminate) {
            byte[] inBuffer = new byte[MAX_PAYLOAD_SIZE + 4];
            DatagramPacket packet = new DatagramPacket(inBuffer, MAX_PAYLOAD_SIZE);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                terminate = true;
            }
            byte[] bytes = packet.getData();
            GenericPacketBuffer genericBuffer = new GenericPacketBuffer(bytes);
            int tid = generateTid();
            switch (genericBuffer.getOpcode()) {
                case ReadRequest:
                    new ReadRequestServlet(packet, tid).start();
                    break;
                case WriteRequest:
                    new WriteRequestServlet(packet, tid).start();
                    break;
                default:
                    new CommThread(packet, tid) {
                        @Override
                        void receive(DatagramPacket packet) {
                            error();
                        }

                        @Override
                        public void run() {
                            ErrorPacketBuffer errorBuffer = new ErrorPacketBuffer(ErrorCode.UnknownError, "Unknown error");
                            send(errorBuffer);
                        }
                    }.start();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
