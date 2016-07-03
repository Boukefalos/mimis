package jni;

import de.hardcode.jxinput.JXInputManager;

public class TestXinput {
    public static void main(String[] args) {
        System.out.println(JXInputManager.getNumberOfDevices());
    }
}