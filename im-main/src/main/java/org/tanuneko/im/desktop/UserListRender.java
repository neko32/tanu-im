package org.tanuneko.im.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by neko32 on 2016/12/09.
 */
@SuppressWarnings("ALL")
public class UserListRender extends JLabel implements ListCellRenderer<User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserListRender.class);
    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    public UserListRender() {
        setOpaque(true);
        setIconTextGap(10);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            setIcon(user.getIconAsImageIcon());
        } catch(IOException ioE) {
            LOG.info(ioE.getMessage(), ioE);
        }
        setText(String.format("%s (%s)", user.getUserName(), user.getGroupName()));
        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return this;
    }
}
