package org.tanuneko.im.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class AttachmentTest {

    @Test
    public void testAttachment() {
        String name = "ATTACHMENT";
        byte[] data = "TEST TEST DATA TEST".getBytes();
        Attachment a = new Attachment(name, data);
        assertThat(a.getFileName(), is(name));
        assertThat(a.getData(), is(data));
        assertThat(a.toString(), is(String.format("%s:%d", name, data.length)));
    }
}
