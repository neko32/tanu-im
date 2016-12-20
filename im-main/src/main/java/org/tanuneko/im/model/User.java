package org.tanuneko.im.model;

import org.tanuneko.im.util.ImageUtil;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by neko32 on 2016/09/10.
 */
@SuppressWarnings("ALL")
public class User implements Comparable<User>, Serializable {

    private int id;
    private String userName;
    private String groupName;
    private String machineName;
    private transient byte[] icon;
    private InetAddress ipAddress;
    private String extraInfo;
    private String appVersion;

    public User() {
        this.appVersion = StringResource.get(StringResource.STR_VERSION);
    }

    public User(int id, String userName, String groupName, String machineName, byte[] icon, InetAddress ipAddress) {
        this.id = id;
        this.userName = userName;
        this.groupName = groupName;
        this.machineName = machineName;
        this.icon = icon;
        this.ipAddress = ipAddress;
        this.appVersion = StringResource.get(StringResource.STR_VERSION);
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getMachineName() {
        return machineName;
    }

    public byte[] getIcon() {
        return icon;
    }

    public ImageIcon getIconAsImageIcon() throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(icon);
        Image image = ImageIO.read(bin);
        image = getScaledImage(image);
        return new ImageIcon(image);
    }

    private Image getScaledImage(Image image) {
        return ImageUtil.getScaledImage(image, 50, 50);
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!userName.equals(user.userName)) return false;
        if (!groupName.equals(user.groupName)) return false;
        //noinspection SimplifiableIfStatement
        if (!machineName.equals(user.machineName)) return false;
        if(!appVersion.equals(user.appVersion)) return false;
        return ipAddress.equals(user.ipAddress);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userName.hashCode();
        result = 31 * result + groupName.hashCode();
        result = 31 * result + machineName.hashCode();
        result = 31 * result + ipAddress.hashCode();
        result = 31 * result + appVersion.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", userName, groupName);
    }

    @Override
    public int compareTo(User o) {
        return userName.compareTo(o.getUserName());
    }
}
