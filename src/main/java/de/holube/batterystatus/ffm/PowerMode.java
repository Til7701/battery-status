package de.holube.batterystatus.ffm;

import java.util.UUID;

public enum PowerMode {

    // https://learn.microsoft.com/en-us/windows/win32/power/power-policy-settings
    POWER_SAVER("a1841308-3541-4fab-bc81-f71556f20b4a"),
    BALANCED("381b4222-f694-41f0-9685-ff5bb260df2e"),
    HIGH_PERFORMANCE("8c5e7fda-e8bf-4a96-9a85-a6e23a8c635c");

    private final UUID uuid;

    PowerMode(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public static PowerMode fromUUID(UUID uuid) {
        for (PowerMode mode : values()) {
            if (mode.uuid.equals(uuid)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown power mode UUID: " + uuid);
    }
}
