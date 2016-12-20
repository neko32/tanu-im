package org.tanuneko.im.netcmd;

import org.junit.Test;
import org.tanuneko.im.model.Generic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/18.
 */
public class AppJarTransferRequestTest {

    @Test
    public void testAppJarTransfer() throws NetCommandException {
        Generic req = new Generic(Generic.CMD_SEND_APP_JAR);
        AppJarTransferRequest appTransReq = new AppJarTransferRequest();
        appTransReq.setWorkDir("C:\\Users\\neko32\\tanu_imtest");
        Generic resp = appTransReq.handleCommand(req);
        assertThat(resp.getData().length, is(1729388));
        assertThat(resp.getCmd(), is(Generic.CMD_SEND_APP_JAR_RESP));
    }
}
