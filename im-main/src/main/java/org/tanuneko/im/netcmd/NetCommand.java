package org.tanuneko.im.netcmd;

import org.tanuneko.im.model.Generic;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public interface NetCommand {

    Generic handleCommand(Generic genericData) throws NetCommandException;

}
