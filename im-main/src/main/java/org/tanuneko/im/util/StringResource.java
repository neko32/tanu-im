package org.tanuneko.im.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by neko32 on 2016/12/13.
 */

public class StringResource {

    private static String locale = "en";
    private static Properties props;

    public static final String STR_VERSION = "app.version";
    public static final String STR_MAINWND_TITLE = "main.window.title";
    public static final String STR_ATTACHMENT_DEFAULT_MSG = "main.attachments.default_msg";
    public static final String MENU_CONFIG = "main.menuitem.config";
    public static final String MENU_EXIT = "main.menuitem.exit";

    public static final String WARNING_NOUSER_SELECTED_MESSAGE = "main.warning.nouser_selected.message";
    public static final String WARNING_NOUSER_SELECTED_SUBJECT = "main.warning.nouser_selected.subject";
    public static final String WARNING_NOITEM_TO_SEND_MESSAGE = "main.warning.noitem_to_send.message";
    public static final String WARNING_NOITEM_TO_SEND_SUBJECT = "main.warning.noitem_to_send.subject";

    public static final String WARNING_NETWORK_IF_RETRIEVAL_FAIL_MESSAGE = "main.warning.failed_if_retrieval.message";
    public static final String WARNING_NETWORK_IF_RETRIEVAL_FAIL_SUBJECT = "main.warning.failed_if_retrieval.subject";

    public static final String WARNING_SHUTDOWN_AFTER_CONFIGUPD_MESSAGE = "main.warning.app_shutdown_after_configupdate.message";
    public static final String WARNING_SHUTDOWN_AFTER_CONFIGUPD_SUBJECT = "main.warning.app_shutdown_after_configupdate.subject";

    public static final String WARNING_VERSION_OLD_MESSAGE = "main.warning.version_old.message";
    public static final String WARNING_VERSION_OLD_SUBJECT = "main.warning.version_old.subjecvt";

    public static final String SPEECH_RECEIVED_MSG = "main.speech.receivedmsg";
    public static final String NOTICE_NOSAVE_DUETO_SAMEFILE = "main.notice.stopsave_dueto_dupefile";
    public static final String NOTICE_ATTACHMENT_SAVED = "main.notice.attachment_saved";

    public static final String TRAY_NOTICE_MESSAGE_AND_ATTACHMENT_RECEIVED_MESSAGE = "main.traynotice.message_attachment_received.message";
    public static final String TRAY_NOTICE_MESSAGE_AND_ATTACHMENT_RECEIVED_SUBJECT = "main.traynotice.message_attachment_received.subject";
    public static final String TRAY_NOTICE_MESSAGE_RECEIVED_MESSAGE = "main.traynotice.message_received.message";
    public static final String TRAY_NOTICE_MESSAGE_RECEIVED_SUBJECT = "main.traynotice.message_received.subject";
    public static final String TRAY_NOTICE_ATTACHMENT_RECEIVED_MESSAGE = "main.traynotice.attachment_received.message";
    public static final String TRAY_NOTICE_ATTACHMENT_RECEIVED_SUBJECT = "main.traynotice.attachment_received.subject";

    public static final String  MESSAGE_ATTACHMENT_INFO = "message.attachment_info";
    public static final String MESSAGE_ATTACHMENT_INFO_SELF = "message.attachment_info_for_self";

    public static final String MORINOKO_MAIN_TOOLTIP = "morinoko.main.tooltip";

    public static final String CONFIGDLG_USERNAME = "configdlg.username";
    public static final String CONFIGDLG_GROUPNAME = "configdlg.groupname";
    public static final String CONFIGDLG_USERICON = "configdlg.usericon";
    public static final String CONFIGDLG_MORINOKOIMG = "configdlg.morinokoimg";
    public static final String CONFIGDLG_MSGLOG = "configdlg.messagelog";
    public static final String CONFIGDLG_APPLOG = "configdlg.applog";
    public static final String CONFIGDLG_LOCALE = "configdlg.locale";
    public static final String CONFIGDLG_PREFERRED_NETWORK_IF = "configdlg.preferredIF";
    public static final String CONFIGDLG_ATTACHMENT_STORE = "configdlg.attachmentstore";
    public static final String CONFIGDLG_ATTACHMENT_OVERWRITE = "configdlg.attachment_ok_to_overwrite";

    public static final String CONFIGUPD_MSG = "configupdate.confirm.msg";

    private StringResource() {
        throw new IllegalStateException("No use");
    }


    public static void init(String localeToSet) throws IOException {
        locale = localeToSet;
        SupportedLocale.valueOf(locale);
        props = new Properties();
        try(InputStream in = StringResource.class.getClassLoader().getResourceAsStream(String.format("string/string_%s.properties", locale))) {
            props.load(in);
        }
    }

    public static String get(final String key) {
        return (String)props.get(key);
    }

    enum SupportedLocale {
        en,
        ja,
        mori
    }
}
