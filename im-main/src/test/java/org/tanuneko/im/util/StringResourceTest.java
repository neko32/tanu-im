package org.tanuneko.im.util;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class StringResourceTest {

    @Test(expected=IllegalArgumentException.class)
    public void testStringResourceWithUnsupportedLocale() throws IOException {
       StringResource.init("no");
    }

    @Test
    public void testStringResourceWithEnglish() throws IOException {
        StringResource.init("en");
        assertThat(StringResource.get(StringResource.STR_MAINWND_TITLE), is("Tanuki Messager"));
    }

    @Test
    public void testStringResourceWithJapanese() throws IOException {
        StringResource.init("ja");
        assertThat(StringResource.get(StringResource.STR_MAINWND_TITLE), is("おたぬき"));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<StringResource> cons = StringResource.class.getDeclaredConstructor();
        cons.setAccessible(true);
        StringResource r = cons.newInstance();
    }
}
