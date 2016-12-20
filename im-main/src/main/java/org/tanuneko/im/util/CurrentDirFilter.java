package org.tanuneko.im.util;


/**
 * Created by neko32 on 2016/12/14.
 */
@SuppressWarnings("ALL")
public class CurrentDirFilter {

    public static final String CURRENT_DIR_KEY = "__CURRENT_DIR__";

    private CurrentDirFilter() {
        throw new IllegalStateException("no use");
    }

    public static String replaceCurrentDir(String path) {
        int loc = path.indexOf(CURRENT_DIR_KEY);
        if(loc == -1) {
            return path;
        }

        String userHome = System.getProperty("user.dir");
        return path.replace(CURRENT_DIR_KEY, userHome);
    }
}
