package org.tanuneko.im.netcmd;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.Generic;

import java.io.File;
import java.io.IOException;

/**
 * Created by neko32 on 2016/12/18.
 */
public class AppJarTransferRequest implements NetCommand {

    private static final Logger LOG = LoggerFactory.getLogger(AppJarTransferRequest.class);
    private String workDirCache = null;

    @Override
    public Generic handleCommand(Generic genericData) throws NetCommandException {
        Generic resp = new Generic();
        resp.setCmd(Generic.CMD_SEND_APP_JAR_RESP);
        String sep = System.getProperty("file.separator");
        File jar;
        if(workDirCache == null) {
            String workdir = System.getProperty("user.dir");
            jar = new File(workdir + sep + "im-main-all.jar");
        }
        else {
            jar = new File(workDirCache + sep + "im-main-all.jar");
        }
        resp.setData(null);
        if(jar.exists()) {
            try {
                byte[] data = FileUtils.readFileToByteArray(jar);
                resp.setData(data);
                LOG.info("Set this jar byte data[{} bytes]. Ready to send back..", data.length);
                // calc PGP and set to
            } catch (IOException e) {
                throw new NetCommandException(e.getMessage(), e);
            }
        }

        return resp;
    }

    public void setWorkDir(String workDir) {
        this.workDirCache = workDir;
    }
}
