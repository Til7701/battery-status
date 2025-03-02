package de.holube.batterystatus.ffm;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class NativePowerLib {

    private NativePowerLib() {
        // Utility class
    }

    // https://learn.microsoft.com/en-us/windows/win32/api/winbase/nf-winbase-getsystempowerstatus
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
            MethodHandle methodHandle = linker.downcallHandle(functionAddr, sig);

            // create input
            MemorySegment nativeInput = arena.allocate(layout);

            // call function
            boolean success = (boolean) methodHandle.invokeExact(nativeInput);

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

    public static PowerMode getActivePowerMode() {
        try (Arena arena = Arena.ofConfined()) {
            // get library
            Linker linker = Linker.nativeLinker();
            SymbolLookup lib = SymbolLookup.libraryLookup("PowrProf", arena);

            // get function address
            Optional<MemorySegment> optional = lib.find("PowerGetActiveScheme");
            if (optional.isEmpty()) {
                throw new FFMException("Failed to find PowerGetActiveScheme");
            }
            MemorySegment functionAddr = optional.get();

            // create layout and downcall
            FunctionDescriptor sig = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle methodHandle = linker.downcallHandle(functionAddr, sig);

            // create input
            MemorySegment nativeInput = arena.allocate(ValueLayout.ADDRESS);

            // call function
            long success = (long) methodHandle.invokeExact(MemorySegment.NULL, nativeInput);

            // check result
            if (success != 0) {
                throw new FFMException("Failed to get power mode");
            }
            MemorySegment guidSegment = nativeInput.getAtIndex(ValueLayout.ADDRESS, 0).reinterpret(GUID.layout().byteSize());
            GUID guid = GUID.fromMemorySegment(guidSegment);
            localFree(guidSegment, arena);
            return PowerMode.fromGUID(guid);
        } catch (Throwable e) {
            throw new FFMException("Failed to get power mode", e);
        }
    }

    public static void setActivePowerMode(PowerMode mode) {
        try (Arena arena = Arena.ofConfined()) {
            // get library
            Linker linker = Linker.nativeLinker();
            SymbolLookup lib = SymbolLookup.libraryLookup("PowrProf", arena);

            // get function address
            Optional<MemorySegment> optional = lib.find("PowerSetActiveScheme");
            if (optional.isEmpty()) {
                throw new FFMException("Failed to find PowerSetActiveScheme");
            }
            MemorySegment functionAddr = optional.get();

            // create layout and downcall
            FunctionDescriptor sig = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle methodHandle = linker.downcallHandle(functionAddr, sig);

            // create input
            MemorySegment nativeInput = arena.allocate(GUID.layout());
            GUID.writeToMemorySegment(mode, nativeInput);

            // call function
            long success = (long) methodHandle.invokeExact(MemorySegment.NULL, nativeInput);

            // check result
            if (success != 0)
                throw new FFMException("Failed to set power mode");
        } catch (Throwable e) {
            throw new FFMException("Failed to set power mode", e);
        }
    }

    private static void localFree(MemorySegment hMem, Arena arena) throws Throwable {
        // get library
        Linker linker = Linker.nativeLinker();
        SymbolLookup lib = SymbolLookup.libraryLookup("Kernel32", arena);

        // get function address
        Optional<MemorySegment> optional = lib.find("LocalFree");
        if (optional.isEmpty()) {
            throw new FFMException("Failed to find LocalFree");
        }
        MemorySegment functionAddr = optional.get();

        // create layout and downcall
        FunctionDescriptor sig = FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS);
        MethodHandle methodHandle = linker.downcallHandle(functionAddr, sig);

        // call function
        MemorySegment result = (MemorySegment) methodHandle.invokeExact(hMem);
        if (!result.equals(MemorySegment.NULL)) {
            throw new FFMException("Failed to free memory");
        }
    }

}
