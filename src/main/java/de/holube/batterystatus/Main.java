package de.holube.batterystatus;

import com.github.jbrienen.vbs_sc.ShortcutFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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
            final SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (Throwable e) {
            System.err.println("Could not add Tray Icon! " + e.getMessage());
            System.exit(1);
        }

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                refreshIcon();
            }

        }, 100, 60 * 1000L);
        registerAutostart();
    }

    private static void registerAutostart() {
        final String autoPath = getAutostartDirectory();
        final File autoDir = new File(autoPath);
        if (!autoDir.exists() || !autoDir.isDirectory()) {
            System.err.println("Could not create Shortcut: Autostart directory not found correctly. Search Path: " + autoPath);
            return;
        }
        System.out.println(autoDir.getAbsolutePath());
        final File installDir = new File(".");
        final String exePath = installDir.getAbsolutePath().substring(0, installDir.getAbsolutePath().length() - 2) + "\\battery-status.exe";
        final String linkPath = autoDir.getAbsolutePath() + "\\battery-status.lnk";
        System.out.println(installDir.getAbsolutePath());

        if (!new File(linkPath).exists()) {
            try {
                ShortcutFactory.createShortcut(exePath, linkPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.println("Could not create Shortcut");
            }
        }
    }

    private static String getAutostartDirectory() {
        return System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
    }

    private static void refreshIcon() {
        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
        String text = batteryStatus.getBatteryLifePercent();

        final BufferedImage img = IconFactory.create(text);

        final int iconWidth = (int) trayIcon.getSize().getWidth();
        final int iconHeight = (int) trayIcon.getSize().getHeight();
        if (img.getWidth() > img.getHeight()) {
            trayIcon.setImage(img.getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        } else {
            trayIcon.setImage(img.getScaledInstance(-1, iconHeight, Image.SCALE_SMOOTH));
        }
    }
}
