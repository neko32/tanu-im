package org.tanuneko.im.netcmd;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/18.
 */
public class NetCommandExceptionTest {

    @Test
    public void testConstructors() {
        String MSG = "errrr";
        String innerMSG = "err_IN";
        NetCommandException ex = new NetCommandException(MSG);
        assertThat(ex.getMessage(), is(MSG));
        ex = new NetCommandException(MSG, new Exception(innerMSG));
        assertThat(ex.getMessage(), is(MSG));
        assertThat(ex.getCause().getMessage(), is(innerMSG));
    }
}
