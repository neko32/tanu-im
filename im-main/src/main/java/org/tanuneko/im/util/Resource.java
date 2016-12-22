package org.tanuneko.im.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by neko32 on 2016/09/10.
 */
@SuppressWarnings("ALL")
public class Resource {

    private static Properties sysProps = null;
    private static Properties appProps = null;
    public static final String RES_SYS_SERVER_PORT = "server.port";
    public static final String RES_SYS_PEER_PROBE_PORT = "peerprobe.port";
    public static final String RES_SYS_GENERIC_PORT = "generic.port";
    public static final String RES_USER_NAME = "user.userName";
    public static final String RES_GROUP_NAME = "user.groupName";
    public static final String RES_MAINPANEL_IMAGE = "user.mainpanel_image";
    public static final String RES_MESSAGE_LOG = "user.messageLog";
    public static final String RES_USER_ICON = "user.icon";
    public static final String RES_PREFERRED_IF = "user.preferredNetworkIF";
    public static final String RES_APP_LOG = "user.appLog";
    public static final String RES_LOCALE = "user.locale";
    public static final String RES_ATTACHMENTS_DIR = "user.attachment.attachmentsStore";
    public static final String RES_ATTAACHMENTS_OKOVERWRITE = "user.attachment.okToOverwrite";

    private Resource() {
        throw new IllegalStateException("Not used");
    }


    public static synchronized void initSysProperty(InputStream in) throws IOException {
        sysProps = new Properties();
        sysProps.load(in);
    }

    public static synchronized void initAppProperty(String resourcePath) throws IOException {
        appProps = new Properties();
        InputStream in = new FileInputStream(new File(resourcePath));
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");
        appProps.load(reader);
    }

    public static synchronized void initAppProperty(InputStream in) throws IOException {
        appProps = new Properties();
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");
        appProps.load(reader);
    }

    public static String getAppProperty(final String prop) {
        if(appProps == null)
            throw new IllegalStateException("Property has not been initialized. Call init() first");
        try {
            String replaced = UserHomeFilter.replaceUserHome(appProps.getProperty(prop), OSUtil.getOSType());
            return CurrentDirFilter.replaceCurrentDir(replaced);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static synchronized Properties getRawAppProperty() {
        return appProps;
    }

    public static String getProperty(final String prop) {
        if(sysProps == null)
            throw new IllegalStateException("Property has not been initialized. Call init() first");
        return sysProps.getProperty(prop);
    }

    public static void unload() {
        sysProps = null;
    }

    public static void unloadAppProperty() {
        appProps = null;
    }

}
