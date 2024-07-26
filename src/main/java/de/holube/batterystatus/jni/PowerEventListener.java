package de.holube.batterystatus.jni;

public class PowerEventListener {

    static {
        System.loadLibrary("PowerEventLib");
    }

    public native void initPowerEventListener();

    public void onSystemSuspend() {
        System.out.println("System is going to sleep");
    }

    public void onSystemResume() {
        System.out.println("System has resumed from sleep");
    }

}