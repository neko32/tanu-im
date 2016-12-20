package org.tanuneko.im.net;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.ConversationItemTest;
import org.tanuneko.im.model.Message;
import org.tanuneko.im.model.User;
import org.tanuneko.im.user.UserMaster;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.notNull;

/**
 * Created by neko32 on 2016/12/18.
 */
public class MessageIOTest {

    private static MessagerUI mockUI;

    private static final String TEST_NAME = "test";
    private static final String TEST_RECV_NAME = "test_receiver";
    private static final String TEST_GROUP = "TEST_GRP";

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initSysProperty(MessageIOTest.class.getClassLoader().getResourceAsStream("application.properties"));
        Resource.initAppProperty(MessageIOTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        LocalUserBuilder.clearCache();

        mockUI = Mockito.mock(MessagerUI.class);
        Mockito.doNothing().when(mockUI).handleReceivedMessage((Message)notNull());
    }

    public static void teardownClass() {
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testMessage() throws IOException {
        int portNum = Integer.parseInt(Resource.getProperty(Resource.RES_SYS_SERVER_PORT));
        MessageReceiver recv = null;
        MessageSender sender = null;
        try {
            recv = new MessageReceiver(mockUI, portNum);
            recv.start();
            sender = new MessageSender(InetAddress.getByName(getLocalNetwork()), portNum);
            Message msg = generateTestMessage("TEST");
            sender.send(msg);
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            assertThat(sender.getNumMessageSent(), greaterThan(0));
            assertThat(recv.getNumMessageReceived(), greaterThan(0));
        } finally {
            if(recv != null && !recv.isClosed()) {
                recv.setContinue(false);
                recv.shutdown();
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
        }
    }

    private String getLocalNetwork() throws IOException {
        User localUser = LocalUserBuilder.createOrGetLocalUser();
        return localUser.getIpAddress().getHostAddress();
    }

    private Message generateTestMessage(String message) throws IOException {
        Message msg = new Message(TEST_NAME, InetAddress.getLocalHost(), TEST_GROUP, TEST_RECV_NAME, InetAddress.getLocalHost(), TEST_GROUP, message);
        msg.setSenderImage(FileUtils.readFileToByteArray(new File(MessageIOTest.class.getClassLoader().getResource("images/icon/icon.png").getPath())));
        return msg;
    }
}
