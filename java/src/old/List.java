package old;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import mimis.exception.worker.ActivateException;
import mimis.worker.Worker;

import com.Ostermiller.util.BufferOverflowException;
import com.Ostermiller.util.CircularByteBuffer;
import com.Ostermiller.util.CircularObjectBuffer;

public class List extends Worker {
	public static final int STEP = 80;        // milliseconds
	public static final int RATE = 192;       // kbps
	public static final int OVERLAP = 20000;  // milliseconds

	protected File file;
	protected String[] fileArray;

	protected int rate;
	protected int chunk;
	protected int overlap;
	protected byte[] bytes;
	protected boolean next;
	protected Mp3 mp3, nextMp3;

	protected InputStream mp3InputStream;
	protected OutputStream audioOutputStream;
	protected CircularByteBuffer circularByteBuffer;
	protected CircularObjectBuffer<String> circularStringBuffer;

	public List(File file) {
		this(file, RATE);
	}

	public List(File file, int rate) {
		this.file = file;
		this.rate = rate;
		chunk = STEP * rate / 8;
		overlap = OVERLAP * RATE / 8;
		bytes = new byte[chunk];
		next = true;
	}

	public void exit() {
		super.exit();
		if (mp3 != null) {
			mp3.exit();
		}
		if (nextMp3 != null) {
			nextMp3.exit();
		}
	}

	protected synchronized void activate() throws ActivateException {
		try {
			Scanner scanner = new Scanner(file);
			ArrayList<String> fileList = new ArrayList<String>();
			while (scanner.hasNextLine()) {
				fileList.add(scanner.nextLine());
			}
			if (fileList.size() > 0) {
				fileArray = fileList.toArray(new String[0]);

				circularByteBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
				audioOutputStream = circularByteBuffer.getOutputStream();

				circularStringBuffer = new CircularObjectBuffer<String>(CircularByteBuffer.INFINITE_SIZE);
				setNext();
				super.activate();
				notifyAll();
				return;
			}
		} catch (FileNotFoundException e) {
			log.error(e);
		}
		throw new ActivateException();		
	}

	protected synchronized void work() {
		try {
			int left = chunk;
			while (left > 0) {
				/* Check for need to load next mp3 */
				int available = mp3InputStream == null ? -1 : mp3InputStream.available();				
				boolean expect = mp3 == null ? false : mp3.active();

				/* Act when no more data is expected */
				if (!expect) {
					if (available < overlap) {
						setNext();
						next = false;
						nextMp3.start();
					}
					if (available < 1) {
						swap();
					}
				}

				/* Transfer data */
				int read = mp3InputStream.read(bytes, 0, left);
				left -= read;
				audioOutputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			/* Swap to next if stream has stopped */
			setNext();
			swap();
		} catch (IllegalStateException e) {
			log.error(e);
		}
		sleep(STEP);
	}

	protected File getRandomFile() {
		return new File(fileArray[(int) (Math.random() * fileArray.length)]);
	}

	public synchronized void setNext() {
		if (nextMp3 == null) {
			log.debug("Initialize next mp3");
			nextMp3 = new Mp3(getRandomFile(), rate);
		} else if (next) {
			log.debug("Load next mp3");
			nextMp3.setFile(getRandomFile());
		}
	}

	public synchronized void next() {		
		log.debug("Stop current mp3");
		mp3.stop();
	}

	public void swap() {
		log.debug("Swap to next mp3");
		Mp3 swapMp3 = mp3;
		mp3 = nextMp3;
		nextMp3 = swapMp3;
		next = true;

		/* Swap stream and announce title */
		mp3InputStream = mp3.getInputStream();
		try {
			circularStringBuffer.write(mp3.getTitle());
		} catch (BufferOverflowException e) {
			log.error(e);
		} catch (IllegalStateException e) {
			log.error(e);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	public synchronized InputStream getInputStream() {
		if (circularByteBuffer == null) {
			start();
			try {
				wait();
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
		return circularByteBuffer.getInputStream();
	}

	public synchronized CircularObjectBuffer<String> getMetaBuffer() {
		if (circularStringBuffer == null) {
			start();
			try {
				wait();
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
		return circularStringBuffer;
	}

	public static void main(String[] args) {
		int rate = 192;
		List list = new List(new File("mp3"), rate);
		/*Shoutcast shoutcast = new Shoutcast(null, rate, 9876);
		shoutcast.start();
		shoutcast.setInputStream(list.getInputStream());
		shoutcast.setMetaBuffer(list.getMetaBuffer());*/
		while (true) {
			try {
				Thread.sleep(15000);
				list.next();
			} catch (InterruptedException e) {}
		}
	}
}
