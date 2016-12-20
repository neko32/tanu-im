package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.Generic;
import org.tanuneko.im.model.User;
import org.tanuneko.im.netcmd.AppJarTransferRequest;
import org.tanuneko.im.netcmd.NetCommand;
import org.tanuneko.im.netcmd.NetCommandException;
import org.tanuneko.im.netcmd.UserIconRequest;
import org.tanuneko.im.util.LocalUserBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class GenericDataReceiver extends Thread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);
    private MessagerUI uiRef;
    private int port;
    private boolean isContinue;
    private boolean isClosed;
    private ServerSocket serverSock;
    private Map<String, NetCommand> netCmdMap;

    public GenericDataReceiver(MessagerUI uiRef, int port) {
        this.uiRef = uiRef;
        this.port = port;
        isContinue = true;
        isClosed = false;
        serverSock = null;
        initNetCmdMap();
    }

    @Override
    public void run() {
        ObjectInputStream in;
        ObjectOutputStream out;
        NetCommand cmd;
        try {
            serverSock = new ServerSocket(port);
            while (isContinue) {
                LOG.info("Waiting for generic data..");
                try (Socket sock = serverSock.accept()) {
                    LOG.info("Received a generic data. Processing..");
                    in = new ObjectInputStream(sock.getInputStream());
                    Generic recvGeneric = (Generic) in.readObject();
                    sock.shutdownInput();
                    LOG.info("Request command is {}", recvGeneric.getCmd());
                    cmd = netCmdMap.get(recvGeneric.getCmd());
                    if(cmd == null) {
                        throw new NetCommandException("Not found Command Handler");
                    }
                    Generic resp = cmd.handleCommand(recvGeneric);
                    out = new ObjectOutputStream(sock.getOutputStream());
                    out.writeObject(resp);
                    out.flush();
                    sock.shutdownOutput();
                } catch (NetCommandException | IOException | ClassNotFoundException e) {
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
            LOG.info("Generic Data Receiver has stopped.");
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

    private void initNetCmdMap() {
        netCmdMap = new HashMap<>();
        netCmdMap.put(Generic.CMD_SEND_USER_ICON, new UserIconRequest());
        netCmdMap.put(Generic.CMD_SEND_APP_JAR, new AppJarTransferRequest());
    }
}
