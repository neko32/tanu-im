package org.tanuneko.im.user;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.model.User;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/17.
 */
public class UserMasterTest {

    private static User localUser;
    private static User nonLocalUser;

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initAppProperty(UserEntryTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        localUser = LocalUserBuilder.createOrGetLocalUser();
        nonLocalUser = createTestUser();
    }

    @AfterClass
    public static void tearDownClass() {
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testUpdate() {
        UserMaster master = new UserMaster(localUser);
        assertThat(master.update(localUser), is(UserMaster.FOLLOWUP_NOOP));
        assertThat(master.update(nonLocalUser), is(UserMaster.FOLLOWUP_NEWUSER));
        nonLocalUser.setExtraInfo("BYE");
        assertThat(master.update(nonLocalUser), is(UserMaster.FOLLOWUP_REMOVEUSER));
    }

    private static User createTestUser() throws UnknownHostException {
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
