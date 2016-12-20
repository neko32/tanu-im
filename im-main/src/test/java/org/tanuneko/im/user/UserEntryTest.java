package org.tanuneko.im.user;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.model.User;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/17.
 */
public class UserEntryTest {

    private static User user;

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initAppProperty(UserEntryTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        user = LocalUserBuilder.createOrGetLocalUser();
    }

    @Test
    public void testIncrement() {
        UserEntry userEnt = new UserEntry(user);
        assertThat(userEnt.getCounter(), is(UserEntry.CTR_ACTIVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_NOT_ACTIVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_NOT_ACTIVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_LEAVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_LEAVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_LEAVE));
        userEnt.reset();
        assertThat(userEnt.getCounter(), is(UserEntry.CTR_ACTIVE));
        assertThat(userEnt.increment(), is(UserEntry.CTR_NOT_ACTIVE));
    }
}
