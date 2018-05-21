package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

public class WriteRequestServlet extends ReceiveThread {

    private FileOutputStream fileWriter;
    private File file;

    public WriteRequestServlet(DatagramPacket packet, int tid) throws IOException {
        super(packet, tid);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(packet.getData());

        System.out.println("Received by " + this.getName());
        System.out.println(requestBuffer);

        file = new File(requestBuffer.getFilename());
        fileWriter = new FileOutputStream(file);
        sendPort = packet.getPort();

        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(0);

        send(acknowledgementBuffer);
    }

    @Override
    public void run() {
        while (!destroyable) {
            DatagramPacket packet = new DatagramPacket(new byte[maxPacketSize], maxPacketSize);
            try {
                socket.receive(packet);
                receive(packet);
            } catch (SocketTimeoutException timeout) {
                if (terminated) {
                    socket.close();
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    destroyable = true;
                    success = true;
                } else {
                    if (!attemptReSend()) {
                        destroyable = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        conclude(success);
    }

    @Override
    void receive(DatagramPacket packet) {

        DataPacketBuffer dataBuffer = new DataPacketBuffer(getBytesFromPacket(packet));

        System.out.println("Received by " + this.getName());
        System.out.println(dataBuffer);

        int previousBlock = 0;
        if (dataBuffer.getBlock() > 1) {
            DataPacketBuffer previousData = (DataPacketBuffer) received;
            previousBlock = previousData.getBlock();
        }
        if (dataBuffer.getBlock() == previousBlock + 1) {
            try {
                fileWriter.write(dataBuffer.getData());
            } catch (IOException e) {
                error();
            }
            received = dataBuffer;
        } else if (!attemptReSend()) {
            destroyable = true;
        }

        if (dataBuffer.isTerminating()) {
            terminated = true;
            success = true;
        }

        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(dataBuffer.getBlock());
        send(acknowledgementBuffer);
    }


}
