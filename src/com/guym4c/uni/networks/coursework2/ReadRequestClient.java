package com.guym4c.uni.networks.coursework2;

public class ReadRequestClient extends ReceiveThread {

    public ReadRequestClient(String address, int port, String filename, int tid) {
        super(address, port, tid);

        initialiseFile(filename);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.ReadRequest, filename, DEFAULT_MODE);
        send(requestBuffer);
    }

}
