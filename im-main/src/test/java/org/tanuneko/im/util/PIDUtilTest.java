package org.tanuneko.im.util;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/19.
 */
public class PIDUtilTest {

    @Test
    public void testPID() throws IOException {
        PIDUtil.createPID();
        assertThat(PIDUtil.exists(), is(true));
        PIDUtil.removePID();
        assertThat(PIDUtil.exists(), is(false));
    }

    @Test(expected=IOException.class)
    public void testPIDNotRemoveFirst() throws IOException {
        PIDUtil.removePID();
    }

    @Test(expected=IOException.class)
    public void testPIDNotPIDDeleteTwice() throws IOException {
        PIDUtil.createPID();
        PIDUtil.removePID();
        PIDUtil.removePID();
    }

    @Test(expected=IOException.class)
    public void testPIDNotPIDCreateTwice() throws IOException {
        try {
            PIDUtil.createPID();
            PIDUtil.createPID();
        } finally {
            PIDUtil.removePID();
        }
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<PIDUtil> cons = PIDUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        PIDUtil r = cons.newInstance();
    }
}
