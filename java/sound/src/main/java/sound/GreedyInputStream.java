package sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreedyInputStream extends BufferedInputStream {
	protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final int SLEEP = 500; // in milliseconds
	protected static final int BUFFER_SIZE = 30000; // in bytes
	protected static final int MINIMUM_SIZE = 1000; // in bytes

	protected int bufferSize;
	protected int minimumSize;
	protected boolean hoard;

	public GreedyInputStream(InputStream inputStream) {
		this(inputStream, BUFFER_SIZE, MINIMUM_SIZE);
	}

	public GreedyInputStream(InputStream inputStream, int bufferSize) {
		super(inputStream, bufferSize);		
		this.bufferSize = bufferSize;
		hoard = true;
	}
	
	public GreedyInputStream(InputStream inputStream, int bufferSize, int minimumSize) {
		this(inputStream, bufferSize);		
		this.minimumSize = minimumSize;
	}

	public int read() throws IOException {
		hoard();
		byte[] buffer = new byte[1];
		in.read(buffer, 0, 1);
		return (int) buffer[0];
	}

	public int read(byte[] buffer, int offset, int length) throws IOException {
		hoard();
		in.read(buffer, offset, length);
		return length;
	}

	public void hoard() throws IOException {
		int available =  available();
		if (hoard && available < MINIMUM_SIZE) {
			long time = System.currentTimeMillis();
			do {
				try {
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
					logger.warn("", e);
				}
			} while (available() < BUFFER_SIZE);
			logger.debug(String.format("Buffered %d bytes in %s milliseconds", BUFFER_SIZE - available, System.currentTimeMillis() - time));
		}
	}

	public void clear() throws IOException {
		this.buf = new byte[buf.length];
		reset();		
	}

	public void drain() {
		drain(true);
	}

	public void drain(boolean drain) {
		hoard = !drain;
	}
}
