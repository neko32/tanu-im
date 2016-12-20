package org.tanuneko.im.model;

import java.io.Serializable;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class Generic implements Serializable {

    public static final String CMD_NOOP = "NOOP";
    public static final String CMD_SEND_USER_ICON = "SEND_USER_ICON";
    public static final String CMD_SEND_USER_ICON_RESP = "SEND_USER_ICON_RESP";
    public static final String CMD_SEND_APP_JAR = "SEND_APP_JAR";
    public static final String CMD_SEND_APP_JAR_RESP = "SEND_APP_JAR_RESP";

    private String cmd;
    private byte[] data;
    private String extra;

    public Generic() {
        cmd = CMD_NOOP;
    }

    public Generic(String cmd) {
        this.cmd = cmd;
        data = null;
        extra = null;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Generic{" +
                "cmd='" + cmd + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
