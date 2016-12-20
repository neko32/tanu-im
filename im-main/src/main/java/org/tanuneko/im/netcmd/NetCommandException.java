package org.tanuneko.im.netcmd;

/**
 * Created by neko32 on 2016/12/18.
 */
public class NetCommandException extends Exception {

    public NetCommandException(String message) {
        super(message);
    }

    public NetCommandException(String message, Exception e) {
        super(message, e);
    }
}
