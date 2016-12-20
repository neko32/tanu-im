package org.tanuneko.im.util;

import org.junit.Test;
import org.tanuneko.im.model.User;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class LocalUserBuilderTest {

    @Test
    public void testLocalUserBuild() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        User user = LocalUserBuilder.createOrGetLocalUser();
        assertThat(user.getUserName(), is("testman"));
        // access cache
        user = LocalUserBuilder.createOrGetLocalUser();
        assertThat(user.getUserName(), is("testman"));
        // with auto IF
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test_with_auto.properties"));
        LocalUserBuilder.clearCache();
        user = LocalUserBuilder.createOrGetLocalUser();
        assertThat(user.getUserName(), is("testman"));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<LocalUserBuilder> cons = LocalUserBuilder.class.getDeclaredConstructor();
        cons.setAccessible(true);
        LocalUserBuilder r = cons.newInstance();
    }
}
