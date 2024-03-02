package de.holube.batterystatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BatteryStatus {

    private static final String[] COMMAND = "WMIC PATH Win32_Battery Get EstimatedChargeRemaining".split(" ");

    public static String getPercentage() {
        String result;

        try {
            result = runCommand();
        } catch (Exception e) {
            result = null;
        }

        if (result == null || result.isEmpty())
            return "Unknown";
        return result;
    }

    private static String runCommand() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(COMMAND);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> lines = new ArrayList<>();
        String s;
        while ((s = stdInput.readLine()) != null) {
            lines.add(s);
            System.out.println(s);
        }

        return lines.get(2);
    }

}
