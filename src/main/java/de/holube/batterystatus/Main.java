package de.holube.batterystatus;

import de.holube.batterystatus.util.IconFactory;
import de.holube.batterystatus.util.Kernel32;
import de.holube.batterystatus.util.TrayIconFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();

    private static final TrayIcon trayIcon = TrayIconFactory.create();

    public static void main(String[] args) {
        if (!SystemTray.isSupported()) {
            System.err.println("System Tray not Supported!");
            System.exit(1);
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (Throwable e) {
            System.err.println("Could not add Tray Icon! " + e.getMessage());
            System.exit(1);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                refreshIcon();
            }

        }, 100, 60 * 1000);
    }

    private static void refreshIcon() {
        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
        String text = batteryStatus.getBatteryLifePercent();

        BufferedImage img = IconFactory.create(text);

        int iconWidth = (int) trayIcon.getSize().getWidth();
        int iconHeight = (int) trayIcon.getSize().getHeight();
        if (img.getWidth() > img.getHeight()) {
            trayIcon.setImage(img.getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        } else {
            trayIcon.setImage(img.getScaledInstance(-1, iconHeight, Image.SCALE_SMOOTH));
        }
    }
}
