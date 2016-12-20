package org.tanuneko.im.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class AppStatusTest {

    @Test
    public void testEnum() {
        assertThat(AppStatus.APP_CLOSED.name(), is("APP_CLOSED"));
        assertThat(AppStatus.APP_CLOSING.name(), is("APP_CLOSING"));
        assertThat(AppStatus.APP_INITIALIZNG.name(), is("APP_INITIALIZNG"));
        assertThat(AppStatus.APP_NOT_STARTED.name(), is("APP_NOT_STARTED"));
        assertThat(AppStatus.APP_RUNNING.name(), is("APP_RUNNING"));
        assertThat(AppStatus.APP_SUSPEND.name(), is("APP_SUSPEND"));
    }
}
