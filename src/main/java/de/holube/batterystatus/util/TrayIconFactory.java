package de.holube.batterystatus.util;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TrayIconFactory {

    private TrayIconFactory() {

    }

    public static TrayIcon create() {
        PopupMenu popup = createPopup();
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Image image = img.getScaledInstance(1, 1, Image.SCALE_FAST);

        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setToolTip(null);
        trayIcon.setPopupMenu(popup);

        return trayIcon;
    }

    private static PopupMenu createPopup() {
        ActionListener listener = e -> System.exit(0);

        MenuItem defaultItem = new MenuItem();
        defaultItem.setLabel("Exit");
        defaultItem.addActionListener(listener);

        PopupMenu popup = new PopupMenu();
        popup.add(defaultItem);

        return popup;
    }

}
