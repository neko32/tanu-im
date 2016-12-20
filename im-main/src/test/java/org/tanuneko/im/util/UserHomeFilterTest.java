package org.tanuneko.im.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class UserHomeFilterTest {

    @Test
    public void testWindowsPath() throws Exception {
       String path = String.format("%s\\test", UserHomeFilter.USER_HOME_KEY);
       String replaced = UserHomeFilter.replaceUserHome(path, OSType.WINDOWS);
       assertThat(replaced.contains(UserHomeFilter.USER_HOME_KEY), is(false));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<UserHomeFilter> cons = UserHomeFilter.class.getDeclaredConstructor();
        cons.setAccessible(true);
        UserHomeFilter r = cons.newInstance();
    }
}
