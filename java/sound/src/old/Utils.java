package old;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Utils {
	public static final int BUFFER = 2048; // bytes

	public static void play(InputStream inputStream) {
		try {
			Thread.sleep(5000);
			new Player(new BufferedInputStream(inputStream)).play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void write(InputStream inputStream, File file) {
		byte[] bytes = new byte[BUFFER];
		int read = 0;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			while ((read = inputStream.read(bytes)) > 0) {
				fileOutputStream.write(bytes, 0, read);
			}
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
