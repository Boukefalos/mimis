package sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Target implements Producer, Format.Standard {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Standard format;

	protected TargetDataLine line;
	protected InputStream targetInputStream;

	protected AudioFormat audioFormat;

	public Target(String name) throws LineUnavailableException {
		logger.debug(String.format("Target \"%s\" without format", name));
		line = Tool.getTargetDataLine(name);
		audioFormat = line.getFormat();
		targetInputStream = new TargetInputStream();
	}

	public Target(String name, AudioFormat audioFormat) throws LineUnavailableException {
		logger.debug(String.format("Target \"%s\" with format: %s", name, audioFormat));
		this.audioFormat = audioFormat;
		line = Tool.getTargetDataLine(name, audioFormat);
		targetInputStream = new TargetInputStream();
	}

	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public InputStream getInputStream() {
		return targetInputStream;
	}

	public class TargetInputStream extends InputStream {
		protected boolean open;

		public TargetInputStream() {
			open = false;
		}

		public int read() throws IOException {
			start();
			byte[] buffer = new byte[1];
			line.read(buffer, 0, 1);
			return (int) buffer[0];
		}

		public int read(byte[] buffer, int offset, int length) {
			start();
			line.read(buffer, offset, length);
			return length;
		}

		public int available() {
			start();
			return line.available();
		}
	}

	public void start() {
		if (!line.isOpen()) {
			try {
				line.open();
			} catch (LineUnavailableException e) {
				logger.error("", e);
			}
		}
		if (!line.isRunning()) {
			line.start();
		}		
	}

	public void stop() {
		line.flush();
	}

	public void exit() {
		line.close();		
	}
}
