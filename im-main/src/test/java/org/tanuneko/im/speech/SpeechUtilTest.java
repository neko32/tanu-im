package org.tanuneko.im.speech;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tanuneko.im.speech.SpeechUtil;
import org.tanuneko.im.util.ConfigValidator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
@RunWith(PowerMockRunner.class)
@PrepareForTest(Speech.class)
public class SpeechUtilTest {

    @Test
    public void testSpeechOnWindows() {
     //   SpeechUtil.speech("tanurin たぬりん うしちゃりん", null);
        SpeechUtil.speech("", null);
    }

    @Test
    public void testSpeechOnWindowsWithAppConfPath() {
        //   SpeechUtil.speech("tanurin たぬりん うしちゃりん", null);
        String appConfPath = SpeechUtilTest.class.getClassLoader().getResource("vbs").getPath();
        SpeechUtil.speech("", new File(appConfPath).getParent());
    }

    @Test(expected=IllegalStateException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<SpeechUtil> cons = SpeechUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        SpeechUtil r = cons.newInstance();
    }

    @Test
    public void testExceptionFlow() {
        PowerMockito.mockStatic(Runtime.class);
        Runtime run = mock(Runtime.class);
        Process pMock = mock(Process.class);
        try {
            when(Runtime.getRuntime()).thenReturn(run);
            when(run.exec(Mockito.any(String.class))).thenThrow(new IOException());
            SpeechUtil.speech("", null);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
