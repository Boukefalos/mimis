#include "mimis.h"
#include <windows.h>
#include <tlhelp32.h>

bool getProcessEntry32(const char *program, PROCESSENTRY32 *pe32) {
	HANDLE hSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (hSnapshot == INVALID_HANDLE_VALUE) {
		return false;
	}
	bool bFound = false;
	while (Process32Next(hSnapshot, pe32) != false) {
		if (strcmp(program, pe32->szExeFile) == 0) {
			bFound = true;
			break;
		}
	}
	CloseHandle(hSnapshot);
	return bFound;
}

bool getProcess(const char *program, HANDLE *hProcess) {
	PROCESSENTRY32 *pe32 = new PROCESSENTRY32;
	bool bResult = false;
	if (getProcessEntry32(program, pe32)) {
		*hProcess = OpenProcess(PROCESS_ALL_ACCESS, false, pe32->th32ProcessID);
		bResult = true;
	}
	delete pe32;
	return bResult;
}

JNIEXPORT jint JNICALL Java_mimis_util_Native_getHandle(JNIEnv *env, jclass cls, jstring jwindow) {
	const char *window = env->GetStringUTFChars(jwindow, 0);
	return (int) FindWindow(window, NULL);
}

JNIEXPORT jint JNICALL Java_mimis_util_Native_sendMessage(JNIEnv *env, jclass cls, jint handle, jint message, jint wParam, jint lParam) {
	return SendMessage((HWND) handle, message, wParam, lParam);
}

JNIEXPORT jint JNICALL Java_mimis_util_Native_postMessage(JNIEnv *env, jclass cls, jint handle, jint message, jint wParam, jint lParam) {
	return PostMessage((HWND) handle, message, wParam, lParam);
}

JNIEXPORT jint JNICALL Java_mimis_util_Native_mapVirtualKey(JNIEnv *env, jclass cls, jint map, jint type) {
	return MapVirtualKey(map, type);
}

JNIEXPORT jboolean JNICALL Java_mimis_util_Native_isRunning(JNIEnv *env, jclass cls, jstring jprogram) {
	const char *program = env->GetStringUTFChars(jprogram, 0);
	PROCESSENTRY32 *pe32 = new PROCESSENTRY32;
	bool bRunning = getProcessEntry32(program, pe32);
	delete pe32;
	return bRunning;
}

JNIEXPORT jboolean JNICALL Java_mimis_util_Native_terminate(JNIEnv *env, jclass cls, jstring jprogram) {
	const char *program = env->GetStringUTFChars(jprogram, 0);
	HANDLE *hProcess = new HANDLE;
	bool bResult = false;
	if (getProcess(program, hProcess)) {
		bResult = TerminateProcess(*hProcess, 0);
	}
	delete hProcess;
	return bResult;
}

JNIEXPORT jstring JNICALL Java_mimis_util_Native_getValue(JNIEnv *env, jclass cls, jint registry, jstring jkey, jstring jname) {
	const char *key = env->GetStringUTFChars(jkey, 0);
	const char *name = env->GetStringUTFChars(jname, 0);
	HKEY hKey;
	char *value = NULL;
	if (RegOpenKey((HKEY) registry, key, &hKey) == ERROR_SUCCESS) {
		char nameBuffer[255];
		byte valueBuffer[255];
		DWORD dwNameSize = sizeof(nameBuffer);
		DWORD dwValueSize = sizeof(valueBuffer);
		int i = 0;
		while (RegEnumValue(hKey, i++, nameBuffer, &dwNameSize, NULL, NULL, valueBuffer, &dwValueSize) == ERROR_SUCCESS) {
			if (strcmp(name, nameBuffer) == 0) {
				value = (char*) valueBuffer;
				break;
			}
			dwNameSize = sizeof(nameBuffer);
		}
	}
	RegCloseKey(hKey);
	return env->NewStringUTF(value);
}
