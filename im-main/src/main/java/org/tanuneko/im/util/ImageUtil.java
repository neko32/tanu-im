package org.tanuneko.im.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by neko32 on 2016/12/11.
 */
@SuppressWarnings("ALL")
public class ImageUtil {

    private ImageUtil() {
        throw new IllegalStateException("No use");
    }

    public static Image getScaledImage(Image image, int preferredWidth, int preferredHeight) {
        BufferedImage resizedImg = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, preferredWidth, preferredHeight, null);
        g2.dispose();
        return resizedImg;
    }
}
