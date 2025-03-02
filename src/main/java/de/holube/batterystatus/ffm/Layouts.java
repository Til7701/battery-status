package de.holube.batterystatus.ffm;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.ValueLayout;

class Layouts {

    private Layouts() {
        // Utility class
    }

    // https://learn.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-system_power_status
    static MemoryLayout SYSTEM_POWER_STATUS() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_BYTE,
                ValueLayout.JAVA_BYTE,
                ValueLayout.JAVA_BYTE.withName("BatteryLifePercent"),
                ValueLayout.JAVA_BYTE,
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT
        );
    }

}
