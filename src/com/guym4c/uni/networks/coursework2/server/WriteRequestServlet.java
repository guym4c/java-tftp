package com.guym4c.uni.networks.coursework2.server;

import com.guym4c.uni.networks.coursework2.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

public class WriteRequestServlet extends RequestServlet {

    private Writer fileWriter;
    private BufferedWriter bufferedWriter;
    private boolean terminated;
    private boolean destroyable;

    public WriteRequestServlet(DatagramPacket packet, int tid) throws IOException {
        super(packet, tid);
        terminated = false;
        destroyable = false;

        TftpRequestPacketBuffer requestBuffer = new TftpRequestPacketBuffer(packet.getData());

        fileWriter = new FileWriter(requestBuffer.getFilename());
        bufferedWriter = new BufferedWriter(fileWriter);

        TftpTransmissionPacketBuffer acknowledgementBuffer = new TftpTransmissionPacketBuffer(0);

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

        TftpDataPacketBuffer dataBuffer = new TftpDataPacketBuffer(bytes);
        TftpDataPacketBuffer previousData = (TftpDataPacketBuffer) received;
        if (dataBuffer.getBlock() == previousData.getBlock() + 1) {
            bufferedWriter.write(dataBuffer.getData());
        }

        if (dataBuffer.isTerminating()) {
            terminated = true;
        }

        TftpTransmissionPacketBuffer acknowledgementBuffer = new TftpTransmissionPacketBuffer(0);
        send(acknowledgementBuffer.getByteBuffer());
    }


}
