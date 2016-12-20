package org.tanuneko.im.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/13.
 */
@SuppressWarnings("ALL")
public class UnicodeUtilTest {

    @Test
    public void testAscii() {
        assertThat(UnicodeUtil.getByteOfChar('A'), is(1));
    }

    @Test
    public void test2ByteJa() {
        assertThat(UnicodeUtil.getByteOfChar('や'), is(2));
    }

    @Test
    public void test2ByteKanji() {
        assertThat(UnicodeUtil.getByteOfChar('猫'), is(2));
    }

    @Test
    public void testTotalByteCount() {
        final String testStr = "abcたぬき者";
        assertThat(UnicodeUtil.getTotalByteCounts(testStr), is(11));
    }

    @Test
    public void testLikely2Byte() {
        final String complete2Bytes = "たぬきー";
        final String complete1Byte = "tanuki-";
        final String mixedWithTwo2Bytes = "たぬKi";
        final String mixedWithThree2Bytes = "たぬきちtanukichi";

        assertThat(UnicodeUtil.isLikely2ByteString(complete1Byte), is(false));
        assertThat(UnicodeUtil.isLikely2ByteString(complete2Bytes), is(true));
        assertThat(UnicodeUtil.isLikely2ByteString(mixedWithTwo2Bytes), is(false));
        assertThat(UnicodeUtil.isLikely2ByteString(mixedWithThree2Bytes), is(true));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<UnicodeUtil> cons = UnicodeUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        UnicodeUtil r = cons.newInstance();
    }
}
