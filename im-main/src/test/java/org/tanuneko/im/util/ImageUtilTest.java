package org.tanuneko.im.util;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class ImageUtilTest {

    @Test
    public void testGetScaledImage() throws IOException {
        Image img = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("images/icon/default.png"));
        // this image's size is (x,y)=(64,64)
        img = ImageUtil.getScaledImage(img, 50, 75);
        assertThat(img.getWidth(null), is(50));
        assertThat(img.getHeight(null), is(75));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ImageUtil> cons = ImageUtil.class.getDeclaredConstructor();
        cons.setAccessible(true);
        ImageUtil r = cons.newInstance();
    }
}
