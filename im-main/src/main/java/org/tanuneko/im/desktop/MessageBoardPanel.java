package org.tanuneko.im.desktop;

import org.tanuneko.im.model.Message;
import org.tanuneko.im.util.Resource;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by neko32 on 2016/09/11.
 */
@SuppressWarnings("ALL")
public class MessageBoardPanel extends JPanel {

    private JTextArea textArea;
    private ConversationCanvas conversation;
    private JScrollPane pane;
    private StringBuilder buffer;

    public MessageBoardPanel() throws IOException {
        setLayout(new BorderLayout());
        textArea = new JTextArea(400, 20);
        conversation = new ConversationCanvas(this);
        conversation.setBackground(new Color(168, 234, 230));
    //    conversation.setPreferredSize(new Dimension(380, 120));
        conversation.setPreferredSize(new Dimension(380, 5000));
        //JScrollPane convScroll = new JScrollPane(conversation);

        pane = new JScrollPane(conversation, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setWheelScrollingEnabled(true);
        pane.setPreferredSize(new Dimension(380, 120));
        pane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        //pane = new JScrollPane(textArea);
        pane.setBackground(Color.RED);
        buffer = new StringBuilder();
    }

    public void init() {
        add(pane, BorderLayout.CENTER);
    }

    public String getText() {
        buffer.delete(0, buffer.length());
        buffer.append(textArea.getText());
        return buffer.toString();
    }

    public void drawLatestMessage() {
        conversation.repaint();
    }

    public void scroll(int y) {
        pane.getVerticalScrollBar().setValue(y);
    }

    public void addMessage(Message msg) throws IOException {
        conversation.add(msg);
        logMessage(msg.getStringForMessageLog());
        if(conversation.getTotalY() < 100) {
            conversation.repaint();
        } else {
            scroll(conversation.getTotalY() - 45);
        }
    }

    public void logMessage(final String msg) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date(System.currentTimeMillis());
        File f = new File(String.format("%s%smessagelog_%s.txt", Resource.getAppProperty(Resource.RES_MESSAGE_LOG), File.separator, sdf.format(d)));
        try(FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            fw.write(msg);
        }
    }

}
