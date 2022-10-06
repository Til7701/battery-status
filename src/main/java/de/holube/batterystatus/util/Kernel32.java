package de.holube.batterystatus.util;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/a/3434962
 */
public interface Kernel32 extends StdCallLibrary {

    Kernel32 INSTANCE = Native.load("Kernel32", Kernel32.class);

    /**
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-system_power_status">Windows Power Status</a>
     */
    class SYSTEM_POWER_STATUS extends Structure {
        public byte ACLineStatus;
        public byte BatteryFlag;
        public byte BatteryLifePercent;
        @SuppressWarnings("unused")
        public byte Reserved1;
        public int BatteryLifeTime;
        public int BatteryFullLifeTime;

        @Override
        protected List<String> getFieldOrder() {
            ArrayList<String> fields = new ArrayList<>();
            fields.add("ACLineStatus");
            fields.add("BatteryFlag");
            fields.add("BatteryLifePercent");
            fields.add("Reserved1");
            fields.add("BatteryLifeTime");
            fields.add("BatteryFullLifeTime");
            return fields;
        }

        /**
         * The AC power status
         */
        public String getACLineStatusString() {
            switch (ACLineStatus) {
                case (0):
                    return "Offline";
                case (1):
                    return "Online";
                default:
                    return "Unknown";
            }
        }

        /**
         * The battery charge status
         */
        public String getBatteryFlagString() {
            switch (BatteryFlag) {
                case (1):
                    return "High, more than 66 percent";
                case (2):
                    return "Low, less than 33 percent";
                case (4):
                    return "Critical, less than five percent";
                case (8):
                    return "Charging";
                case ((byte) 128):
                    return "No system battery";
                default:
                    return "Unknown";
            }
        }

        /**
         * The percentage of full battery charge remaining
         */
        public String getBatteryLifePercent() {
            return (BatteryLifePercent == (byte) 255) ? "Unknown" : BatteryLifePercent + "";
        }

        /**
         * The number of seconds of battery life remaining
         */
        public String getBatteryLifeTime() {
            return (BatteryLifeTime == -1) ? "Unknown" : BatteryLifeTime + " seconds";
        }

        /**
         * The number of seconds of battery life when at full charge
         */
        public String getBatteryFullLifeTime() {
            return (BatteryFullLifeTime == -1) ? "Unknown" : BatteryFullLifeTime + " seconds";
        }

        @Override
        public String toString() {
            return "ACLineStatus: " + getACLineStatusString() + "\n" +
                    "Battery Flag: " + getBatteryFlagString() + "\n" +
                    "Battery Life: " + getBatteryLifePercent() + "\n" +
                    "Battery Left: " + getBatteryLifeTime() + "\n" +
                    "Battery Full: " + getBatteryFullLifeTime() + "\n";
        }
    }

    /**
     * Fill the structure.
     */
    @SuppressWarnings("UnusedReturnValue")
    int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);

}