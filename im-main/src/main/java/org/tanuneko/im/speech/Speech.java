package org.tanuneko.im.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.util.OSType;
import org.tanuneko.im.util.OSUtil;

import java.io.IOException;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class Speech implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SpeechUtil.class);
    private String appConfPath;
    private String str;

    public Speech(String str, String appConfPath) {
        this.str = str;
        this.appConfPath = appConfPath;
    }

    @Override
    public void run() {
        if(OSUtil.getOSType() == OSType.WINDOWS) {
            str = "\"" + str + "\"";
            String path;
            if(appConfPath == null) {
                path = Speech.class.getClassLoader().getResource("vbs/speech.vbs").getPath().substring(1);
            } else {
                String sep = System.getProperty("file.separator");
                path = String.format("%s%s%s%s%s", appConfPath, sep, "vbs", sep, "speech.vbs");
            }
            try {
                LOG.info("Speech cmd:{}", String.format("cmd.exe /c %s %s", path, str));
                String cmd = String.format("cmd.exe /c %s %s", path, str);
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
