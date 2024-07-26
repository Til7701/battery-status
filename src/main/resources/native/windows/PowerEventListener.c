#include <jni.h>
#include <windows.h>
#include "PowerEventListener.h"

static jobject listenerObject;
static jmethodID onSystemSuspendMethod;
static jmethodID onSystemResumeMethod;

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    if (uMsg == WM_POWERBROADCAST) {
        if (wParam == PBT_APMSUSPEND) {
            JNIEnv* env;
            JavaVM* jvm;
            (*listenerObject)->GetJavaVM(listenerObject, &jvm);
            (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
            (*env)->CallVoidMethod(env, listenerObject, onSystemSuspendMethod);
            (*jvm)->DetachCurrentThread(jvm);
        } else if (wParam == PBT_APMRESUMESUSPEND) {
            JNIEnv* env;
            JavaVM* jvm;
            (*listenerObject)->GetJavaVM(listenerObject, &jvm);
            (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
            (*env)->CallVoidMethod(env, listenerObject, onSystemResumeMethod);
            (*jvm)->DetachCurrentThread(jvm);
        }
    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}

JNIEXPORT void JNICALL Java_PowerEventListener_initPowerEventListener(JNIEnv* env, jobject obj) {
    listenerObject = (*env)->NewGlobalRef(env, obj);

    jclass cls = (*env)->GetObjectClass(env, obj);
    onSystemSuspendMethod = (*env)->GetMethodID(env, cls, "onSystemSuspend", "()V");
    onSystemResumeMethod = (*env)->GetMethodID(env, cls, "onSystemResume", "()V");

    WNDCLASS wndClass = {0};
    wndClass.lpfnWndProc = WindowProc;
    wndClass.hInstance = GetModuleHandle(NULL);
    wndClass.lpszClassName = "PowerEventListenerClass";

    RegisterClass(&wndClass);

    HWND hwnd = CreateWindow("PowerEventListenerClass", "Power Event Listener",
                             0, 0, 0, 0, 0,
                             NULL, NULL, GetModuleHandle(NULL), NULL);

    MSG msg;
    while (GetMessage(&msg, hwnd, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    (*env)->DeleteGlobalRef(env, listenerObject);
}
