package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by neko32 on 2016/12/07.
 */
@SuppressWarnings("ALL")
public class MessageReceiver extends Thread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);
    private static int numMessageReceived = 0;
    private MessagerUI uiRef;
    private int port;
    private boolean isContinue;
    private boolean isClosed;
    private ServerSocket serverSock;

    public MessageReceiver(MessagerUI uiRef, int port) {
        this.uiRef = uiRef;
        this.port = port;
        isContinue = true;
        isClosed = false;
        serverSock = null;
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try {
            serverSock = new ServerSocket(port);
            while (isContinue) {
                LOG.info("Waiting for message..");
                try (Socket sock = serverSock.accept()) {
                    LOG.info("Received a message. Processing..");
                    in = new ObjectInputStream(sock.getInputStream());
                    Message message = (Message) in.readObject();
                    uiRef.handleReceivedMessage(message);
                    numMessageReceived++;
                } catch (IOException | ClassNotFoundException e) {
                    if(isContinue) {
                        LOG.error(e.getMessage(), e);
                    }
               }
            }
        } catch (IOException e) {
            if(isContinue) {
                LOG.error(e.getMessage(), e);
            }
        } finally {
            LOG.info("Message Receiver has stopped.");
            isClosed = true;
        }
    }

    public void shutdown() {
        if(serverSock != null) {
            try {
                serverSock.close();
                serverSock = null;
                isClosed = true;
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public int getNumMessageReceived() {
        return numMessageReceived;
    }
}
