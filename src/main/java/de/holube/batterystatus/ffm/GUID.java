package de.holube.batterystatus.ffm;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;
import java.util.UUID;

// https://learn.microsoft.com/en-us/windows/win32/api/guiddef/ns-guiddef-guid
class GUID {

    private final long data1;
    private final long data2;

    GUID(long data1, long data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    GUID(UUID uuid) {
        this(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUID guid = (GUID) o;
        return data1 == guid.data1 && data2 == guid.data2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data1, data2);
    }

    @Override
    public String toString() {
        return "GUID{" +
               "data1=" + Long.toHexString(data1) +
               ", data2=" + Long.toHexString(data2) +
               '}';
    }

    static MemoryLayout layout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_LONG,
                ValueLayout.JAVA_SHORT,
                ValueLayout.JAVA_SHORT,
                MemoryLayout.sequenceLayout(8, ValueLayout.JAVA_CHAR)
        );
    }

    static GUID fromMemorySegment(MemorySegment segment) {
        long data1 = segment.getAtIndex(ValueLayout.JAVA_LONG, 0);
        long data2 = segment.getAtIndex(ValueLayout.JAVA_LONG, ValueLayout.JAVA_BYTE.byteSize());

        // create UUID
        long part1 = ((data1 & 0xFFFF000000000000L) >> 48) | ((data1 & 0x0000FFFF00000000L) >> 16) | ((data1 & 0x00000000FFFFFFFFL) << 32);
        long part2 = Long.reverseBytes(data2);
        return new GUID(part1, part2);
    }

    static void writeToMemorySegment(PowerMode mode, MemorySegment nativeInput) {
        GUID guid = mode.getGUID();
        long data1 = guid.data1;
        long data2 = guid.data2;

        // create GUID
        long tmp1 = ((data1 & 0x000000000000FFFFL) << 48);
        long tmp2 = tmp1 | ((data1 & 0x00000000FFFF0000L) << 16);
        long part1 = tmp2 | ((data1 & 0xFFFFFFFF00000000L) >>> 32);
        long part2 = Long.reverseBytes(data2);

        nativeInput.setAtIndex(ValueLayout.JAVA_LONG, ValueLayout.JAVA_BYTE.byteSize(), part2);
        nativeInput.setAtIndex(ValueLayout.JAVA_LONG, 0, part1);
    }

}
