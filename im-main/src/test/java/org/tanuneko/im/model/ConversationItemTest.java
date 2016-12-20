package org.tanuneko.im.model;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.ResourceTest;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class ConversationItemTest {

    private static final String SEP = System.getProperty("line.separator");
    private static final String TEST_NAME = "test";
    private static final String TEST_RECV_NAME = "test_other";
    private static final String TEST_GROUP = "TEST_GRP";
    private static final String TEST_MSG = String.format("%s%s%s%s%s", "test_message", SEP, "testtest", SEP, "test3rdline");

    @BeforeClass
    public static void setUpClass() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testAccessors() throws IOException {
        ConversationItem ci = new ConversationItem(generateTestMessage(false));
        assertThat(ci.getUserName(), is(TEST_NAME));
        assertThat(ci.getGroupName(), is(TEST_GROUP));
        assertThat(ci.getMessage(), is(TEST_MSG));
        assertThat(ci.getUserIcon().getWidth(null), is(96));
        assertThat(ci.getUserIcon().getHeight(null), is(96));
        assertThat(ci.getStamp(), nullValue());
        assertThat(ci.getCols(), is(12));
        assertThat(ci.getRows(), is(3));
        assertThat(ci.isMyMessage(), is(false));
        assertThat(ci.toString().contains(TEST_NAME), is(true));
        ci = new ConversationItem(generateTestMessage(true));
        assertThat(ci.getStamp().getWidth(null), is(64));
        assertThat(ci.getStamp().getHeight(null), is(64));
    }

    private Message generateTestMessage(boolean isStampRequired) throws IOException {
        Message msg = new Message(TEST_NAME, InetAddress.getLocalHost(), TEST_GROUP, TEST_RECV_NAME, InetAddress.getLocalHost(), TEST_GROUP, TEST_MSG);
        msg.setSenderImage(FileUtils.readFileToByteArray(new File(ConversationItemTest.class.getClassLoader().getResource("images/icon/icon.png").getPath())));
        if(isStampRequired) {
            msg.setStamp(FileUtils.readFileToByteArray(new File(ConversationItemTest.class.getClassLoader().getResource("images/icon/default.png").getPath())));
        }
        return msg;
    }
}
