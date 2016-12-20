package org.tanuneko.im.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class GenericTest {

    @Test
    public void testDataModelWithDefaultConstructor() {
        String extra = "EXTRA";
        byte[] data = "TEST TEST T".getBytes();
        Generic g = new Generic();
        g.setData(data);
        g.setExtra(extra);
        assertThat(g.getData(), is(data));
        assertThat(g.getExtra(), is(extra));
        assertThat(g.getCmd(), is(Generic.CMD_NOOP));
        String expectedStr = String.format("Generic{cmd='%s', extra='%s'}", Generic.CMD_NOOP, extra);
        assertThat(g.toString(), is(expectedStr));
    }

    @Test
    public void testDataModel() {
        String extra = "EXTRA";
        byte[] data = "TEST TEST T".getBytes();
        Generic g = new Generic(Generic.CMD_SEND_USER_ICON);
        g.setData(data);
        g.setExtra(extra);
        assertThat(g.getData(), is(data));
        assertThat(g.getExtra(), is(extra));
        assertThat(g.getCmd(), is(Generic.CMD_SEND_USER_ICON));
        String expectedStr = String.format("Generic{cmd='%s', extra='%s'}", Generic.CMD_SEND_USER_ICON, extra);
        assertThat(g.toString(), is(expectedStr));
        g.setCmd(Generic.CMD_SEND_USER_ICON_RESP);
        assertThat(g.getCmd(), is(not(Generic.CMD_SEND_USER_ICON)));
    }
}
