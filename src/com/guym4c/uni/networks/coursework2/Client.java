package com.guym4c.uni.networks.coursework2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client extends NetworkObject {

    public Client() {
        super(generateTid());
    }

    @Override
    public void run() {
        boolean exit = false;

        while (!exit) {
            System.out.println("Choose: ");
            System.out.println("1: Get a file");
            System.out.println("2: Send a file");
            System.out.println("3: Exit");
            System.out.println();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                int choice = Integer.parseInt(reader.readLine());
                System.out.println("Please enter the filename:");
                String file = reader.readLine();
                int tid = generateTid();
                switch (Opcode.fromInt(choice)) {
                    case ReadRequest:
                        new ReadRequestClient(SERVER_IP, SERVER_PORT, file, tid).start();
                        break;
                    case WriteRequest:
                        new WriteRequestClient(SERVER_IP, SERVER_PORT, file, tid).start();
                        break;
                    default:
                        exit = true;
                        break;
                }
            } catch (IOException e) {
                exit = true;
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

}
