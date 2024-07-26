module battery.status {
    requires java.desktop;
    requires com.sun.jna;

    exports de.holube.batterystatus;
    exports de.holube.batterystatus.jna to com.sun.jna;
}