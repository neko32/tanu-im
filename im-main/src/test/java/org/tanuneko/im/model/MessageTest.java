package org.tanuneko.im.model;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/17.
 */
public class MessageTest {

    private static final String SEP = System.getProperty("line.separator");
    private static final String TEST_NAME = "test";
    private static final String TEST_GROUP = "TEST_GRP";
    private static final String TEST_RECV_NAME = "test_other";
    private static final String ATTACHMENT1_NAME = "ATTACHMENT1";
    private static final byte[] ATTACHMENT1_DATA = "TEST TEST DATA TEST".getBytes();
    private static final String ATTACHMENT2_NAME = "ATTACHMENT2";
    private static final byte[] ATTACHMENT2_DATA = "AAAABBBBCCCCZ".getBytes();
    private static final String TEST_MSG = String.format("%s%s%s%s%s", "test_message", SEP, "testtest", SEP, "test3rdline");
    private static final String TEST_SINGLEBYTE_SHORT_MSG = "abc";
    private static final String TEST_SINGLEBYTE_LONG_MSG = "aaaaabbbbbcccccdddddeeeeefffffggggghhhhhiiiiijjjjjlllll";
    private static final String TEST_2BYTES_SHORT_MSG = "２バイトのメッセージ";
    private static final String TEST_2BYTES_LONG_MSG = "むかしむかしあるところにおじいさんとおばあさんがおったとさ";

    @BeforeClass
    public static void classSetup() throws IOException {
        Resource.initAppProperty(MessageTest.class.getClassLoader().getResource("conf/tanuim_test.properties").getPath());
        StringResource.init("en");
    }

    @Test
    public void testBasicConstructorAndAccessors() throws IOException {
        Message msg = new Message();
        assertThat(msg.isConfirmationRequired(), is(false));
        assertThat(msg.isEnvelopeRequired(), is(false));
        msg.setSenderName(TEST_NAME);
        msg.setSenderGroupName(TEST_GROUP);
        msg.setSenderAddress(InetAddress.getLocalHost());
        msg.setMessage(TEST_MSG);
        msg.setSenderImage(readImageFile("images/icon/icon.png"));
        msg.setStamp(readImageFile("images/icon/default.png"));
        msg.setEnvelopeRequired(true);
        msg.setConfirmationRequired(true);
        msg.setAttachments(prepareAttachments());
        assertThat(msg.getSenderName(), is(TEST_NAME));
        assertThat(msg.getSenderGroupName(), is(TEST_GROUP));
        assertThat(msg.getSenderAddress(), is(InetAddress.getLocalHost()));
        assertThat(msg.getMessage(), is(TEST_MSG));
        assertThat(byteArrayToImage(msg.getSenderImage()).getWidth(null), is(96));
        assertThat(byteArrayToImage(msg.getStamp()).getWidth(null), is(64));
        java.util.List<Attachment> attachments = msg.getAttachments();
        assertThat(attachments.get(0).getFileName(), is(ATTACHMENT1_NAME));
        assertThat(attachments.get(0).getData(), is(ATTACHMENT1_DATA));
        assertThat(attachments.get(1).getFileName(), is(ATTACHMENT2_NAME));
        assertThat(attachments.get(1).getData(), is(ATTACHMENT2_DATA));
        msg = generateTestMessage(TEST_2BYTES_SHORT_MSG, false);
        assertThat(msg.getMessage(), is(TEST_2BYTES_SHORT_MSG));
    }

    @Test
    public void testSingleByteLongMessage() throws IOException {
        Message msg = generateTestMessage(TEST_SINGLEBYTE_LONG_MSG, false);
        assertThat(msg.getMessage(), is("aaaaabbbbbcccccdddddeeeeefffffggggghhhhhiiiiijjjjj" + SEP + "lllll"));
    }

    @Test
    public void test2ByteLongMessage() throws IOException {
        Message msg = generateTestMessage(TEST_2BYTES_LONG_MSG, false);
        assertThat(msg.getMessage(), is("むかしむかしあるところにおじいさんとおばあさんが" + SEP + "おったとさ"));
    }

    @Test
    public void testToString() throws IOException {
        Message msg = generateTestMessage(TEST_SINGLEBYTE_SHORT_MSG, false);
        assertThat(msg.toString().contains("senderName='test'"), is(true));
        assertThat(msg.toString().contains("num of attachments=0"), is(true));
        assertThat(msg.toString().contains("message='abc'"), is(true));
        msg.setAttachments(prepareAttachments());
        System.out.println(msg.toString());
        assertThat(msg.toString().contains("num of attachments=2"), is(true));
    }

    @Test
    public void testStringForMessageLog() throws IOException {
        Message msg = generateTestMessage(TEST_SINGLEBYTE_SHORT_MSG, false);
        String s = msg.getStringForMessageLog();
        assertThat(s.startsWith("*test[TEST_GRP]"), is(true));
        assertThat(s.endsWith("abc" + SEP + "**************" + SEP), is(true));
    }

    @Test
    public void testMessageAlterationByAttachments() throws IOException {
        Message msg = generateTestMessage(TEST_SINGLEBYTE_SHORT_MSG, false);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(false);
        assertThat(msg.getMessage(), is("abc" + SEP + "2 attachment(s) [ATTACHMENT1,ATTACHMENT2] has" + SEP + " been sent from test"));

        msg = new Message();
        msg.setMessage(null);
        msg.setSenderName(TEST_NAME);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(false);
        assertThat(msg.getMessage(), is("2 attachment(s) [ATTACHMENT1,ATTACHMENT2] has been" + SEP + " sent from test"));

        msg = new Message();
        msg.setSenderName(TEST_NAME);
        msg.setMessage(TEST_2BYTES_LONG_MSG);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(false);
        assertThat(msg.getMessage(), is("むかしむかしあるところにおじいさんとおばあさんが" + SEP + "おったとさ" + SEP + "2 attachment(s) [" + SEP + "ATTACHMENT1,ATTACHMENT2]" + SEP + " has been sent from test"));

        msg.updateMessageIfAttachmentExists(false);
    }

    @Test
    public void testMessageAlterationByAttachmentsAsSelfMessage() throws IOException {
        Message msg = generateTestMessage(TEST_SINGLEBYTE_SHORT_MSG, false);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(true);
        assertThat(msg.getMessage(), is("abc" + SEP + "2 attachment(s) [ATTACHMENT1,ATTACHMENT2] has" + SEP + " been sent to test_other"));

        msg = new Message();
        msg.setMessage(null);
        msg.setSenderName(TEST_NAME);
        msg.setReceiverName(TEST_NAME);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(true);
        assertThat(msg.getMessage(), is("2 attachment(s) [ATTACHMENT1,ATTACHMENT2] has been" + SEP + " sent to test"));

        msg = new Message();
        msg.setSenderName(TEST_NAME);
        msg.setMessage(TEST_2BYTES_LONG_MSG);
        msg.setReceiverName(TEST_NAME);
        msg.setAttachments(prepareAttachments());
        msg.updateMessageIfAttachmentExists(true);
        assertThat(msg.getMessage(), is("むかしむかしあるところにおじいさんとおばあさんが" + SEP + "おったとさ" + SEP + "2 attachment(s) [" + SEP + "ATTACHMENT1,ATTACHMENT2]" + SEP + " has been sent to test"));
    }

    private java.util.List<Attachment> prepareAttachments() {
        java.util.List<Attachment> list = new ArrayList<>();
        Attachment attach1 = new Attachment(ATTACHMENT1_NAME, ATTACHMENT1_DATA);
        Attachment attach2 = new Attachment(ATTACHMENT2_NAME, ATTACHMENT2_DATA);
        list.add(attach1);
        list.add(attach2);
        return list;
    }

    private Image byteArrayToImage(byte[] data) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        return ImageIO.read(bin);
    }

    private byte[] readImageFile(String path) throws IOException {
        return FileUtils.readFileToByteArray(new File(MessageTest.class.getClassLoader().getResource(path).getPath()));
    }

    private Message generateTestMessage(String message, boolean isStampRequired) throws IOException {
        Message msg = new Message(TEST_NAME, InetAddress.getLocalHost(), TEST_GROUP, TEST_RECV_NAME, InetAddress.getLocalHost(), TEST_GROUP, message);
        msg.setSenderImage(FileUtils.readFileToByteArray(new File(ConversationItemTest.class.getClassLoader().getResource("images/icon/icon.png").getPath())));
        if(isStampRequired) {
            msg.setStamp(FileUtils.readFileToByteArray(new File(ConversationItemTest.class.getClassLoader().getResource("images/icon/default.png").getPath())));
        }
        return msg;
    }
}
