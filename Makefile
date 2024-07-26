JAVA_HOME := $(shell echo $$JAVA_HOME)

all: TBatteryPowerLib.dll

TBatteryPowerLib.dll: src/main/c/de_holube_batterystatus_jni_TBatteryPowerLib.c
	gcc -shared -o src/main/c/TBatteryPowerLib.dll -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/win32" -I"src/main/c/" src/main/c/de_holube_batterystatus_jni_TBatteryPowerLib.c
	mkdir -p "target/native"
	mv src/main/c/TBatteryPowerLib.dll target/native/TBatteryPowerLib.dll
