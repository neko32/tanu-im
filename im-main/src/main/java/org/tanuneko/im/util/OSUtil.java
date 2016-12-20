package org.tanuneko.im.util;

import org.apache.commons.lang3.SystemUtils;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class OSUtil {

    private OSUtil() {
        throw new IllegalStateException("no access");
    }

    public static OSType getOSType() {
        if(SystemUtils.IS_OS_WINDOWS) {
            return OSType.WINDOWS;
        }
        else if(SystemUtils.IS_OS_LINUX) {
            return OSType.LINUX;
        }
        throw new RuntimeException("OS Type not supported");
    }
}
