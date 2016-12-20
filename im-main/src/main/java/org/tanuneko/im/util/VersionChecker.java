package org.tanuneko.im.util;

/**
 * Created by neko32 on 2016/12/18.
 */
public class VersionChecker {

    private VersionChecker() {
        throw new IllegalStateException("no use");
    }

    public static boolean isAppOlderThanPeer(String myVersion, String otherVersion) {
        return convertVersionStringToInt(myVersion) < convertVersionStringToInt(otherVersion);
    }

    private static int convertVersionStringToInt(String version) {
        // major version will be 1000000 + num
        // minor version is 10000 + num
        String[] vers = version.split("\\.");
        if(vers.length != 3) {
            throw new IllegalStateException("Wrong version format");
        }
        int num = Integer.parseInt(vers[2]);
        num += Integer.parseInt(vers[1]) * 10000;
        num += Integer.parseInt(vers[0]) * 1000000;
        return num;
    }
}
