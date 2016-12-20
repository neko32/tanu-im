package org.tanuneko.im.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class SimpleIDGenTest {

    @Test
    public void testIDGen() {
        SimpleIDGen.reset();
        assertThat(SimpleIDGen.generateID(), is(1));
        assertThat(SimpleIDGen.generateID(), is(2));
        assertThat(SimpleIDGen.generateID(), is(3));
        assertThat(SimpleIDGen.generateID(), is(4));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<SimpleIDGen> cons = SimpleIDGen.class.getDeclaredConstructor();
        cons.setAccessible(true);
        SimpleIDGen r = cons.newInstance();
    }
}
