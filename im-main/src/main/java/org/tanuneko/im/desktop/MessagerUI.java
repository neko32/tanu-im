package org.tanuneko.im.desktop;

import net.iharder.dnd.FileDrop;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.configdlg.ConfigDialog;
import org.tanuneko.im.model.*;
import org.tanuneko.im.net.*;
import org.tanuneko.im.speech.SpeechUtil;
import org.tanuneko.im.user.UserEntry;
import org.tanuneko.im.user.UserMaster;
import org.tanuneko.im.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by neko32 on 2016/12/06.
 */
@SuppressWarnings("ALL")
public class MessagerUI extends JFrame implements WindowListener, ActionListener {

    public static final Logger LOG = LoggerFactory.getLogger(MessagerUI.class);

    // app conf path
    private String appConfPath = null;

    // local user
    User localUser;

    // app status
    private static AppStatus APP_STATUS = AppStatus.APP_NOT_STARTED;

    // user master
    private UserMaster userMaster;

    // parts
    private TrayIcon tray;
    private MessageBoardPanel messageBoardPanel;
    private MorinokoPanel morinokoPanel;
    private JButton sendBtn;
    private JButton refreshBtn;
    private JTextArea messageInput;
    private DefaultListModel<User> userWithIconListModel;
    private JList userListWithIcon;
    private JLabel attachmentsNotice;

    // attachments
    private java.util.List<File> attachments = null;
    private String attachmentStoreCache = null;

    // local service
    private PeerProbeReceiver recv;
    private PeerProbeSender sender;
    private MessageReceiver msgRecv;
    private GenericDataReceiver genericRecv;

    public MessagerUI(String appConfPath) throws IOException {
        this.appConfPath = appConfPath;
        attachments = new ArrayList<>();
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public void initialize() throws IOException, AWTException {
        APP_STATUS = AppStatus.APP_INITIALIZNG;

        try {
            if(!PIDUtil.exists()) {
                PIDUtil.createPID();
            }

            // Init Tray Icon
            initIconTray();

            // Init local user
            localUser = LocalUserBuilder.createOrGetLocalUser();
            userMaster = new UserMaster(localUser);

            // set layout manager
            setVisible(false);
            initLayout();
            setResizable(false);

            // initialize components
            initMorinokoPane();
            initUserListPane();
            initMessageBoard();
            initIconField();
        } catch(Exception e) {
            LOG.error(String.format("Initialization failed[%s] - exiting", e.getMessage()), e);
            System.exit(1);
        }

        // Start Peer Probe Recv and Sender
        recv = new PeerProbeReceiver(this, Integer.parseInt(Resource.getProperty(Resource.RES_SYS_PEER_PROBE_PORT)));
        sender = new PeerProbeSender(getLocalNetwork(), Integer.parseInt(Resource.getProperty(Resource.RES_SYS_PEER_PROBE_PORT)));
        msgRecv = new MessageReceiver(this, Integer.parseInt(Resource.getProperty(Resource.RES_SYS_SERVER_PORT)));
        genericRecv = new GenericDataReceiver(this, Integer.parseInt(Resource.getProperty(Resource.RES_SYS_GENERIC_PORT)));
        recv.start();
        sender.start();
        msgRecv.start();
        genericRecv.start();

        // set exit behavior
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image img = ImageIO.read(MessagerUI.class.getClassLoader().getResourceAsStream("images/icon/icon.png"));
        setIconImage(img);
        addWindowListener(this);
        setTitle(String.format("%s %s", StringResource.get(StringResource.STR_MAINWND_TITLE), StringResource.get(StringResource.STR_VERSION)));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        setVisible(true);
        morinokoPanel.resizeImage(morinokoPanel.getWidth(), morinokoPanel.getHeight());
        APP_STATUS = AppStatus.APP_RUNNING;
    }

    private void initLayout() {
        setSize(new Dimension(400, 600));
        setLayout(new GridLayout(4, 1));
    }

    private void initMorinokoPane() throws IOException {
        morinokoPanel = new MorinokoPanel();
        morinokoPanel.init();
        morinokoPanel.setPreferredSize(new Dimension(400, 130));
        getContentPane().add(morinokoPanel);
    }

    private void initUserListPane() throws IOException {
        userListWithIcon = new JList();
        userWithIconListModel = new DefaultListModel<>();
        //noinspection unchecked
        userListWithIcon.setCellRenderer(new UserListRender());
        userWithIconListModel.add(0, localUser);
        //noinspection unchecked
        userListWithIcon.setModel(userWithIconListModel);
     //   userListWithIcon.setBackground(UIManager.getColor("Panel.background"));

        JScrollPane pane = new JScrollPane(userListWithIcon);
        pane.setPreferredSize(new Dimension(400, 130));
        getContentPane().add(new JScrollPane(userListWithIcon));

    }

    public void updateUserList(User user) {
        int followupCode = userMaster.update(user);
        if(followupCode == UserMaster.FOLLOWUP_NEWUSER) {
            // request user icon
            GenericDataSender sender = new GenericDataSender(user.getIpAddress(), Integer.parseInt(Resource.getProperty(Resource.RES_SYS_GENERIC_PORT)));
            try {
                Generic userIconResp = sender.send(new Generic(Generic.CMD_SEND_USER_ICON));
                LOG.info("Received userIcon size:{} bytes", userIconResp.getData().length);
                user.setIcon(userIconResp.getData());
            } catch(IOException|ClassNotFoundException e) {
                LOG.error(e.getMessage(), e);
            }
            userWithIconListModel.addElement(user);
        }
        else if(followupCode == UserEntry.CTR_LEAVE || followupCode == UserMaster.FOLLOWUP_REMOVEUSER) {
            userWithIconListModel.removeElement(user);
        }
    }

    private Collection<User> buildSelfUser() throws IOException {
        Collection<User> user = new ArrayList<>();
        user.add(LocalUserBuilder.createOrGetLocalUser());
        return user;
    }

    private void initMessageBoard() throws IOException {
        messageBoardPanel = new MessageBoardPanel();
        messageBoardPanel.init();
        messageBoardPanel.setPreferredSize(new Dimension(400, 130));
        getContentPane().add(messageBoardPanel);
    }

    private void initIconField() throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        // upper panel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        attachmentsNotice = new JLabel(StringResource.get(StringResource.STR_ATTACHMENT_DEFAULT_MSG));
        attachmentsNotice.setIcon(new ImageIcon(ImageIO.read(MessagerUI.class.getClassLoader().getResourceAsStream("images/panel/drag_drop.png"))));
        //upperPanel.add
        new FileDrop(attachmentsNotice, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                StringBuilder sb = new StringBuilder();
                for(int i = 0;i < files.length;i++) {
                    attachments.add(files[i]);
                    sb.append(String.format("%s,", files[i].getName()));
                }
                setAttachmentsText(sb.toString().substring(0, sb.length() - 1));
            }
        });
        upperPanel.add(attachmentsNotice);
        upperPanel.setPreferredSize(new Dimension(400, 20));

        //lower panel
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        sendBtn = new JButton();
        sendBtn.setIcon(new ImageIcon(ImageIO.read(MessagerUI.class.getClassLoader().getResourceAsStream("images/panel/send_btn.png"))));
        sendBtn.setMnemonic(KeyEvent.VK_S);
        sendBtn.addActionListener(this);

        messageInput = new JTextArea(3, 25);
        JScrollPane messageInputScroll = new JScrollPane(messageInput);
        messageInputScroll.setPreferredSize(new Dimension(300, 60));
        lowerPanel.add(messageInputScroll);
        lowerPanel.add(sendBtn);
        lowerPanel.setPreferredSize(new Dimension(400, 80));
        panel.add(upperPanel);
        panel.add(lowerPanel);

        getContentPane().add(panel);
    }

    public void initIconTray() throws AWTException, IOException {
        Image img = ImageIO.read(MessagerUI.class.getClassLoader().getResourceAsStream("images/tray/tasktray.gif"));
        if(SystemTray.isSupported()) {
            tray = new TrayIcon(img);
            SystemTray sysTray = SystemTray.getSystemTray();
            tray.setImageAutoSize(true);
            LOG.info("SystemTray is supported? - {}", SystemTray.isSupported());

            PopupMenu popup = new PopupMenu();
            MenuItem configMenu = new MenuItem(StringResource.get(StringResource.MENU_CONFIG));
            configMenu.addActionListener(this);
            MenuItem exitMenu = new MenuItem(StringResource.get(StringResource.MENU_EXIT));
            exitMenu.addActionListener(this);
            popup.add(configMenu);
            popup.addSeparator();
            popup.add(exitMenu);
            tray.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    showWindow();
                }
            });
            tray.setPopupMenu(popup);
            sysTray.add(tray);
        }
    }

    public void notifyUser(String message, String title, int noticeType) {
        JOptionPane.showMessageDialog(null, message, title, noticeType);
    }

    public void handleReceivedMessage(Message message) throws IOException {
        LOG.info("Received message:{}", message.toString());

        // processing text message
        if(!StringUtils.isEmpty(message.getMessage()) || (message.getAttachments() != null && message.getAttachments().size() > 0)) {
            message.updateMessageIfAttachmentExists(false);
            messageBoardPanel.addMessage(message);
            SpeechUtil.speech(String.format(StringResource.get(StringResource.SPEECH_RECEIVED_MSG), message.getSenderName(), message.getMessage()), appConfPath);
        }

        // processing attachment(s) if sent
        if(message.getAttachments() != null) {
            if(attachmentStoreCache == null) {
                attachmentStoreCache = Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR);
            }

            final String sep = System.getProperty("file.separator");
            final boolean okToOverwrite = Boolean.parseBoolean(Resource.getAppProperty(Resource.RES_ATTAACHMENTS_OKOVERWRITE));
            StringBuilder sb = new StringBuilder();
            int ctr = 1;
            for(Attachment attachment: message.getAttachments()) {
                File dest = new File(attachmentStoreCache + sep + attachment.getFileName());
                if(dest.exists() && !okToOverwrite) {
                    // TODO - implement yes no action
                    sb.append(String.format(StringResource.get(StringResource.NOTICE_NOSAVE_DUETO_SAMEFILE), ctr++, attachment.getFileName(), attachmentStoreCache));
                    continue;
                }
                FileUtils.writeByteArrayToFile(dest, attachment.getData());
                sb.append(String.format(StringResource.get(StringResource.NOTICE_ATTACHMENT_SAVED), ctr++, attachment.getFileName(), attachmentStoreCache));
            }
        }
        showMessageArrivalNotification(message, attachments, NoticePreference.NOTICE_BY_BALOON);
    }

    public void promptUpdate(InetAddress peerAddr, String myVersion, String peerVersion) {
        notifyUser(String.format(StringResource.get(StringResource.WARNING_VERSION_OLD_MESSAGE), myVersion, peerVersion), StringResource.get(StringResource.WARNING_VERSION_OLD_SUBJECT), JOptionPane.WARNING_MESSAGE);
        Generic req = new Generic();
        req.setCmd(Generic.CMD_SEND_APP_JAR);
        String workdir = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");
        try {
            Generic resp = new GenericDataSender(peerAddr, Integer.parseInt(Resource.getProperty(Resource.RES_SYS_GENERIC_PORT))).send(req);
            File jar = new File(workdir + sep + "im-main-all-new.jar");
            FileUtils.writeByteArrayToFile(jar, resp.getData());
        } catch (IOException|ClassNotFoundException e) {
            LOG.error("Failed to update application.", e);
        }
        try {
            windowClosing(null);
        } finally {
            try {
                String ext = OSUtil.getOSType() == OSType.WINDOWS ? ".bat" : ".sh";
                LOG.info("Executing {}", String.format("%s%s%s %s %s %s", workdir, sep, "updater" + ext, "JAR_ONLY", Resource.getAppProperty(Resource.RES_LOCALE)));
                Process p = Runtime.getRuntime().exec(workdir + sep + "updater" + ext, new String[]{"JAR_ONLY", Resource.getAppProperty(Resource.RES_LOCALE)});
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        System.exit(0);
    }

    private void showMessageArrivalNotification(Message message, java.util.List<File> attachments, NoticePreference noticePref) {
        StringBuilder sb = new StringBuilder();
        String subject = "";
        boolean msgExists = !StringUtils.isEmpty(message.getMessage());
        boolean attachmentExists = attachments != null && attachments.size() > 0;
        if(msgExists && attachmentExists) {
            sb.append(String.format(StringResource.get(StringResource.TRAY_NOTICE_MESSAGE_AND_ATTACHMENT_RECEIVED_MESSAGE), message.getSenderName(), attachments.size()));
            subject = StringResource.get(StringResource.TRAY_NOTICE_MESSAGE_AND_ATTACHMENT_RECEIVED_SUBJECT);
        } else if(msgExists && !attachmentExists) {
            sb.append(String.format(StringResource.get(StringResource.TRAY_NOTICE_MESSAGE_RECEIVED_MESSAGE), message.getSenderName()));
            subject = StringResource.get(StringResource.TRAY_NOTICE_MESSAGE_RECEIVED_SUBJECT);
        } else if(!msgExists && attachmentExists) {
            sb.append(String.format(StringResource.get(StringResource.TRAY_NOTICE_ATTACHMENT_RECEIVED_MESSAGE), message.getMessage(), attachments.size()));
            subject = StringResource.get(StringResource.TRAY_NOTICE_ATTACHMENT_RECEIVED_SUBJECT);
        }
        if(sb.length() != 0) {
            tray.displayMessage(sb.toString(), subject, TrayIcon.MessageType.INFO);
        }
    }

    public void setAttachmentsText(String text) {
        attachmentsNotice.setText(text.length() > 32 ? text.substring(0, 32) + "..(" + attachments.size() + ")" : text);
    }

    public String getLocalNetwork() throws IOException {
        User localUser = LocalUserBuilder.createOrGetLocalUser();
        String localIP = localUser.getIpAddress().getHostAddress();
        return localIP.substring(0, localIP.lastIndexOf(".")) + ".255";
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);

        if(recv != null) {
            recv.setContinue(false);
            recv.interrupt();
        }
        int retry = 0;
        while(retry++ < 3) {
            try {
                if(recv.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
        if(sender != null) {
            sender.setContinue(false);
            sender.interrupt();
        }
        retry = 0;
        while(retry++ < 3) {
            try {
                if(sender.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
        if(msgRecv != null) {
            msgRecv.setContinue(false);
            msgRecv.shutdown();
        }
        retry = 0;
        while(retry++ < 3) {
            try {
                if(msgRecv.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
        if(genericRecv != null) {
            genericRecv.setContinue(false);
            genericRecv.shutdown();
        }
        retry = 0;
        while(retry++ < 3) {
            try {
                if(genericRecv.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }

        try {
            PeerByeSender byeSender = new PeerByeSender(getLocalNetwork(), Integer.parseInt(Resource.getProperty(Resource.RES_SYS_PEER_PROBE_PORT)));
            byeSender.sendBye();
        } catch(IOException ioE) {
            LOG.error(ioE.getMessage(), ioE);
        }
        SystemTray.getSystemTray().remove(tray);

        LOG.info("Both probe sender and probe recv and MessageReceiver are shut down successfully");
        try {
            PIDUtil.removePID();
        } catch(IOException ioE) {
            LOG.error(ioE.getMessage(), ioE);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // ******************************
        // Send Btn
        // ******************************
        if(source == sendBtn) {
            int row = userListWithIcon.getSelectedIndex();
            if(row == -1) {
                LOG.warn("None is selected.");
                notifyUser(StringResource.get(StringResource.WARNING_NOUSER_SELECTED_MESSAGE), StringResource.get(StringResource.WARNING_NOUSER_SELECTED_SUBJECT), JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(StringUtils.isEmpty(messageInput.getText()) && attachments.size() == 0) {
                LOG.warn("Message is empty.");
                notifyUser(StringResource.get(StringResource.WARNING_NOITEM_TO_SEND_MESSAGE), StringResource.get(StringResource.WARNING_NOITEM_TO_SEND_SUBJECT), JOptionPane.WARNING_MESSAGE);
                return;
            }

            User selectedUser = userWithIconListModel.get(row);
            String toUserName = selectedUser.getUserName();
            String toIPAddr = selectedUser.getIpAddress().getHostAddress();
            if(toUserName.equals(localUser.getUserName())) {
                toIPAddr = "localhost";
            }
            Message message = buildMessage(toUserName, selectedUser.getIpAddress(), localUser.getGroupName());

            try {
                MessageSender sender = new MessageSender(InetAddress.getByName(toIPAddr), Integer.parseInt(Resource.getProperty(Resource.RES_SYS_SERVER_PORT)));
                sender.send(message);
                if (!toUserName.equals(localUser.getUserName())) {
                    message.updateMessageIfAttachmentExists(true);
                    messageBoardPanel.addMessage(message);
                }
                messageInput.setText("");
                attachments.clear();
                attachmentsNotice.setText(StringResource.get(StringResource.STR_ATTACHMENT_DEFAULT_MSG));
            } catch (IOException e1) {
                LOG.error(e1.getMessage(), e1);
            }
        }

        // ******************************
        // Exit Menu
        // ******************************
        if(source instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) source;
            if(menuItem.getLabel().equals(StringResource.get(StringResource.MENU_CONFIG))) {
                try {
                    ConfigDialog cd = new ConfigDialog(this, "Config");
                    if(ConfigDialog.isUpdated) {
                        notifyUser(StringResource.get(StringResource.WARNING_SHUTDOWN_AFTER_CONFIGUPD_MESSAGE), StringResource.get(StringResource.WARNING_SHUTDOWN_AFTER_CONFIGUPD_SUBJECT), JOptionPane.WARNING_MESSAGE);
                        windowClosing(null);
                        System.exit(0);
                    }
                } catch(SocketException sE) {
                    LOG.error(sE.getMessage(), sE);
                    notifyUser(StringResource.get(StringResource.WARNING_NETWORK_IF_RETRIEVAL_FAIL_MESSAGE), StringResource.get(StringResource.WARNING_NETWORK_IF_RETRIEVAL_FAIL_SUBJECT), JOptionPane.ERROR_MESSAGE);
                }
            } else if(menuItem.getLabel().equals(StringResource.get(StringResource.MENU_EXIT))) {
                windowClosing(null);
                System.exit(0);
            }
        }
    }

    private Message buildMessage(String receiverName, InetAddress receiverAddress, String receiverGroupName) {
        Message message = new Message(localUser.getUserName(), localUser.getIpAddress(), localUser.getGroupName(), receiverName, receiverAddress, receiverGroupName, messageInput.getText());
        message.setSenderImage(localUser.getIcon());
        if(attachments.size() >= 1) {
            java.util.List<Attachment> transferData = attachments.stream().
                    map(f -> {
                        try {
                            return new Attachment(f.getName(), FileUtils.readFileToByteArray(f));
                        } catch (IOException ioE) {
                            throw new UncheckedIOException(ioE);
                        }
                    }).collect(Collectors.toList());
            message.setAttachments(transferData);
        }
        return message;
    }

    public void showWindow() {
        if(getState() == Frame.ICONIFIED) {
            setState(Frame.NORMAL);
        }
    }

    public String getAppConfPath() {
        return appConfPath;
    }
}
