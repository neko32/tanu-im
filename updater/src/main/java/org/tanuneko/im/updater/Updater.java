package org.tanuneko.im.updater;

import javax.swing.*;
import java.io.File;

/**
 * Created by neko32 on 2016/12/19.
 */
public class Updater {

    public static void main(String[] args) {
        System.out.println("args - len - " + args.length);
        for(String arg: args) {
            System.out.println("param:" + arg);
        }
        if(args.length != 4) {
            System.out.println("Usage: java org.tanuneko.im.updater.Updater [workdir] [upgrade key] [locale]");
            System.exit(1);
        }
        upgrade(args[1], UpgradeKey.valueOf(args[2]), args[3]);
    }

    public static void upgrade(String workDir, UpgradeKey upgradeKey, String locale) {
        String SEP = System.getProperty("file.separator");

        if(!waitMessagerPIDGone(5, 2000)) {
            System.out.println("Failed update - Messager is still running.");
            JOptionPane.showMessageDialog(null, "Failed update - Messager is still running", "Update Failure", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        if(upgradeKey != UpgradeKey.CONF_ONLY) {
            // upgrade jar
            File newFile = new File(workDir + SEP + "im-main-all-new.jar");
            File jarFile = new File(workDir + SEP + "im-main-all.jar");

            if(!newFile.exists()) {
                throw new IllegalStateException("Not found new jar");
            }

            if(!jarFile.delete()) {
                throw new IllegalStateException("Failed to delete " + jarFile.getAbsolutePath());
            }
            if(!newFile.renameTo(jarFile)) {
                throw new IllegalStateException("Failed to rename " + newFile.getName() + " to " + jarFile.getName());
            }
        }
    }

    public static boolean waitMessagerPIDGone(int maxRetry, int waitTime) {
        int retryCnt = 0;
        while(retryCnt++ < maxRetry) {
            if(!isPIDFileExist()) {
                return true;
            }
            try {
                Thread.sleep(waitTime);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean isPIDFileExist() {
        String SEP = System.getProperty("file.separator");
        String tmpDir = System.getProperty("java.io.tmpdir");
        File f = new File(tmpDir + SEP + "tanu-im");
        return f.exists();
    }
}

