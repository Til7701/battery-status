package de.holube.batterystatus.jni;

public class TBatteryPowerLib {

    static {
        System.loadLibrary("TBatteryPowerLib");
    }

    private final Runnable resumeCallback;

    public TBatteryPowerLib(Runnable resumeCallback) {
        this.resumeCallback = resumeCallback;
    }

    public native void initTBatteryPowerLib();

    public void onSystemSuspend() {
        System.out.println("System is going to sleep");
    }

    public void onSystemResume() {
        System.out.println("System has resumed from sleep");
        resumeCallback.run();
    }

}
