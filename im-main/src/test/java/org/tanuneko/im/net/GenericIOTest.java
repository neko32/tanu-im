package org.tanuneko.im.net;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.model.Generic;
import org.tanuneko.im.model.Message;
import org.tanuneko.im.model.User;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.notNull;

/**
 * Created by neko32 on 2016/12/18.
 */
public class GenericIOTest {

    private static MessagerUI mockUI;

    private static final String TEST_NAME = "test";
    private static final String TEST_GROUP = "TEST_GRP";

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initSysProperty(GenericIOTest.class.getClassLoader().getResourceAsStream("application.properties"));
        Resource.initAppProperty(GenericIOTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        LocalUserBuilder.clearCache();

        mockUI = Mockito.mock(MessagerUI.class);
        Mockito.doNothing().when(mockUI).handleReceivedMessage((Message)notNull());
    }

    public static void teardownClass() {
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testMessage() throws IOException {
        int portNum = Integer.parseInt(Resource.getProperty(Resource.RES_SYS_GENERIC_PORT));
        GenericDataReceiver recv = null;
        GenericDataSender sender = null;
        try {
            recv = new GenericDataReceiver(mockUI, portNum);
            recv.start();
            sender = new GenericDataSender(InetAddress.getByName(getLocalNetwork()), portNum);
            try {
                Generic g = new Generic(Generic.CMD_SEND_USER_ICON);
                g = sender.send(g);
                assertThat(g.getCmd(), is(Generic.CMD_SEND_USER_ICON_RESP));
                assertThat(g.getExtra(), is(nullValue()));
                byte[] imgBytes = g.getData();
                ByteArrayInputStream bin = new ByteArrayInputStream(imgBytes);
                Image img = ImageIO.read(bin);
                assertThat(img.getWidth(null), is(64));
                assertThat(img.getHeight(null), is(64));
                bin.close();
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
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
}
