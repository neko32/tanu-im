package org.tanuneko.im.netcmd;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tanuneko.im.model.Generic;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/18.
 */
public class UserIconRequestTest {

    @BeforeClass
    public static void setupClass() throws IOException {
        Resource.initAppProperty(UserIconRequestTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        StringResource.init("en");
        LocalUserBuilder.clearCache();
    }

    @Test
    public void testCommand() throws NetCommandException, IOException {
        Generic userIconReq = new Generic(Generic.CMD_SEND_USER_ICON);
        UserIconRequest reqHandler = new UserIconRequest();
        Generic resp = reqHandler.handleCommand(userIconReq);
        ByteArrayInputStream bin = new ByteArrayInputStream(resp.getData());
        Image img = ImageIO.read(bin);
        bin.close();
        assertThat(img.getWidth(null), is(64));
        assertThat(img.getHeight(null), is(64));
        assertThat(resp.getCmd(), is(Generic.CMD_SEND_USER_ICON_RESP));
    }
}
