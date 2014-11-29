package jni;

import mimis.util.Native;
import mimis.value.Registry;

public class TestNative {
    public static void main(String[] args) {    	 
      	 System.out.println(Native.getValue(Registry.CURRENT_USER, "Software\\LIRC", "password"));
    }
}