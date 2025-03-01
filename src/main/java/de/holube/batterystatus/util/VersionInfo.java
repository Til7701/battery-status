package de.holube.batterystatus.util;

public class VersionInfo {

    private VersionInfo() {
        // Utility class
    }

    public static boolean isWindows11() {
        String name = System.getProperty("os.name");
        System.out.println("OS name: " + name);
        return name != null && name.startsWith("Windows 11");
    }

}
