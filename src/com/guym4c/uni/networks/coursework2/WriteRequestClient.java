package com.guym4c.uni.networks.coursework2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class WriteRequestClient extends RequestClient {

    private FileReader fileReader;
    private BufferedReader bufferedReader;

    public WriteRequestClient(String address, int port, String filename, int tid) throws SocketException {
        super(address, port, tid);
        try {
            fileReader = new FileReader(filename);
        } catch (FileNotFoundException notFound) {

        }
        bufferedReader = new BufferedReader(fileReader);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.WriteRequest, filename, DEFAULT_MODE);

    }

    @Override
    public void run() {

    }

    @Override
    void receive(DatagramPacket packet) throws IOException {

    }
}
