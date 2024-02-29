package de.holube.batterystatus;

import com.github.jbrienen.vbs_sc.ShortcutFactory;
import dorkbox.systemTray.SystemTray;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();

    private static final SystemTray systemTray = TrayIconFactory.create();

    public static void main(String[] args) {
        registerAutostart();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                refreshIcon();
            }

        }, 100, 60 * 1000L);
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

        BufferedImage img = IconFactory.create(text);

        int size = systemTray.getMenuImageSize();
        if (img.getWidth() > img.getHeight()) {
            systemTray.setImage(img.getScaledInstance(size, -1, Image.SCALE_SMOOTH));
        } else {
            systemTray.setImage(img.getScaledInstance(-1, size, Image.SCALE_SMOOTH));
        }
    }
}
