package de.holube.batterystatus.util;

public class VersionInfo {

    private VersionInfo() {
        // Utility class
    }

    public static boolean isWindows11() {
        String version = System.getProperty("os.version");
        System.out.println("OS version: " + version);
        return version != null && version.startsWith("11");
    }

}
