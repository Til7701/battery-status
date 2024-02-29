package de.holube.batterystatus;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IconFactory {

    private IconFactory() {

    }

    public static BufferedImage create(String text) {
        final Font font = new Font("Arial", Font.PLAIN, 200);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        final FontMetrics fm = g2d.getFontMetrics();
        final int width = fm.stringWidth(text);
        final int height = fm.getAscent() - fm.getDescent();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, height);
        g2d.dispose();

        return img;
    }

}
