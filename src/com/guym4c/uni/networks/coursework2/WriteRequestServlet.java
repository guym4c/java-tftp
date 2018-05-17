package com.guym4c.uni.networks.coursework2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

public class WriteRequestServlet extends RequestServlet {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private boolean terminated;
    private boolean destroyable;

    public WriteRequestServlet(DatagramPacket packet, int tid) throws IOException {
        super(packet, tid);
        terminated = false;
        destroyable = false;

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(packet.getData());

        fileWriter = new FileWriter(requestBuffer.getFilename());
        bufferedWriter = new BufferedWriter(fileWriter);

        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(0);

        send(acknowledgementBuffer.getByteBuffer());
    }

    @Override
    public void run() {
        while (!destroyable) {
            DatagramPacket packet = new DatagramPacket(new byte[MAX_PAYLOAD_SIZE + 4], MAX_PAYLOAD_SIZE + 4);
            try {
                socket.receive(packet);
                receive(packet);
            } catch (SocketTimeoutException timeout) {
                if (terminated) {
                    socket.close();
                    try {
                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    destroyable = true;
                } else {
                    try {
                        send(sent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void receive(DatagramPacket packet) throws IOException {

        byte[] bytes = packet.getData();

        DataPacketBuffer dataBuffer = new DataPacketBuffer(bytes);
        int previousBlock = 0;
        if (dataBuffer.getBlock() > 1) {
            DataPacketBuffer previousData = (DataPacketBuffer) received;
            previousBlock = previousData.getBlock();
        }
        if (dataBuffer.getBlock() == previousBlock + 1) {
            bufferedWriter.write(dataBuffer.getData());
        } else {
            resend();
        }

        if (dataBuffer.isTerminating()) {
            terminated = true;
        }

        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(0);
        send(acknowledgementBuffer.getByteBuffer());
    }


}
