package org.tanuneko.im.model;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class UserTest {


    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initAppProperty(UserTest.class.getClassLoader().getResource("conf/tanuim_test.properties").getPath());
        StringResource.init("en");
    }

    @Test
    public void testUserWithDefaultConstructor() throws UnknownHostException {
        final int ID = 100;
        final String name = "test_test_";
        final String group = "GRUPO";
        final String machine = "M_A_";
        final byte[] icon = "TEST_ICON".getBytes();
        final String extra = "E*X*T*R*A";
        User user = new User();
        user.setId(ID);
        user.setUserName(name);
        user.setGroupName(group);
        user.setMachineName(machine);
        user.setIcon(icon);
        user.setExtraInfo(extra);
        user.setIpAddress(InetAddress.getLocalHost());
        assertThat(user.getId(), is(ID));
        assertThat(user.getUserName(), is(name));
        assertThat(user.getGroupName(), is(group));
        assertThat(user.getExtraInfo(), is(extra));
        assertThat(user.getIcon(), is(icon));
        assertThat(user.getAppVersion(), is("0.9.0"));
        assertThat(user.getIpAddress(), is(InetAddress.getLocalHost()));
        assertThat(user.getMachineName(), is(machine));
    }

    @Test
    public void testUser() throws UnknownHostException {
        final int ID = 100;
        final String name = "test_test_";
        final String group = "GRUPO";
        final String machine = "M_A_";
        final byte[] icon = "TEST_ICON".getBytes();
        final String extra = "E*X*T*R*A";
        User user = new User(ID, name, group, machine, icon, InetAddress.getLocalHost());
        user.setExtraInfo(extra);
        assertThat(user.getId(), is(ID));
        assertThat(user.getUserName(), is(name));
        assertThat(user.getGroupName(), is(group));
        assertThat(user.getExtraInfo(), is(extra));
        assertThat(user.getIcon(), is(icon));
        assertThat(user.getIpAddress(), is(InetAddress.getLocalHost()));
        assertThat(user.getMachineName(), is(machine));
    }

    @Test
    public void testGetIcon() throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(new File(UserTest.class.getClassLoader().getResource("images/icon/default.png").getPath()));
        User user = new User();
        user.setIcon(bytes);
        ImageIcon img = user.getIconAsImageIcon();
        assertThat(img.getIconWidth(), is(50));
        assertThat(img.getIconHeight(), is(50));
    }

    @Test
    public void testCompareTo() {
        User user = new User();
        user.setUserName("Tanuki");
        User user2 = new User();
        user2.setUserName("Tanuki");
        assertThat(user.compareTo(user2), is(0));
    }

    @Test
    public void testToString() {
        final String name = "Tanu";
        final String group = "GSTANU";
        User user = new User();
        user.setUserName(name);
        user.setGroupName(group);
        String t = String.format("%s[%s]", user, group);
        assertThat(user.toString(), is(String.format("%s[%s]", name, group)));
    }

    @Test
    public void testEqualsAndHash() throws UnknownHostException {
        User user1 = createTestUser();
        User user2 = createTestUser();
        assertThat(user1, is(user2));
        assertThat(user1.hashCode(), is(user2.hashCode()));
    }

    private User createTestUser() throws UnknownHostException {
        final int ID = 100;
        final String name = "test_test_";
        final String group = "GRUPO";
        final String machine = "M_A_";
        final byte[] icon = "TEST_ICON".getBytes();
        final String extra = "E*X*T*R*A";
        User user = new User(ID, name, group, machine, icon, InetAddress.getLocalHost());
        user.setExtraInfo(extra);
        return user;
    }
}
