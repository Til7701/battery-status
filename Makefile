JAVA_HOME := $(shell echo $$JAVA_HOME)

all: PowerEventLib.dll

PowerEventLib.dll: src/main/resources/native/windows/PowerEventListener.c
	gcc -shared -o src/main/resources/native/windows/PowerEventLib.dll -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/win32" src/main/resources/native/windows/PowerEventListener.c
