package com.guym4c.uni.networks.coursework2.server;

import java.net.SocketException;

public class WriteRequestServlet extends RequestServlet {


    public WriteRequestServlet(byte[] bytes) throws SocketException {
        super(bytes);
    }

    @Override
    public void run() {

    }

}
