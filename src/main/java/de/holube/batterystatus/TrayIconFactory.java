package de.holube.batterystatus;

import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconFactory {

    private TrayIconFactory() {

    }

    public static SystemTray create() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Image image = img.getScaledInstance(1, 1, Image.SCALE_FAST);

        SystemTray systemTray = SystemTray.get();
        if (systemTray == null) {
            System.err.println("System Tray not Supported!");
            System.exit(1);
        }

        systemTray.setImage(image);
        systemTray.getMenu().add(new MenuItem("Exit", e -> systemTray.shutdown()));

        return systemTray;
    }

}
