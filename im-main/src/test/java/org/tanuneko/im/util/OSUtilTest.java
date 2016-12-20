package org.tanuneko.im.util;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by neko32 on 2016/12/15.
 */
public class OSUtilTest {

    @Test
    public void testOS() {
        // can't assert due to OS dependency
        if(SystemUtils.IS_OS_WINDOWS)
            assertThat(OSUtil.getOSType(), is(OSType.WINDOWS));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<OSUtil> cons = OSUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        OSUtil r = cons.newInstance();
    }
}
