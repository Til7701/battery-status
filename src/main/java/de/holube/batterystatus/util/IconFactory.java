package de.holube.batterystatus.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IconFactory {

    private IconFactory() {

    }

    public static BufferedImage create(String text) {
        Font font = new Font("Arial", Font.PLAIN, 200);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getAscent() - fm.getDescent() + 10;
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, height - 5);
        g2d.dispose();

        return img;
    }

}
