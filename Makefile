JAVA_HOME := $(shell echo $$JAVA_HOME)

all: PowerEventLib.dll

PowerEventLib.dll: src/main/c/de_holube_batterystatus_jni_PowerEventListener.c
	gcc -shared -o src/main/c/PowerEventLib.dll -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/win32" -I"src/main/c/" src/main/c/de_holube_batterystatus_jni_PowerEventListener.c
	mkdir -p "target/native"
	mv src/main/c/PowerEventLib.dll target/native/PowerEventLib.dll
