package org.tanuneko.im.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/16.
 */
public class NoticePreferenceTest {

    @Test
    public void testEnum() {
        assertThat(NoticePreference.NOTICE_BY_BALOON.name(), is("NOTICE_BY_BALOON"));
        assertThat(NoticePreference.NOICE_BY_MESSAGEBOX.name(), is("NOICE_BY_MESSAGEBOX"));
    }
}
