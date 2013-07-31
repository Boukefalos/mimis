package sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.worker.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Source implements Consumer {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected static final int BUFFER_SIZE = 1024 * 4;	// in bytes
	protected static final int PLAY_FRAMES = 10;		// count

	protected String name;
	protected Producer producer;
	protected InputStream producerInputStream;
	protected Worker worker;

	public Source(String name) throws LineUnavailableException {
		this.name = name;		
	}

	public void start() {
		if (worker != null) {
			worker.start(true);
		}
	}

	public void start(Producer producer) {
		this.producer = producer;
		producerInputStream = producer.getInputStream();
		if (worker != null) {
			worker.exit();
		}
		if (producer instanceof Format.Standard) {
			logger.debug("Format.Standard");
			worker = new DefaultWorker((Format.Standard) producer);
		} else if (producer instanceof Format.Mp3) {
			logger.debug("Format.Mp3");
			worker = new Mp3Worker((Format.Mp3) producer);
		}
		start();
	}

	public void stop() {
		if (worker != null) {
			worker.stop();
		}
	}

	public void exit() {
		if (worker != null) {
			worker.exit();
		}
	}

	protected class DefaultWorker extends Worker {
		protected Format.Standard format;
		protected SourceDataLine line;

		public DefaultWorker(Format.Standard format) {
			this.format = format;
		}

		public void activate() throws ActivateException {
			AudioFormat audioFormat = format.getAudioFormat();
			try {
				if (line == null) {
					line = Tool.getSourceDataLine(name, audioFormat);
				}
				if (!line.isOpen()) {
					line.open();
				}
			} catch (LineUnavailableException e) {
				logger.error("", e);
				throw new ActivateException();
			}
			if (!line.isRunning()) {
				line.start();
			}
			super.activate();
		}

		public void deactivate() throws DeactivateException {
			super.deactivate();
			line.flush();
		}

		public void exit() {
			super.exit();
			line.close();
		}

		protected void work() {
			try {			
				byte[] buffer = new byte[BUFFER_SIZE];
		        int read = producerInputStream.read(buffer, 0, buffer.length);
		        if (read > 0) {
		        	line.write(buffer, 0, read);
		        } else {
		        	exit();
		        }
			} catch (IOException e) {
				logger.error("", e);
				exit();
			}
		}
	}

	protected class Mp3Worker extends Worker {
		protected Format.Mp3 format;
		protected Player player;

		public Mp3Worker(Format.Mp3 format) {
			this.format = format;			
		}

		public void activate() throws ActivateException {
			producer.start();
			super.activate();
		}

		public void deactivate() throws DeactivateException {
			super.deactivate();	
			producer.stop();		
		}

		public void exit() {
			super.exit();
			player.close();
		}

		protected void work() {
			try {
				if (player == null) {
					player = new Player(producerInputStream);
					sleep(500);
				}
				player.play(PLAY_FRAMES);
			} catch (JavaLayerException e) {
				logger.error("", e);
			}
			if (player.isComplete()) {
				stop();
			}
		}
	}
}
