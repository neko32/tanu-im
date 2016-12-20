package org.tanuneko.im.desktop;

import org.tanuneko.im.util.ImageUtil;
import org.tanuneko.im.util.StringResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Created by neko32 on 2016/09/10.
 */
@SuppressWarnings("ALL")
public class MorinokoPanel extends JPanel implements ActionListener, MouseListener {

    private static final String DEFAULT_PANEL_IMAGE = "images/panel/morinoko_pane.png";
    private Image image;

    public MorinokoPanel() throws IOException {
        image = ImageIO.read(MorinokoPanel.class.getClassLoader().getResourceAsStream(DEFAULT_PANEL_IMAGE));
    }

    public void init() {
        setToolTipText(StringResource.get(StringResource.MORINOKO_MAIN_TOOLTIP));
        addMouseListener(this);
    }

    public void resizeImage(int w, int h) {
        image = ImageUtil.getScaledImage(image, w, h);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
