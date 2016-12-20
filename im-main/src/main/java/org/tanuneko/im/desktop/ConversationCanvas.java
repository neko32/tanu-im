package org.tanuneko.im.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.model.ConversationItem;
import org.tanuneko.im.model.Message;
import org.tanuneko.im.util.ImageUtil;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.UnicodeUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by neko32 on 2016/12/11.
 */
@SuppressWarnings("ALL")
public class ConversationCanvas extends JPanel {

    private static final Logger LOG = LoggerFactory.getLogger(ConversationCanvas.class);
    private java.util.List<ConversationItem> convHistory;
    private static final Color BACKGROUND_COLOR = new Color(168, 234, 230);
    private static final Color MY_CONV_COLOR = new Color(255, 235, 255);
    private static final Color OTHER_CONV_COLOR = new Color(231, 249, 226);
    private static final String LINE_SEP = System.getProperty("line.separator");
    private final MessageBoardPanel parent;
    private int totalY;

    private static Image localUserIcon;

    public ConversationCanvas(MessageBoardPanel parent) throws IOException {
        super();
        this.parent = parent;
        convHistory = new CopyOnWriteArrayList<>();
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(380, 120));
        ByteArrayInputStream bin = new ByteArrayInputStream(LocalUserBuilder.createOrGetLocalUser().getIcon());
        localUserIcon = ImageIO.read(bin);
        bin.close();
        totalY = 10;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int curX = 10;
        int curY = 10;
        int addY = 10;
        int textY = 0;
        int width;
        int rowSize;
        g2.setBackground(BACKGROUND_COLOR);
        g2.clearRect(0, 0, getWidth(), getHeight());
        Image userIcon;
        for(ConversationItem item: convHistory) {
            boolean isMyMessage = item.isMyMessage();
            addY = 18 * item.getRows();
            if(isMyMessage) {
                // draw icon
                userIcon = ImageUtil.getScaledImage(item.getUserIcon(), 30, 30);
                g2.drawImage(userIcon, curX, curY, null);
                curX += 35;

                // draw fukidashi Triangle
                g2.setColor(MY_CONV_COLOR);
                g2.fillPolygon(new int[]{curX - 5, curX, curX}, new int[]{curY + 7, curY + 3, curY + 10}, 3);
                g2.setColor(Color.BLACK);
                g2.drawPolygon(new int[]{curX - 5, curX, curX}, new int[]{curY + 7, curY + 3, curY + 10}, 3);

                // draw fukidashi Rectangle
                g2.setColor(MY_CONV_COLOR);
                rowSize = UnicodeUtil.isLikely2ByteString(item.getMessage()) ? 15 : 8;
                width = item.getCols() * rowSize;
                if(width >= 300) {
                    width = 300;
                }
                g2.fillRoundRect(curX, curY, width, addY, 15, 5);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(curX, curY, width, addY, 15, 5);
                /*
                g2.fillRoundRect(curX, curY, 300, addY, 15, 5);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(curX, curY, 300, addY, 15, 5);
                */

                // remove draw line of rectangle within fukidashi Triangle
                g2.setColor(MY_CONV_COLOR);
                g2.drawLine(curX, curY + 3, curX, curY + 10);

                // draw message
                g2.setColor(Color.BLUE);
                textY = curY + 14;
                String[] msgs = item.getMessage().split("\n");
                for(int i = 0;i < msgs.length;i++, textY += 15) {
                    g2.drawString(msgs[i], curX + 10, textY);
                }
            } else {
                // draw icon
                curX = 320;
                userIcon = ImageUtil.getScaledImage(item.getUserIcon(), 30, 30);
                g2.drawImage(userIcon, curX, curY, null);

                // draw fukidashi Triangle
                curX = 310;
                g2.setColor(OTHER_CONV_COLOR);
                g2.fillPolygon(new int[]{curX, curX, curX + 5}, new int[]{curY + 3, curY + 10, curY + 7}, 3);
                g2.setColor(Color.BLACK);
                g2.drawPolygon(new int[]{curX, curX, curX + 5}, new int[]{curY + 3, curY + 10, curY + 7}, 3);

                // draw fukidashi Rectangle
                g2.setColor(OTHER_CONV_COLOR);
                rowSize = UnicodeUtil.isLikely2ByteString(item.getMessage()) ? 15 : 8;
                width = item.getCols() * rowSize;
                if(width >= 300) {
                    width = 300;
                }
                curX = 310 - width;
                if(curX < 10) {
                    curX = 10;
                }
                g2.fillRoundRect(curX, curY, width, addY, 15, 5);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(curX, curY, width, addY, 15, 5);

                // remove draw line of rectangle within fukidashi Triangle
                curX = 310;
                g2.setColor(OTHER_CONV_COLOR);
                g2.drawLine(curX, curY + 3, curX, curY + 10);

                // draw message
                g2.setColor(Color.BLUE);
                String[] msgs = item.getMessage().split("\n");
                textY = curY + 14;
                for(int i = 0;i < msgs.length;i++, textY += 15) {
                    g2.drawString(msgs[i], curX - width + 5, textY);
                }
            }
            curX = 10;
            curY += addY + 15;
            totalY = curY;
        }
        LOG.debug("totalY:{} height:{}", totalY, getHeight());
    }

    public void add(Message msg) throws IOException {
        convHistory.add(new ConversationItem(msg));
    }

    public int getTotalY() {
        return totalY;
    }
}
