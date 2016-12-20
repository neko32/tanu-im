package org.tanuneko.im.net;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.User;
import org.tanuneko.im.user.UserMaster;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.notNull;

/**
 * Created by neko32 on 2016/12/17.
 */
public class PeerProbeAndByeTest {

    private static MessagerUI uiRef;

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initSysProperty(PeerProbeAndByeTest.class.getClassLoader().getResourceAsStream("application.properties"));
        Resource.initAppProperty(PeerProbeAndByeTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        LocalUserBuilder.clearCache();
        uiRef = new MessagerUI(null);
        uiRef.setUserMaster(new UserMaster(LocalUserBuilder.createOrGetLocalUser()));
    }

    public static void teardownClass() {
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testPeerProbe() throws IOException {
        int portNum = Integer.parseInt(Resource.getProperty(Resource.RES_SYS_PEER_PROBE_PORT));
        PeerProbeReceiver recv = null;
        PeerProbeSender sender = null;
        try {
            recv = new PeerProbeReceiver(uiRef, portNum);
            recv.start();
            sender = new PeerProbeSender(getLocalNetwork(), portNum);
            sender.start();
            PeerByeSender byeSender = new PeerByeSender(getLocalNetwork(), portNum);
            byeSender.sendBye();

            Thread.sleep(5000);
            assertThat(sender.getNumProbeSent(), greaterThan(0));
            assertThat(recv.getNumProbeReceived(), greaterThan(0));
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(recv != null && !recv.isClosed()) {
                recv.setContinue(false);
                recv.interrupt();
            }
            int retry = 0;
            while(retry++ < 3) {
                try {
                    if(recv.isClosed()) {
                        break;
                    }
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            if(sender != null && !sender.isClosed()) {
                sender.setContinue(false);
                sender.interrupt();
            }

            retry = 0;
            while(retry++ < 3) {
                try {
                    if(sender.isClosed()) {
                        break;
                    }
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    private String getLocalNetwork() throws IOException {
        User localUser = LocalUserBuilder.createOrGetLocalUser();
        String localIP = localUser.getIpAddress().getHostAddress();
        return localIP.substring(0, localIP.lastIndexOf(".")) + ".255";
    }
}
