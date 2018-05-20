package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Arrays;

public class WriteRequestClient extends SendThread {

    private byte[] file;
    private int filePointer = 0;

    public WriteRequestClient(String address, int port, String filename, int tid) throws IOException {
        super(address, port, tid);
        file = Files.readAllBytes(new File(filename).toPath());

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.WriteRequest, filename, DEFAULT_MODE);
        send(requestBuffer.getByteBuffer());
    }

    @Override
    public void run() {
        while (!destroyable) {
            DatagramPacket packet = new DatagramPacket(new byte[maxPacketSize], maxPacketSize);
            try {
                socket.receive(packet);
                receive(packet);
            } catch (SocketTimeoutException timeout) {
                resend();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void receive(DatagramPacket packet) {
        byte[] bytes = packet.getData();
        TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(bytes);

        if (acknowledgementBuffer.getBlock() != lastSentBlock || terminated) {
            destroyable = true;
        } else {
            DataPacketBuffer dataBuffer = new DataPacketBuffer(incrementBlock(), getNextFileHunk());
            send(dataBuffer.getByteBuffer());
            received = acknowledgementBuffer;
        }
    }

    private byte[] getNextFileHunk() {
        byte[] hunk;
        if (file.length - filePointer < MAX_PAYLOAD_SIZE) {
            hunk = Arrays.copyOfRange(file, filePointer, file.length - 1);
            terminated = true;
        } else {
            hunk = Arrays.copyOfRange(file, filePointer, filePointer + MAX_PAYLOAD_SIZE);
        }
        filePointer += hunk.length;
        return hunk;
    }
}
