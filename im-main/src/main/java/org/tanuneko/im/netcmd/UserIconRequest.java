package org.tanuneko.im.netcmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.Generic;
import org.tanuneko.im.model.User;
import org.tanuneko.im.util.LocalUserBuilder;

import java.io.IOException;

/**
 * Created by neko32 on 2016/12/18.
 */
public class UserIconRequest implements NetCommand {

    private static final Logger LOG = LoggerFactory.getLogger(UserIconRequest.class);

    @Override
    public Generic handleCommand(Generic genericData) throws NetCommandException {
        try {
            User user = LocalUserBuilder.createOrGetLocalUser();
            Generic resp = new Generic(Generic.CMD_SEND_USER_ICON_RESP);
            resp.setData(user.getIcon());
            LOG.info("This user[{}]'s Icon is set to generic data packet. Ready to send back..", user.getUserName());
            return resp;
        } catch(IOException ioE) {
            throw new NetCommandException(ioE.getMessage(), ioE);
        }
    }
}
