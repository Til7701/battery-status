package de.holube.batterystatus;

import com.github.jbrienen.vbs_sc.ShortcutFactory;
import de.holube.batterystatus.jni.TBatteryPowerLib;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final TBatteryPowerLib lib = new TBatteryPowerLib(Main::refreshIcon);

    private static final TrayIcon trayIcon;

    static {
        final MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(a -> System.exit(0));
        final PopupMenu popup = new PopupMenu();
        popup.add(exitMenuItem);

        final BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Image image = img.getScaledInstance(1, 1, Image.SCALE_FAST);

        trayIcon = new TrayIcon(image);
        trayIcon.setToolTip(null);
        trayIcon.setPopupMenu(popup);
    }

    public static void main(String[] args) {
        if (!SystemTray.isSupported()) {
            System.err.println("System Tray not Supported!");
            System.exit(1);
        }
        try {
            final SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Could not add Tray Icon! " + e.getMessage());
            System.exit(1);
        }

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                refreshIcon();
                System.gc();
            }

        }, 100, 60 * 1000L);
        registerAutostart();
        lib.initTBatteryPowerLib();
    }

    private static void registerAutostart() {
        final String autoPath = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
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

    private static void refreshIcon() {
        final String text = String.valueOf(lib.getBatteryPercentage());

        final BufferedImage img = createImage(text);

        final int iconWidth = (int) trayIcon.getSize().getWidth();
        final int iconHeight = (int) trayIcon.getSize().getHeight();
        if (img.getWidth() > img.getHeight()) {
            trayIcon.setImage(img.getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        } else {
            trayIcon.setImage(img.getScaledInstance(-1, iconHeight, Image.SCALE_SMOOTH));
        }
        trayIcon.setToolTip(text + "%");
    }

    public static BufferedImage createImage(final String text) {
        final Font font = new Font("Arial", Font.PLAIN, 50);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        final FontMetrics fm = g2d.getFontMetrics();
        final int textHeight = fm.getAscent() - fm.getDescent();
        final int widthHeight = Math.max(fm.stringWidth(text), textHeight);
        g2d.dispose();

        img = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, widthHeight - ((widthHeight - textHeight) / 2));
        g2d.dispose();
        return img;
    }

}
