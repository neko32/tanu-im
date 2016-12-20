package org.tanuneko.im.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class SpeechUtil {

    private SpeechUtil() {
        throw new IllegalStateException("no use");
    }

    private static final Logger LOG = LoggerFactory.getLogger(SpeechUtil.class);

    public static void speech(String str, String appConfPath) {
        Thread th = new Thread(new Speech(str, appConfPath));
        th.start();
    }
}
