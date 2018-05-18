package com.guym4c.uni.networks.coursework2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Server extends NetworkObject {

    public Server() throws SocketException {
        super(SERVER_PORT);
    }

    @Override
    public void run() throws IOException {

        while (true) {
            byte[] inBuffer = new byte[MAX_PAYLOAD_SIZE + 4];
            DatagramPacket packet = new DatagramPacket(inBuffer, MAX_PAYLOAD_SIZE);
            socket.receive(packet);
            byte[] bytes = packet.getData();
            try {
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
                        new RequestServlet(packet, tid) {
                            @Override
                            void receive(DatagramPacket packet) throws IOException {
                                error();
                            }

                            @Override
                            public void run() {
                                ErrorPacketBuffer errorBuffer = new ErrorPacketBuffer(ErrorCode.UnknownError, "Unknown error");
                                send(errorBuffer.getByteBuffer());
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
