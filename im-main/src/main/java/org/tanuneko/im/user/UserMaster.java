package org.tanuneko.im.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neko32 on 2016/12/06.
 */
@SuppressWarnings("ALL")
public class UserMaster {

    private Map<String, UserEntry> users;
    private User localUser;
    private static final Logger LOG = LoggerFactory.getLogger(UserMaster.class);
    public static final int FOLLOWUP_NOOP = 0;
    public static final int FOLLOWUP_NEWUSER = -40;
    public static final int FOLLOWUP_REMOVEUSER = -50;

    public UserMaster(User localUser) {
        users = new HashMap<>();
        this.localUser = localUser;
        users.put(localUser.getUserName(), new UserEntry(localUser));
    }

    public int update(User user) {
        int retVal = FOLLOWUP_NOOP;
        if(user.equals(localUser)) {
            return retVal;
        }
        UserEntry userEnt = users.get(user.getUserName());
        if(userEnt == null) {
            LOG.info("Adding {} as a new user", user.getUserName());
            // request ICON as sending ICON data over UDP is size over

            users.put(user.getUserName(), new UserEntry(user));
            retVal = FOLLOWUP_NEWUSER;
        } else {
            if(user.getExtraInfo() != null && user.getExtraInfo().equals("BYE")) {
                users.remove(user.getUserName());
                retVal = FOLLOWUP_REMOVEUSER;
            }
        //    retVal = userEnt.increment();
        //    users.put(user.getUserName(), userEnt);
        }
        return retVal;
    }
}
