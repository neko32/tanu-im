package org.tanuneko.im.util;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class ResourceTest {

    @Test
    public void testAppResourceLoadByPath() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResource("conf/tanuim_test.properties").getPath());
        assertThat(Resource.getAppProperty(Resource.RES_USER_NAME), is("testman"));
        assertThat(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR), is(new String("C:\\dev\\tanu-im\\im-main\\tanu_imtest\\attachments".getBytes(), "UTF-8")));
    }

    @Test
    public void testAppResourceLoadByInStream() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        assertThat(Resource.getAppProperty(Resource.RES_USER_NAME), is("testman"));
        assertThat(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR), is(new String("C:\\dev\\tanu-im\\im-main\\tanu_imtest\\attachments".getBytes(), "UTF-8")));
    }

    @Test(expected=java.lang.Exception.class)
    public void testAppResourceException() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        Resource.getAppProperty(null);
    }

    @Test
    public void testSysResourceLoad() throws IOException {
        Resource.initSysProperty(ResourceTest.class.getClassLoader().getResourceAsStream("application.properties"));
        assertThat(Resource.getProperty(Resource.RES_SYS_SERVER_PORT), is("30388"));
    }

    @Test(expected=IllegalStateException.class)
    public void testUnloadSysProperty() throws IOException {
        Resource.initSysProperty(ResourceTest.class.getClassLoader().getResourceAsStream("application.properties"));
        assertThat(Resource.getProperty(Resource.RES_SYS_SERVER_PORT), is("30388"));
        Resource.unload();
        Resource.getProperty(Resource.RES_SYS_SERVER_PORT);
    }

    @Test(expected=IllegalStateException.class)
    public void testUnloadAppProperty() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        assertThat(Resource.getAppProperty(Resource.RES_USER_NAME), is("testman"));
        Resource.unloadAppProperty();
        Resource.getAppProperty(Resource.RES_USER_NAME);
    }

    @Test(expected=InvocationTargetException.class)
    public void testPrivateConstructor() throws Exception {
        Constructor<Resource> cons = Resource.class.getDeclaredConstructor();
        cons.setAccessible(true);
        Resource r = cons.newInstance();
    }

    @Test
    public void testGetRawProperty() throws IOException {
        Resource.initAppProperty(ResourceTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        Properties props = Resource.getRawAppProperty();
        assertThat(props, is(not(nullValue())));
        assertThat(props.getProperty(Resource.RES_USER_NAME), is("testman"));
    }
}
