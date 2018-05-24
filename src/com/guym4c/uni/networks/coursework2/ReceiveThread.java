package com.guym4c.uni.networks.coursework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

public abstract class ReceiveThread extends CommThread {

    private FileOutputStream fileWriter;
    private File file;

    public ReceiveThread(DatagramPacket packet, int tid) {
        super(packet, tid);
    }

    public ReceiveThread(String address, int port, int tid) {
        super(address, port, tid);
    }

    protected boolean initialiseFile(String filename) {
        file = new File("rcv-" + filename);
        try {
            fileWriter = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * @param block A block number.
     * @return Whether $block is chronological to the previously sent packet's block number.
     */
    protected boolean isChronologicalBlock(int block) {
        int previousBlock = 0;
        if (received instanceof DataPacketBuffer) {
            DataPacketBuffer previousData = (DataPacketBuffer) received;
            previousBlock = previousData.getBlock();
        }
        if (previousBlock == BLOCK_MAX_VALUE && block == 0) {
            return true;
        } else return previousBlock + 1 == block;
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
        System.out.println(conclude(success));
    }

    @Override
    void receive(DatagramPacket packet) {
        GenericPacketBuffer buffer = new GenericPacketBuffer(getBytesFromPacket(packet));

        switch (buffer.getOpcode()) {
            case Data:
                DataPacketBuffer dataBuffer = new DataPacketBuffer(getBytesFromPacket(packet));

                System.out.println("Received by " + this.getName());
                System.out.println(dataBuffer);

                if (isChronologicalBlock(dataBuffer.getBlock())) {
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

                sendPort = packet.getPort();
                TransmissionPacketBuffer acknowledgementBuffer = new TransmissionPacketBuffer(dataBuffer.getBlock());
                send(acknowledgementBuffer);
                break;

            case Error:
                ErrorPacketBuffer errorBuffer = new ErrorPacketBuffer(getBytesFromPacket(packet));

                System.out.println("Received by " + this.getName());
                System.out.println(errorBuffer);

                System.out.println(String.format("%s: %s", errorBuffer.getErrorCode().toString(), errorBuffer.getMessage()));
                destroyable = true;
        }

    }


}
