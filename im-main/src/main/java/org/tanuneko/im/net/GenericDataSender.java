package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.Generic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class GenericDataSender {

    private final int port;
    private final InetAddress dest;
    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    public GenericDataSender(InetAddress dest, int port) {
        this.port = port;
        this.dest = dest;
    }

    public Generic send(Generic genericData) throws IOException, ClassNotFoundException {
        try(Socket sock = new Socket(dest, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream())) {
            LOG.info("opened generic data socket to {}:{}", dest.getHostAddress(), port);
            out.writeObject(genericData);
            out.flush();
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            Generic resp = (Generic)in.readObject();
            LOG.info("Generic data was responded back");
            return resp;
        }
    }
}
