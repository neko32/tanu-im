package org.tanuneko.im.configdlg;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.net.util.NetworkIFUtil;
import org.tanuneko.im.util.ConfigValidator;
import org.tanuneko.im.util.Resource;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.Properties;

/**
 * Created by neko32 on 2016/12/13.
 */
@SuppressWarnings("ALL")
public class ConfigDialog extends JDialog implements ActionListener, MouseListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigDialog.class);
    private JTextField userNameInput;
    private JTextField groupNameInput;
    private JLabel userIconLoc;
    private JLabel messageLogLoc;
    private JLabel appLogLoc;
    private JComboBox<String> locale;
    private JComboBox<String> networkIF;
    private JLabel attachmentStoreLoc;
    private JComboBox<String> attachmentOverwriteOK;
    private JButton okBtn;
    private JButton cancelBtn;
    public static boolean isUpdated = false;

    public ConfigDialog(JFrame parent, String title) throws SocketException {
        super(parent, title, true);
        setSize(400, 600);
        Point prtLoc = parent.getLocation();
        setLocation((int)prtLoc.getX(), (int)prtLoc.getY());
        setResizable(false);

        setLayout(new GridLayout(10, 2));
        JLabel userNameLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_USERNAME));
        userNameLabel.setPreferredSize(new Dimension(100, 15));
        JLabel groupNameLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_GROUPNAME));
        groupNameLabel.setPreferredSize(new Dimension(100, 15));
        JLabel userIconLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_USERICON));
        userIconLabel.setPreferredSize(new Dimension(100, 15));
        JLabel messageLogLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_MSGLOG));
        messageLogLabel.setPreferredSize(new Dimension(100, 15));
        JLabel appLogLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_APPLOG));
        appLogLabel.setPreferredSize(new Dimension(100, 15));
        JLabel localeLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_LOCALE));
        localeLabel.setPreferredSize(new Dimension(100, 15));
        JLabel preferredNetworkIFLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_PREFERRED_NETWORK_IF));
        preferredNetworkIFLabel.setPreferredSize(new Dimension(100, 15));
        JLabel attachmentStoreLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_ATTACHMENT_STORE));
        attachmentStoreLabel.setPreferredSize(new Dimension(100, 15));
        JLabel okToOverwriteLabel = new JLabel(StringResource.get(StringResource.CONFIGDLG_ATTACHMENT_OVERWRITE));
        okToOverwriteLabel.setPreferredSize(new Dimension(100, 15));

        userNameInput = new JTextField();
        userNameInput.setPreferredSize(new Dimension(300, 15));
        groupNameInput = new JTextField();
        groupNameInput.setPreferredSize(new Dimension(300, 15));
        userIconLoc = new JLabel();
        userIconLoc.setPreferredSize(new Dimension(300, 15));
        messageLogLoc = new JLabel();
        messageLogLoc.setPreferredSize(new Dimension(300, 15));
        appLogLoc = new JLabel();
        appLogLoc.setPreferredSize(new Dimension(300, 15));
        locale = new JComboBox<>();
        locale.addItem("en");
        locale.addItem("ja");
        locale.addItem("mori");
        locale.setPreferredSize(new Dimension(300, 15));
        networkIF = new JComboBox<>();
        networkIF.addItem("auto");
        NetworkIFUtil.getNetworkIFList().forEach(s -> networkIF.addItem(s));
        networkIF.setPreferredSize(new Dimension(300, 15));
        attachmentStoreLoc = new JLabel("User Name");
        attachmentStoreLoc.setPreferredSize(new Dimension(100, 15));
        attachmentOverwriteOK = new JComboBox<>();
        attachmentOverwriteOK.addItem("true");
        attachmentOverwriteOK.addItem("false");

        initField(userNameInput, Resource.RES_USER_NAME);
        add(userNameLabel);
        add(userNameInput);
        initField(groupNameInput, Resource.RES_GROUP_NAME);
        add(groupNameLabel);
        add(groupNameInput);
        initLabel(userIconLoc, Resource.RES_USER_ICON);
        add(userIconLabel);
        add(userIconLoc);
        initLabel(messageLogLoc, Resource.RES_MESSAGE_LOG);
        add(messageLogLabel);
        add(messageLogLoc);
        initLabel(appLogLoc, Resource.RES_APP_LOG);
        add(appLogLabel);
        add(appLogLoc);
        initCombo(locale, Resource.RES_LOCALE);
        add(localeLabel);
        add(locale);
        initCombo(networkIF, Resource.RES_PREFERRED_IF);
        add(preferredNetworkIFLabel);
        add(networkIF);
        initLabel(attachmentStoreLoc, Resource.RES_ATTACHMENTS_DIR);
        add(attachmentStoreLabel);
        add(attachmentStoreLoc);
        initCombo(attachmentOverwriteOK, Resource.RES_ATTAACHMENTS_OKOVERWRITE);
        add(okToOverwriteLabel);
        add(attachmentOverwriteOK);

        okBtn = new JButton("OK");
        okBtn.addActionListener(this);
        okBtn.setPreferredSize(new Dimension(50, 15));
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        okBtn.setPreferredSize(new Dimension(50, 15));
        add(okBtn);
        add(cancelBtn);

        pack();
        setVisible(true);
    }

    public void initField(JTextField comp, String propName) {
        comp.setText(Resource.getAppProperty(propName));
    }

    public void initLabel(JLabel comp, String propName) {
        comp.setText(Resource.getAppProperty(propName));
        comp.addMouseListener(this);
    }
    public void initCombo(JComboBox<String> combo, String propName) {
        String val = Resource.getAppProperty(propName);
        for(int i = 0; i < combo.getItemCount();i++) {
            if(combo.getItemAt(i).equals(val)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == okBtn) {
            try {
                isUpdated = updateConfig();
            } catch(IOException ioE) {
                LOG.error(ioE.getMessage(), ioE);
            }
            setVisible(false);
            dispose();
        }
        else if(source == cancelBtn) {
            setVisible(false);
            dispose();
        }
    }

    private boolean updateConfig() throws IOException {
        Properties props = Resource.getRawAppProperty();
        StringBuilder sb = new StringBuilder();
        String validationMsg = ConfigValidator.validateUserName(userNameInput.getText());
        boolean updated = false;
        updated = validateAndRenewFieldIfRequired(userNameInput, sb, props, Resource.RES_USER_NAME, updated);
        updated = validateAndRenewFieldIfRequired(groupNameInput, sb, props, Resource.RES_GROUP_NAME, updated);
        updated = validateAndRenewLabelIfRequired(userIconLoc, sb, props, Resource.RES_USER_ICON, updated);
        updated = validateAndRenewLabelIfRequired(messageLogLoc, sb, props, Resource.RES_MESSAGE_LOG, updated);
        updated = validateAndRenewLabelIfRequired(appLogLoc, sb, props, Resource.RES_APP_LOG, updated);
        updated = validateAndRenewComboIfRequired(locale, sb, props, Resource.RES_LOCALE, updated);
        updated = validateAndRenewComboIfRequired(networkIF, sb, props, Resource.RES_PREFERRED_IF, updated);
        updated = validateAndRenewLabelIfRequired(attachmentStoreLoc, sb, props, Resource.RES_ATTACHMENTS_DIR, updated);
        updated = validateAndRenewComboIfRequired(attachmentOverwriteOK, sb, props, Resource.RES_ATTAACHMENTS_OKOVERWRITE, updated);

        if(updated) {
            // TODO ask user if really update and shutdown app
            int dialogResult = JOptionPane.showConfirmDialog(null, StringResource.get(StringResource.CONFIGUPD_MSG));
            if(dialogResult == JOptionPane.YES_OPTION) {
                MessagerUI ref = (MessagerUI)super.getParent();
                if(StringUtils.isEmpty(ref.getAppConfPath())) {
                    try (FileOutputStream fout = new FileOutputStream(ConfigDialog.class.getClassLoader().getResource("conf/tanuim.properties").getPath())) {
                        props.store(fout, null);
                        return true;
                    }
                } else {
                    try (FileOutputStream fout = new FileOutputStream(String.format("%s%s%s%s%s", ref.getAppConfPath(), System.getProperty("file.separator"), "conf", System.getProperty("file.separator"), "tanuim.properties"))) {
                        props.store(new OutputStreamWriter(fout, "UTF-8"), null);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean validateAndRenewFieldIfRequired(JTextField field, StringBuilder sb, Properties props, String key, boolean isChanged) {
        String validationMsg = ConfigValidator.validateField(field.getText(), key);
        if(StringUtils.isEmpty(validationMsg)) {
            if(!Resource.getAppProperty(key).equals(field.getText())) {
                props.setProperty(key, field.getText());
                return true;
            }
        }
        else {
            sb.append(validationMsg);
        }
        return isChanged;
    }

    private boolean validateAndRenewLabelIfRequired(JLabel label, StringBuilder sb, Properties props, String key, boolean isChanged) {
        if(!Resource.getAppProperty(key).equals(label.getText())) {
            props.setProperty(key, label.getText());
            return true;
        }
        return isChanged;
    }

    private boolean validateAndRenewComboIfRequired(JComboBox<String> combo, StringBuilder sb, Properties props, String key, boolean isChanged) {
        String selected = combo.getItemAt(combo.getSelectedIndex());
        if(!Resource.getAppProperty(key).equals(selected)) {
            props.setProperty(key, selected);
            return true;
        }
        return isChanged;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        int retval;
        JFileChooser fc;
        if(source == messageLogLoc) {
             fc = new JFileChooser();
             fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
             fc.setCurrentDirectory(new File(messageLogLoc.getText()));
             retval = fc.showOpenDialog(this);
             if(retval == JFileChooser.APPROVE_OPTION) {
                 messageLogLoc.setText(fc.getSelectedFile().getAbsolutePath());
             }
        }
        else if(source == appLogLoc) {
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(appLogLoc.getText()));
            retval = fc.showOpenDialog(this);
            if(retval == JFileChooser.APPROVE_OPTION) {
                appLogLoc.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
        else if(source == userIconLoc) {
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fc.addChoosableFileFilter(imageFilter);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setCurrentDirectory(new File(userIconLoc.getText()));
            retval = fc.showOpenDialog(this);
            if(retval == JFileChooser.APPROVE_OPTION) {
                userIconLoc.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
        else if(source == attachmentStoreLoc) {
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(attachmentStoreLoc.getText()));
            retval = fc.showOpenDialog(this);
            if(retval == JFileChooser.APPROVE_OPTION) {
                attachmentStoreLoc.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
