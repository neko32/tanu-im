package org.tanuneko.im.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/18.
 */
public class VersionCheckerTest {

    @Test
    public void testVersionCompareFixVersionDiff() {
        String myVer = "1.0.8";
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.0.5"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.0.9"), is(true));
    }

    @Test
    public void testVersionCompareMinorVersionDiff() {
        String myVer = "1.5.8";
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.2.0"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.5.5"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.5.9"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.6.0"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.8.2"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.6.0"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "2.0.0"), is(true));
    }

    @Test
    public void testVersionCompareMajorVersionDiff() {
        String myVer = "3.5.5";
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "1.2.0"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "3.4.108"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "3.9.108"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "3.5.5"), is(false));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "3.5.6"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "3.6.5"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "4.5.5"), is(true));
        assertThat(VersionChecker.isAppOlderThanPeer(myVer, "4.5.4"), is(true));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<VersionChecker> cons = VersionChecker.class.getDeclaredConstructor();
        cons.setAccessible(true);
        VersionChecker r = cons.newInstance();
    }
}
