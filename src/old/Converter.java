package old;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.worker.Worker;

import com.Ostermiller.util.CircularByteBuffer;

public class Converter extends Worker {
	public static final String COMMAND = "lame --mp3input --cbr %s - - --quiet";
	public static final int BYTES = 4096;     // bytes
	public static final int BUFFER = 30000;   // milliseconds
	public static final int BUFFERING = 1000; // milliseconds

	protected int targetRate;
	protected int rate;
	protected int buffer;
	protected boolean convert;

	protected Process process;
	protected InputStream sourceInputStream, processInputStream, inputStream;
	protected OutputStream processOutputStream;
	protected CircularByteBuffer circularByteBuffer;
	protected BufferWorker bufferWorker;

	public Converter(InputStream inputStream) {
		this(inputStream, -1);		
	}

	public Converter(InputStream inputStream, int targetRate) {
		this.sourceInputStream = inputStream;
		this.targetRate = targetRate;
		bufferWorker = new BufferWorker();
		convert = false;
	}
	
    public void exit() {
        super.exit();
        bufferWorker.exit();
    }

	public synchronized void activate() throws ActivateException {
		/* Read bitrate */
		BufferedInputStream bufferedInputStream = new BufferedInputStream(sourceInputStream);
		Bitstream bitStream = new Bitstream(bufferedInputStream);
		try {
			rate = bitStream.readFrame().bitrate() / 1000;
			buffer = BUFFER * rate / 8;
		} catch (BitstreamException e) {
			log.error(e);
			throw new ActivateException();
		}

		/* Check for need to convert */
		if (targetRate < 0 || rate == targetRate) {
			log.debug("No conversion required");
			inputStream = sourceInputStream;
		} else {
			log.debug("Converting from " + rate + "kbps to " + targetRate + "kbps");
			try {
				String command = String.format(COMMAND, rate > targetRate ? "-B " + targetRate : "-F -b " + targetRate);
				log.debug("Starting process: " + command);
				process = Runtime.getRuntime().exec(command);
				processInputStream = process.getInputStream();
				processOutputStream = process.getOutputStream();

				/* Buffer output */
				circularByteBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
				inputStream = circularByteBuffer.getInputStream();
				bufferWorker.start();
				convert = true;
			} catch (IOException e) {
				log.error(e);
				throw new ActivateException();
			}
		}
		super.activate();
		notifyAll();
	}

	protected void deactivate() throws DeactivateException {
		super.deactivate();
		try {
			sourceInputStream.close();
			bufferWorker.stop();
			if (convert) {
				circularByteBuffer.clear();
				convert = false;
			}
			inputStream.close();
		} catch (IOException e) {
			log.error(e);
			throw new DeactivateException();
		}
	}

	protected void work() {
		if (!convert) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				log.error(e);
			}
			return;
		}
		byte[] bytes = new byte[BYTES];
		int read = 0;
		try {
			log.debug("Writing input to process");
			while ((read = sourceInputStream.read(bytes)) > 0 && !deactivate) {
				/* Limit buffer size */
				while (inputStream.available() > buffer) {
	  				int progress = (int) ((1 - (inputStream.available() - buffer) / (float) buffer) * 100);
					log.trace("Waiting for buffer to empty: " + progress + "%");
					sleep(BUFFERING);
				}
				processOutputStream.write(bytes, 0, read);
			}
			processOutputStream.close();
			log.debug("Stopped writing input to process");
			process.waitFor();
			log.debug("Process finished");
		} catch (IOException e) {
		} catch (InterruptedException e) {
			log.error(e);
		}
		stop();
	}

	public synchronized InputStream getInputStream() {
		if (!active()) {
			if (!activate) {
				start();
			}
			try {
				wait();
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
		return inputStream;
	}

	public synchronized void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;	
	}

	class BufferWorker extends Worker {
		protected void work() {
			byte[] bytes = new byte[BYTES];
			int read = 0;
			try {
				OutputStream bufferOutputStream = circularByteBuffer.getOutputStream();
				log.debug("Start buffering process output");
				while ((read = processInputStream.read(bytes, 0, BYTES)) > 0) {
					bufferOutputStream.write(bytes, 0, read);
				}
				log.debug("Finished buffering process output");
				bufferOutputStream.close();
			} catch (IOException e) {}
			stop();
		}
	}

	public static void main(String[] args) {
		Mp3 mp3 = new Mp3(new File("stream.mp3"), 128);
		InputStream inputStream = mp3.getInputStream();

		/* Play */
		//Utils.play(inputStream);

		/* Write to file */
		Utils.write(inputStream, new File("output.mp3"));
		mp3.exit();
	}
}
