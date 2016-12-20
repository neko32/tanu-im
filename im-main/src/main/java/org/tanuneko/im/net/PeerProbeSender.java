package org.tanuneko.im.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.User;
import org.tanuneko.im.net.util.NetworkIFUtil;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * Created by neko32 on 2016/12/04.
 */
@SuppressWarnings("ALL")
public class PeerProbeSender extends Thread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PeerProbeSender.class);
    private final String addr;
    private final int port;
    private boolean isContinue;
    private boolean isClosed;
    private int numProbeSent;

    public PeerProbeSender(String addr, int port) {
        this.addr = addr;
        this.port = port;
        numProbeSent = 0;
        isClosed = isContinue = true;
    }

    public void run() {
        isClosed = false;
        try(DatagramSocket sock = new DatagramSocket();
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bs)) {
            InetAddress grp = InetAddress.getByName(addr);
            InetAddress ipAddr = NetworkIFUtil.findPreferredNetIF(Resource.getProperty(Resource.RES_PREFERRED_IF));
            User user = LocalUserBuilder.createOrGetLocalUser();
            out.writeObject(user);
            out.flush();
            byte[] msg = bs.toByteArray();
            while (isContinue) {
                LOG.debug("Sending packet[{}]..", user);
                DatagramPacket packet = new DatagramPacket(msg, msg.length, grp, port);
                sock.send(packet);
                numProbeSent++;
                Thread.sleep(10000);
            }
        } catch(InterruptedException|IOException e) {
            if(isContinue) {
                LOG.error(e.getMessage(), e);
            }
        } finally {
            LOG.info("Peer Probe Sender has stopped.");
            isClosed = true;
        }
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public int getNumProbeSent() {
        return numProbeSent;
    }
}
