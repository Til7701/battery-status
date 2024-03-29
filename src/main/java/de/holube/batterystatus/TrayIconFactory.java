package de.holube.batterystatus;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconFactory {

    private TrayIconFactory() {

    }

    public static TrayIcon create() {
        final PopupMenu popup = createPopup();
        final BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Image image = img.getScaledInstance(1, 1, Image.SCALE_FAST);

        final TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setToolTip(null);
        trayIcon.setPopupMenu(popup);

        return trayIcon;
    }

    private static PopupMenu createPopup() {
        final MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(a -> System.exit(0));

        final PopupMenu popup = new PopupMenu();
        popup.add(exitMenuItem);

        return popup;
    }

}
