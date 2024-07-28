# battery-status

Adds a tray icon to show the current battery percentage for Windows devices.

## Installation

If you have an older version of the application installed, I recommend to stop the application first. You can do this by
right-clicking on the tray icon and selecting "Exit".

Download the latest release and run the installer. When you run the application for the first time, it will add the
application to the startup folder, so it will start automatically when you log in again.

I tested the application on Windows 10 and 11.

## Build

To build the application, you need to have the following tools installed on a Windows machine:

- JDK 21 or later
- Maven
- WiX (https://wixtoolset.org/)
- GCC
- Make

The last two tools are required to build the native library that is used to get the battery status and detect, when the
system wakes up from sleep. You likely want to use the https://github.com/skeeto/w64devkit to get the required tools.

To build the application, run the `build.sh` script. This will build the java application and the native library.
Compilation of Java and C is handled by Maven. Maven calls Make to build the native library. The script also creates an
installer for the application using `jpackage`. For that you need WiX installed on your machine. (Note, I used WiX 3.14
to create the installer. Newer versions might not be supported by JPackage).

## License

battery-status is released under the GNU General Public License v3.0
