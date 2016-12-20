package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.User;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.VersionChecker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by neko32 on 2016/12/04.
 */
@SuppressWarnings("ALL")
public class PeerProbeReceiver extends Thread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PeerProbeReceiver.class);
    private int port;
    private boolean isContinue;
    private MessagerUI uiRef;
    private boolean isClosed;
    private int numProbeReceived;
    public static final String CMD_PROBE = "PROBE";
    public static final String CMD_ACK = "ACK";
    public static final String SEPARATOR = "|";

    public PeerProbeReceiver(MessagerUI uiRef, int port) {
        this.port = port;
        isContinue = true;
        this.uiRef = uiRef;
        isClosed = true;
        numProbeReceived = 0;
    }

    public void run() {
        byte[] msgBuf = new byte[8092];
        isClosed = false;
        User localUser;
        try {
            localUser = LocalUserBuilder.createOrGetLocalUser();
        } catch(IOException ioE) {
            LOG.error(ioE.getMessage(), ioE);
            throw new IllegalStateException(ioE);
        }

        try {
            while (isContinue) {
                DatagramPacket pack = new DatagramPacket(msgBuf, msgBuf.length);
                try (DatagramSocket sock = new DatagramSocket(port)) {
                    sock.receive(pack);
                    try(ByteArrayInputStream bis = new ByteArrayInputStream(msgBuf);
                        ObjectInput in = new ObjectInputStream(bis)) {
                        User user = (User)in.readObject();
                        LOG.debug("Received msg[{}]", user);
                        numProbeReceived++;

                        // first check if the probe belongs to same group
                        if(!user.getGroupName().equals(localUser.getGroupName())) {
                            LOG.info("User group is different. My Group - {}, Group in Probe - {}", localUser.getGroupName(), user.getGroupName());
                            continue;
                        }
                        // then check version
                        if(VersionChecker.isAppOlderThanPeer(localUser.getAppVersion(), user.getAppVersion())) {
                            uiRef.promptUpdate(user.getIpAddress(), user.getAppVersion(), localUser.getAppVersion());
                            isContinue = false;
                            break;
                        }

                        if(user.getExtraInfo() == null) {
                            LOG.debug(String.format("Peer[%s] found or still active", user));
                        }
                        else if(user.getExtraInfo().equals("BYE")) {
                            LOG.debug(String.format("Peer[%s] is going to leave", user));
                        }
                        uiRef.updateUserList(user);
                    }
                } catch (IOException|ClassNotFoundException e) {
                    if(!isContinue && e.getMessage().equals("socket closed")) {
                        // this case fine to ignore exception
                    }
                    else {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
        } finally {
            LOG.info("Peer Probe Receiver has stopped.");
            isClosed = true;
        }
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public int getNumProbeReceived() {
        return numProbeReceived;
    }

}
