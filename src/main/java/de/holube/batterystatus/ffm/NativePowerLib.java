package de.holube.batterystatus.ffm;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class NativePowerLib {

    private NativePowerLib() {
        // Utility class
    }

    public static int getBatteryPercentage() {
        try (Arena arena = Arena.ofConfined()) {
            // get library
            Linker linker = Linker.nativeLinker();
            SymbolLookup lib = SymbolLookup.libraryLookup("Kernel32", arena);

            // get function address
            Optional<MemorySegment> optional = lib.find("GetSystemPowerStatus");
            if (optional.isEmpty()) {
                throw new FFMException("Failed to find GetSystemPowerStatus");
            }
            MemorySegment functionAddr = optional.get();

            // create layout and downcall
            MemoryLayout layout = Layouts.SYSTEM_POWER_STATUS();
            FunctionDescriptor sig = FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, ValueLayout.ADDRESS);
            MethodHandle strlen = linker.downcallHandle(functionAddr, sig);

            // create input
            MemorySegment nativeInput = arena.allocate(layout);

            // call function
            boolean success = (boolean) strlen.invokeExact(nativeInput);

            // check result
            if (!success) {
                throw new FFMException("Failed to get battery percentage");
            }
            return nativeInput.get(ValueLayout.JAVA_BYTE,
                    layout.byteOffset(MemoryLayout.PathElement.groupElement("BatteryLifePercent")));
        } catch (Throwable e) {
            throw new FFMException("Failed to get battery percentage", e);
        }
    }

}
