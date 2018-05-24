package com.guym4c.uni.networks.coursework2;

public class WriteRequestClient extends SendThread {

    public WriteRequestClient(String address, int port, String filename, int tid) {
        super(address, port, tid);
        initialiseFile(filename);

        RequestPacketBuffer requestBuffer = new RequestPacketBuffer(Opcode.WriteRequest, filename);
        send(requestBuffer);
    }

}
