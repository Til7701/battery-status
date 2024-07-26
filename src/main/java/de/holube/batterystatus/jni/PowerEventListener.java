package de.holube.batterystatus.jni;

public class PowerEventListener {

    static {
        System.loadLibrary("PowerEventLib");
    }

    private final Runnable resumeCallback;

    public PowerEventListener(Runnable resumeCallback) {
        this.resumeCallback = resumeCallback;
    }

    public native void initPowerEventListener();

    public native int getBatteryPercentage();

    public void onSystemSuspend() {
        System.out.println("System is going to sleep");
    }

    public void onSystemResume() {
        System.out.println("System has resumed from sleep");
        resumeCallback.run();
    }

}
