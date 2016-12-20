package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by neko32 on 2016/12/07.
 */
@SuppressWarnings("ALL")
public class MessageSender {

    private final int port;
    private final InetAddress dest;
    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);
    private static int numMessageSent = 0;

    public MessageSender(InetAddress dest, int port) {
        this.port = port;
        this.dest = dest;
    }

    public void send(Message message) throws IOException {
        try(Socket sock = new Socket(dest, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream())) {
            LOG.info("opened message socket to {}:{}", dest.getHostAddress(), port);
            out.writeObject(message);
            out.flush();
            numMessageSent++;
        }
    }

    public int getNumMessageSent() {
        return numMessageSent;
    }
}
