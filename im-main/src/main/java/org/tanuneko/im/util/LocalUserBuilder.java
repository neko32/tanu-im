package org.tanuneko.im.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.User;
import org.tanuneko.im.net.util.NetworkIFUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by neko32 on 2016/09/10.
 */
@SuppressWarnings("ALL")
public class LocalUserBuilder {

    private static Logger LOG = LoggerFactory.getLogger(LocalUserBuilder.class);
    private static User localUserCache = null;

    private LocalUserBuilder() {
        throw new IllegalStateException("no use");
    }

    public static synchronized User createOrGetLocalUser() throws IOException {
        if(localUserCache != null) {
            return localUserCache;
        }
        String name = Resource.getAppProperty(Resource.RES_USER_NAME);
        String group = Resource.getAppProperty(Resource.RES_GROUP_NAME);
        String preferredIF = Resource.getAppProperty(Resource.RES_PREFERRED_IF);
        String iconPath = Resource.getAppProperty(Resource.RES_USER_ICON);
        byte[] icon = FileUtils.readFileToByteArray(new File(iconPath));
        LOG.info("Preferred Network Interface is {}", preferredIF);
        int id = SimpleIDGen.generateID();
        InetAddress inet;
        if(preferredIF.equalsIgnoreCase("AUTO")) {
            inet = InetAddress.getLocalHost();
        } else {
            inet = NetworkIFUtil.findPreferredNetIF(preferredIF);
        }
        String hostName = InetAddress.getLocalHost().getHostName();
        User user = new User(id, name, group, hostName, icon, inet);
        localUserCache = user;
        return user;
    }

    public static void clearCache() {
        localUserCache = null;
    }
}
