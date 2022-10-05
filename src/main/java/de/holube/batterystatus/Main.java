package de.holube.batterystatus;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
    private static TrayIcon trayIcon = null;

    public static void main(String[] args) {

        // for Battery Level
        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);

        // for TrayIcon
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon.gif");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = e -> System.exit(0);
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem();
            defaultItem.setLabel("Exit");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);

            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Battery Status", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(listener);
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
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
        trayIcon.setToolTip(text + "%");

        Font font = new Font("Arial", Font.PLAIN, 100);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        /*try {
            ImageIO.write(img, "gif", new File("images/icon.gif"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/

        //Image updatedImage = Toolkit.getDefaultToolkit().getImage("images/icon.gif");

        // update the TrayIcon
        if (trayIcon != null) {
            int iconWidth = (int) trayIcon.getSize().getWidth();
            trayIcon.setImage(img.getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
            //trayIcon.setImage(updatedImage);
        }
    }
}
