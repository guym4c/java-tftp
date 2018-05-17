package com.guym4c.uni.networks.coursework2.server;

import java.net.SocketException;

public class ReadRequestServlet extends RequestServlet {

    public ReadRequestServlet(byte[] bytes) throws SocketException {
        super(bytes);
    }

    @Override
    public void run() {

    }

}
