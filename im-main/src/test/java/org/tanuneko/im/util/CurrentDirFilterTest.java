package org.tanuneko.im.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class CurrentDirFilterTest {

    @Test
    public void testReplaceCurDir() {
        String replaced = CurrentDirFilter.replaceCurrentDir(CurrentDirFilter.CURRENT_DIR_KEY);
        assertThat(replaced.contains(CurrentDirFilter.CURRENT_DIR_KEY), is(false));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CurrentDirFilter> cons = CurrentDirFilter.class.getDeclaredConstructor();
        cons.setAccessible(true);
        CurrentDirFilter r = cons.newInstance();
    }
}
