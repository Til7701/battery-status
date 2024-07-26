#include <jni.h>
#include <windows.h>
#include "de_holube_batterystatus_jni_TBatteryPowerLib.h"

static JavaVM* jvm;
static jobject listenerObject;
static jmethodID onSystemSuspendMethod;
static jmethodID onSystemResumeMethod;

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    if (uMsg == WM_POWERBROADCAST) {
        JNIEnv* env;
        (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);

        if (wParam == PBT_APMSUSPEND) {
            (*env)->CallVoidMethod(env, listenerObject, onSystemSuspendMethod);
        } else if (wParam == PBT_APMRESUMESUSPEND) {
            (*env)->CallVoidMethod(env, listenerObject, onSystemResumeMethod);
        }

        (*jvm)->DetachCurrentThread(jvm);
    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}

JNIEXPORT void JNICALL Java_de_holube_batterystatus_jni_TBatteryPowerLib_initTBatteryPowerLib(JNIEnv* env, jobject obj) {
    (*env)->GetJavaVM(env, &jvm);
    listenerObject = (*env)->NewGlobalRef(env, obj);

    jclass cls = (*env)->GetObjectClass(env, obj);
    onSystemSuspendMethod = (*env)->GetMethodID(env, cls, "onSystemSuspend", "()V");
    onSystemResumeMethod = (*env)->GetMethodID(env, cls, "onSystemResume", "()V");

    WNDCLASS wndClass = {0};
    wndClass.lpfnWndProc = WindowProc;
    wndClass.hInstance = GetModuleHandle(NULL);
    wndClass.lpszClassName = "TBatteryPowerLibClass";

    RegisterClass(&wndClass);

    HWND hwnd = CreateWindow("TBatteryPowerLibClass", "Power Event Listener",
                             0, 0, 0, 0, 0,
                             NULL, NULL, GetModuleHandle(NULL), NULL);

    // Main loop to keep the window alive
    MSG msg;
    while (GetMessage(&msg, hwnd, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    (*env)->DeleteGlobalRef(env, listenerObject);
}

JNIEXPORT jint JNICALL Java_de_holube_batterystatus_jni_TBatteryPowerLib_getBatteryPercentage(JNIEnv* env, jobject obj) {
    SYSTEM_POWER_STATUS sps;
    if (GetSystemPowerStatus(&sps)) {
        return sps.BatteryLifePercent;
    }
    return -1; // Return -1 if unable to get battery status
}
