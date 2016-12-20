package org.tanuneko.im.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by neko32 on 2016/12/19.
 */
public class PIDUtil {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String SEP = System.getProperty("file.separator");
    private static final String NAME = "tanu-im";

    private PIDUtil() {
        throw new IllegalStateException("no use");
    }

    public static void createPID() throws IOException {
        File f = new File(TMP_DIR + SEP + NAME);
        if(!f.createNewFile()) {
            throw new IOException("Creating PID file failed");
        }
    }

    public static void removePID() throws IOException {
        File f = new File(TMP_DIR + SEP + NAME);
        if(!f.delete()) {
            throw new IOException("Creating PID file failed");
        }
    }

    public static boolean exists() {
        return new File(TMP_DIR + SEP + NAME).exists();
    }
}
