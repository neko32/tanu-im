package org.tanuneko.im;

import org.junit.*;
import org.mockito.Mockito;
import org.tanuneko.im.desktop.MessagerUI;
import org.tanuneko.im.net.GenericIOTest;
import org.tanuneko.im.util.LocalUserBuilder;
import org.tanuneko.im.util.Resource;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/18.
 */
public class AppRunnerTest {

    private static MessagerUI mockUI;

    @BeforeClass
    public static void setupClass() throws IOException, AWTException {
        Resource.initSysProperty(AppRunnerTest.class.getClassLoader().getResourceAsStream("application.properties"));
        Resource.initAppProperty(AppRunnerTest.class.getClassLoader().getResourceAsStream("conf/tanuim_test.properties"));
        LocalUserBuilder.clearCache();

        mockUI = Mockito.mock(MessagerUI.class);
        Mockito.doNothing().when(mockUI).initialize();
        AppRunner.setOldLogRetention(0);
        AppRunner.setMessagerUI(mockUI);
        AppRunner.setPropName("\\conf\\tanuim_test.properties");
        AppRunner.setPropPath("conf/tanuim_test.properties");
    }

    @AfterClass
    public static void tearDownClass() {
        LocalUserBuilder.clearCache();
    }

    @Before
    public void setup() {
        /*
        ceateDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_APP_LOG));
        createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_MESSAGE_LOG));
        createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR));
        */
        File appLog = new File(Resource.getAppProperty(Resource.RES_APP_LOG));
        System.out.println("appLog:" + appLog.getAbsolutePath());
        if(appLog.exists()) {
            appLog.delete();
        }
        File msgLog = new File(Resource.getAppProperty(Resource.RES_MESSAGE_LOG));
        System.out.println("msgLog:" + msgLog.getAbsolutePath());
        if(msgLog.exists()) {
            msgLog.delete();
        }
        File attachmentLog = new File(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR));
        System.out.println("attachmentLog:" + attachmentLog.getAbsolutePath());
        if(attachmentLog.exists()) {
            attachmentLog.delete();
        }
    }

    @After
    public void tearDown() {
        String appLogDir = Resource.getAppProperty(Resource.RES_APP_LOG);
        String sep = System.getProperty("file.separator");
        File f1 = new File(appLogDir + sep + "logfile1.log");
        if(f1.exists()) {
            f1.delete();
        }
        File f2 = new File(appLogDir + sep + "logfile2.log");
        f2 = new File(appLogDir + sep + "logfile2.log");
        if(f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void testCreateDirectoryIfNotExists() throws IOException {

        assertThat(new File(Resource.getAppProperty(Resource.RES_APP_LOG)).exists(), is(false));
        assertThat(new File(Resource.getAppProperty(Resource.RES_MESSAGE_LOG)).exists(), is(false));
        assertThat(new File(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR)).exists(), is(false));
        AppRunner.createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_APP_LOG));
        AppRunner.createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_MESSAGE_LOG));
        AppRunner.createDirectoryIfNotExists(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR));
        assertThat(new File(Resource.getAppProperty(Resource.RES_APP_LOG)).exists(), is(true));
        assertThat(new File(Resource.getAppProperty(Resource.RES_MESSAGE_LOG)).exists(), is(true));
        assertThat(new File(Resource.getAppProperty(Resource.RES_ATTACHMENTS_DIR)).exists(), is(true));
    }

    @Test
    public void testPurgeOldLogs() throws IOException {
        String appLogDir = Resource.getAppProperty(Resource.RES_APP_LOG);
        String sep = System.getProperty("file.separator");
        AppRunner.createDirectoryIfNotExists(appLogDir);
        assertThat(new File(appLogDir).exists(), is(true));
        File f1 = new File(appLogDir + sep + "logfile1.log");
        f1.createNewFile();
        f1.setLastModified(System.currentTimeMillis() - 432000000);
        File f2 = new File(appLogDir + sep + "logfile2.log");
        f2 = new File(appLogDir + sep + "logfile2.log");
        f2.createNewFile();
        f2.setLastModified(System.currentTimeMillis() - 432000000);
        assertThat(f1.exists(), is(true));
        assertThat(f2.exists(), is(true));
        AppRunner.purgeOldLogs(appLogDir);
        assertThat(f1.exists(), is(false));
        assertThat(f2.exists(), is(false));
    }

    @Test
    public void testMain() throws IOException, AWTException {
        String[] args = new String[0];
        AppRunner.main(args);
    }
}
