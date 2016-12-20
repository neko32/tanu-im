package org.tanuneko.im.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanuneko.im.util.StringResource;
import org.tanuneko.im.util.UnicodeUtil;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by neko32 on 2016/12/07.
 */
@SuppressWarnings("ALL")
public class Message implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Message.class);
    private static final String MSG_ENTRY_SEPARATOR = "**************";
    private String senderName;
    private String receiverName;
    private InetAddress senderAddress;
    private InetAddress receiverAddress;
    private String senderGroupName;
    private String receiverGroupName;
    private byte[] senderImage;
    private byte[] stamp;
    private String message;
    private List<Attachment> attachments;
    private boolean isEnvelopeRequired;
    private boolean isConfirmationRequired;
    private boolean isMessageUpdated;

    public Message() {
        isEnvelopeRequired = isConfirmationRequired = false;
        stamp = senderImage = null;
        attachments = null;
    }

    public Message(String senderName, InetAddress senderAddress, String senderGroupName, String receiverName, InetAddress receiverAddress, String receiverGroupName, String message) {
        this.senderName = senderName;
        this.senderAddress = senderAddress;
        this.message = message;
        this.receiverName = receiverName;
        this.receiverGroupName = receiverGroupName;
        this.receiverAddress = receiverAddress;
        this.senderGroupName = senderGroupName;
        LOG.debug("MSGLEN:{}", message.length());
        if(UnicodeUtil.isLikely2ByteString(this.message)) {
            if (UnicodeUtil.getTotalByteCounts(this.message) > 40) {
                splitTwoByteLongMessage();
            }
        }
        else {
            if(this.message.length() > 24) {
                splitSingleByteLongMessage();
            }
        }
        isEnvelopeRequired = isConfirmationRequired = false;
        stamp = senderImage = null;
        attachments = null;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderGroupName() {
        return senderGroupName;
    }

    public void setSenderGroupName(String senderGroupName) {
        this.senderGroupName = senderGroupName;
    }

    public InetAddress getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(InetAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public InetAddress getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(InetAddress receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverGroupName() {
        return receiverGroupName;
    }

    public void setReceiverGroupName(String receiverGroupName) {
        this.receiverGroupName = receiverGroupName;
    }

    public byte[] getSenderImage() {
        return senderImage;
    }

    public byte[] getStamp() {
        return stamp;
    }

    public void setSenderImage(byte[] senderImage) {
        this.senderImage = senderImage;
    }

    public void setStamp(byte[] stamp) {
        this.stamp = stamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isEnvelopeRequired() {
        return isEnvelopeRequired;
    }

    public void setEnvelopeRequired(boolean envelopeRequired) {
        isEnvelopeRequired = envelopeRequired;
    }

    public boolean isConfirmationRequired() {
        return isConfirmationRequired;
    }

    public void setConfirmationRequired(boolean confirmationRequired) {
        isConfirmationRequired = confirmationRequired;
    }

    public void updateMessageIfAttachmentExists(boolean isForSelfMessage) {
        if(isMessageUpdated) {
            return;
        }
        int numAttachments = 0;
        StringBuilder sb = new StringBuilder();
        if(attachments != null) {
            if(message == null) {
                message = "";
            }
            for(Attachment attachment: attachments) {
                sb.append(String.format("%s,", attachment.getFileName()));
            }
            if(message.isEmpty()) {
                if(isForSelfMessage) {
                    message = String.format(StringResource.get(StringResource.MESSAGE_ATTACHMENT_INFO_SELF), attachments.size(), sb.toString().substring(0, sb.length() - 1), receiverName);
                } else {
                    message = String.format(StringResource.get(StringResource.MESSAGE_ATTACHMENT_INFO), attachments.size(), sb.toString().substring(0, sb.length() - 1), senderName);
                }
            }
            else {
                if(isForSelfMessage) {
                    message += String.format("%s" + StringResource.get(StringResource.MESSAGE_ATTACHMENT_INFO_SELF), System.getProperty("line.separator"), attachments.size(), sb.toString().substring(0, sb.length() - 1), receiverName);
                } else {
                    message += String.format("%s" + StringResource.get(StringResource.MESSAGE_ATTACHMENT_INFO), System.getProperty("line.separator"), attachments.size(), sb.toString().substring(0, sb.length() - 1), senderName);
                }
            }
            if (UnicodeUtil.isLikely2ByteString(message)) {
                splitTwoByteLongMessage();
            }
            else {
                splitSingleByteLongMessage();
            }
            isMessageUpdated = true;
        }
    }

    public void splitSingleByteLongMessage() {
        StringBuilder orig = new StringBuilder(message);
        StringBuilder news = new StringBuilder();
        String sep = System.getProperty("line.separator");
        while(UnicodeUtil.getTotalByteCounts(orig.toString()) > 50) {
            news.append(String.format("%s%s", orig.substring(0, 50), sep));
            orig.delete(0, 50);
        }
        news.append(orig.toString());
        message = news.toString();
    }

    public void splitTwoByteLongMessage() {
        StringBuilder orig = new StringBuilder(message);
        StringBuilder news = new StringBuilder();
        String sep = System.getProperty("line.separator");
        while(orig.length() > 24) {
            LOG.debug("orig len:{}", orig.length());
            news.append(String.format("%s%s", orig.substring(0, 24), sep));
            orig.delete(0, 24);
        }
        news.append(orig.toString());
        message = news.toString();
    }

    public String getStringForMessageLog() {
        StringBuilder sb = new StringBuilder();
        String sep = System.getProperty("line.separator");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:MM:ss");
        Date d = new Date(System.currentTimeMillis());
        sb.append(String.format("*%s[%s](%s) at %s%s", senderName, senderGroupName, senderAddress.getHostAddress(), sdf.format(d), sep));
        sb.append(String.format("%s%s", message, sep));
        sb.append(String.format("%s%s", MSG_ENTRY_SEPARATOR, sep));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderName='" + senderName + '\'' +
                ", senderGroup='" + senderGroupName + '\'' +
                ", senderAddress=" + senderAddress +
                ", receiverName='" + receiverName + '\'' +
                ", receiverGroup='" + receiverGroupName + '\'' +
                ", receiverAddress=" + receiverAddress + '\'' +
                ", message='" + message + '\'' +
                ", num of attachments=" + (attachments == null ? "0" : attachments.size()) +
                ", isEnvelopeRequired=" + isEnvelopeRequired +
                ", isConfirmationRequired=" + isConfirmationRequired +
                '}';
    }
}
