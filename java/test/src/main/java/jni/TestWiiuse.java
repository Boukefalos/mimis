package jni;

import wiiusej.WiiUseApiManager;

public class TestWiiuse {
    public static void main(String[] args) {
        WiiUseApiManager.getWiimotes(0, false);
    }
}