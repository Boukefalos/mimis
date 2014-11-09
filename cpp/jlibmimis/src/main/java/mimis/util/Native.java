package mimis.util;

import mimis.value.Registry;
import mimis.value.Windows;

public class Native {
    static {
    	net.rubygrapefruit.platform.Native.load("com.github.boukefalos.mimis", "mimis");
    }

    public native static int getHandle(String window);

    public static int sendMessage(int handle, Windows windows, int wParam, int lParam) {
        return sendMessage(handle, windows.getCode(), wParam, lParam);
    }

    public native static int sendMessage(int handle, int message, int wParam, int lParam);

    public static int postMessage(int handle, Windows windows, int wParam, int lParam) {
        return postMessage(handle, windows.getCode(), wParam, lParam);
    }

    public native static int postMessage(int handle, int message, int wParam, int lParam);
    
    public static int mapVirtualKey(int code, Windows windows) {
        return mapVirtualKey(code, windows.getCode());
    }

    public native static int mapVirtualKey(int code, int type);

    public native static boolean isRunning(String program);

    public native static boolean terminate(String program);

    public static String getValue(Registry registry, String key) {
        return getValue(registry, key, "");
    }

    public static String getValue(Registry registry, String key, String name) {
        return getValue(registry.getCode(), key, name);
    }

    public native static String getValue(int registry, String key, String name);
}
