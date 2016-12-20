package org.tanuneko.im.model;

import org.tanuneko.im.util.LocalUserBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * Created by neko32 on 2016/12/11.
 */
@SuppressWarnings("ALL")
public class ConversationItem {

    private String userName;
    private String groupName;
    private Image userIcon;
    private Image stamp;
    private boolean isMyMessage;
    private String message;
    private int rows;
    private int cols;

    public ConversationItem(Message message) throws IOException {
        userName = message.getSenderName();
        InputStream in = new ByteArrayInputStream(message.getSenderImage());
        userIcon = ImageIO.read(in);
        groupName = message.getSenderGroupName();
        in.close();
        if(message.getStamp() != null) {
            in = new ByteArrayInputStream(message.getStamp());
            stamp = ImageIO.read(in);
        }
        this.message = message.getMessage();
        isMyMessage = userName.equals(LocalUserBuilder.createOrGetLocalUser().getUserName());
        StringTokenizer st = new StringTokenizer(this.message, System.getProperty("line.separator"));
        int maxRows = 0;
        int maxCols = 0;
        int colNum;
        while(st.hasMoreTokens()) {
            maxRows++;
            colNum = st.nextToken().length();
            maxCols = colNum > maxCols ? colNum : maxCols;
        }
        this.rows = maxRows;
        this.cols = maxCols;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Image getUserIcon() {
        return userIcon;
    }

    public Image getStamp() {
        return stamp;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public String getMessage() {
        return message;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String toString() {
        return "{userNmae:" + userName +
                ",groupName:" + groupName +
                ",message:" + message +
                ",isMyMessage:" + isMyMessage +
                ",msgRow:" + rows +
                ",msgCol:" + cols + "}";
    }
}
