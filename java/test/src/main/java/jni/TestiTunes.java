package jni;

import com.dt.iTunesController.iTunes;

public class TestiTunes {
	public static void main(String[] args) {
		iTunes iTunes = new iTunes();
		iTunes.connect();
		iTunes.playPause();
	}
}