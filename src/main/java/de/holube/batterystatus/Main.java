package de.holube.batterystatus;

import de.holube.batterystatus.util.IconFactory;
import de.holube.batterystatus.util.Kernel32;
import de.holube.batterystatus.util.TrayIconFactory;
import dorkbox.systemTray.SystemTray;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();

    private static final SystemTray systemTray = TrayIconFactory.create();

    public static void main(String[] args) {
        Timer timer = new Timer(true);
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

        int size = systemTray.getMenuImageSize();
        if (img.getWidth() > img.getHeight()) {
            systemTray.setImage(img.getScaledInstance(size, -1, Image.SCALE_SMOOTH));
        } else {
            systemTray.setImage(img.getScaledInstance(-1, size, Image.SCALE_SMOOTH));
        }
    }
}
