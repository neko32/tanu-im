package org.tanuneko.im;

import org.apache.commons.io.FileUtils;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.StringResource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by neko32 on 2016/12/06.
 */
@SuppressWarnings("ALL")
public class AppRunner {

    private static int OLD_LOG_RETENTION = 10;
    private static String PROP_NAME = "\\conf\\tanuim.properties";
    private static String PROP_PATH = "conf/tanuim.properties";
    private static MessagerUI ui = null;

    public static void main(String[] args) throws IOException, AWTException {
        System.out.println("ARGS -len " + args.length);
        System.out.println("Current dir - " + System.getProperty("user.dir"));
        for(int i = 0;i < args.length;i++) {
            System.out.println("args[" + i + "]:" + args[i]);
        }

        Resource.initSysProperty(MessagerUI.class.getClassLoader().getResourceAsStream("application.properties"));
        if(args.length == 2) {
            Resource.initAppProperty(String.format("%s%s", args[1], PROP_NAME));
        } else {
            Resource.initAppProperty(MessagerUI.class.getClassLoader().getResource(PROP_PATH).getPath());
        }
        StringResource.init(Resource.getAppProperty(Resource.RES_LOCALE));
        System.setProperty("LOG_DIR", Resource.getAppProperty(Resource.RES_APP_LOG));
        createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_APP_LOG));
        createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_MESSAGE_LOG));
        createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR));
        purgeOldLogs(Resource.getAppProperty(Resource.RES_APP_LOG));

        // args 0 .. main class name
        // args 1 .. confpath
        if(ui == null) {
            ui = new MessagerUI(args.length == 2 ? args[1] : null);
        }
        ui.initialize();
    }

    public static void setMessagerUI(MessagerUI uiRef) {
        ui = uiRef;
    }

    public static void createDirectoryIfNotExists(String path) throws IOException {
        File f = new File(path);
        if(!f.exists()) {
            FileUtils.forceMkdir(new File(path));
        }
    }

    public static void purgeOldLogs(String logDir) {
        System.out.println(String.format("Finding old logs - retention policy is %d", OLD_LOG_RETENTION));
        File logDirFile = new File(logDir);
        for(File f: logDirFile.listFiles()) {
            LocalDate fileDate = Instant.ofEpochMilli(f.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate curDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
            if(ChronoUnit.DAYS.between(fileDate, curDate) > OLD_LOG_RETENTION) {
                System.out.println(String.format("Deleting %s", f.getAbsolutePath()));
                f.delete();
            }
        }
    }

    public static void setOldLogRetention(int retention) {
        OLD_LOG_RETENTION = retention;
    }

    public static void setPropName(String newPropName) {
        PROP_NAME = newPropName;
    }

    public static void setPropPath(String newPropPath) {
        PROP_PATH = newPropPath;
    }
}
