package org.tanuneko.im.net.util;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/17.
 */
public class NetworkIFUtilTest {

    @Test
    public void testNetworkIFList() throws SocketException {
        List<String> list = NetworkIFUtil.getNetworkIFList();
        assertThat(list, not(nullValue()));
        assertThat(list.size(), greaterThan(0));
    }

    @Test
    public void testFindPreferredNetIF() throws IOException {
        InetAddress inet = NetworkIFUtil.findPreferredNetIF("lo");
        assertThat(inet.getHostAddress(), is("127.0.0.1"));
        // if preferred one not found, then local one is chosen
        inet = NetworkIFUtil.findPreferredNetIF("no");
        assertThat(inet, not(nullValue()));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<NetworkIFUtil> cons = NetworkIFUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        NetworkIFUtil r = cons.newInstance();
    }
}
