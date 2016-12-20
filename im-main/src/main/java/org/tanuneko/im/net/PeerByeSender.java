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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class PeerByeSender {

    private static final Logger LOG = LoggerFactory.getLogger(PeerProbeSender.class);
    private final String addr;
    private final int port;

    public PeerByeSender(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public void sendBye() {
        try(DatagramSocket sock = new DatagramSocket();
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bs)) {
            LOG.info("Sending BYE message..");
            InetAddress grp = InetAddress.getByName(addr);
            InetAddress ipAddr = NetworkIFUtil.findPreferredNetIF(Resource.getProperty(Resource.RES_PREFERRED_IF));
            User user = LocalUserBuilder.createOrGetLocalUser();
            user.setExtraInfo("BYE");
            out.writeObject(user);
            out.flush();
            byte[] msg = bs.toByteArray();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, grp, port);
            sock.send(packet);
            LOG.info("BYE message has been sent.");
        } catch(IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
