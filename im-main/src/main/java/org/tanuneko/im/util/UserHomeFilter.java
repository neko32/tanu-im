package org.tanuneko.im.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class UserHomeFilter {

    public static final String USER_HOME_KEY = "__USER_HOME__";

    private UserHomeFilter() {
        throw new IllegalStateException("no use");
    }

    public static String replaceUserHome(String path, OSType os) throws IOException, InterruptedException {
        int loc = path.indexOf(USER_HOME_KEY);
        if(loc == -1) {
            return path;
        }
        if(os == OSType.WINDOWS) {
            Process p = Runtime.getRuntime().exec("cmd.exe /c echo %USERPROFILE%");
            p.waitFor();
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String s;
            while((s = out.readLine()) != null) {
                sb.append(s);
            }
            return path.replace(USER_HOME_KEY, sb.toString());
        }
        else {
            return path;
        }
    }
}
