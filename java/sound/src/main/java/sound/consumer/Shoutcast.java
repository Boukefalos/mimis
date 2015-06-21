package sound.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import sound.Consumer;
import sound.Producer;
import sound.data.Data;
import sound.util.Buffer;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Listener;
import base.worker.Worker;

import com.Ostermiller.util.CircularObjectBuffer;

public class Shoutcast extends Worker implements Consumer {
	public static final int PORT = 9876;
	public static final int RATE = 192; // in kbps
	public static final int META = 8192; // in bytes
	public static final int STEP = 80; // in milliseconds
	public static final int BUFFER = 2000; // in bytes
	public static final int BUFFERING = 500; // in milliseconds
	public static final String DATA = "StreamTitle='%s';StreamUrl='%s';";
	protected int rate;
	protected int port;
	protected Server server;
	protected HashMap<String, String> headerMap;
	protected ConcurrentLinkedQueue<Client> clientList;
	protected InputStream producerInputStream;
	protected int chunk;
	protected Buffer buffer;
	protected byte[] bytes;
	protected Data data;
	protected String metaData;
	private CircularObjectBuffer<String> circularStringBuffer;

	public Shoutcast() {
		this(RATE, PORT);
	}

	public Shoutcast(int rate) {
		this(rate, PORT);
	}

	public Shoutcast(int rate, int port) {
		this.rate = rate;
		this.port = port;
		clientList = new ConcurrentLinkedQueue<Client>();
		metaData = "";

		chunk = STEP * rate / 8;
		bytes = new byte[chunk];
		buffer = new Buffer(BUFFER * rate / 8);

		headerMap = new HashMap<String, String>();
		headerMap.put("icy-notice1", "This stream requires <a href=\"http://www.winamp.com/\">Winamp</a>");
		headerMap.put("icy-notice2", "Java SHOUTcast Server");
		headerMap.put("icy-name", "Java Radio");
		headerMap.put("icy-genre", "Java");
		headerMap.put("icy-url", "http://localhost");
		headerMap.put("content-type:", "audio/mpeg");
		headerMap.put("icy-pub", "0");
		headerMap.put("icy-metaint", String.valueOf(META));
		headerMap.put("icy-br", String.valueOf(rate));
	}

	public void activate() throws ActivateException {
		logger.trace("Activate Server");
		server = new Server(port);
		server.start();
		super.activate();
	}

	public boolean active() {
		return active = server.active();
	}

	public void deactivate() throws DeactivateException {
		super.deactivate();
		server.stop();
	}

	public void work() {
		int progress;
		try {
			int read = 0;
			if (producerInputStream != null) {
				while (producerInputStream.available() < buffer.capacity) {
					progress = (int) (producerInputStream.available() / (buffer.capacity / 100.0F));
					logger.debug("Filling buffer: " + progress + "%");
					sleep(BUFFERING);
				}
				read = producerInputStream.read(bytes);
			}
			data = new Data(bytes, read);
			buffer.write(bytes, 0, read);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		for (Client client : clientList) {
			if (client.active) {
				client.add(data);
			}
		}
		sleep(STEP);
	}

	public void setMetaBuffer(CircularObjectBuffer<String> circularStringBuffer) {
		logger.debug("Set meta input stream");
		this.circularStringBuffer = circularStringBuffer;
	}

	public void setMeta(String meta) {
		logger.debug("Set meta string: " + meta);
		metaData = meta;
	}

	protected class Client extends Listener<Data> {
		protected Socket socket;
		protected InputStream inputStream;
		protected OutputStream outputStream;
		protected boolean writeMeta;
		protected int untilMeta;
		protected boolean active;

		public Client(Socket socket) throws IOException {
			this.socket = socket;
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			active = false;
			clientList.add(this);
		}

		public void activate() throws ActivateException {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			try {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.startsWith("Icy-MetaData")) {
						writeMeta = Integer.valueOf(line.substring(line.indexOf(":") + 1).trim()).intValue() == 1;
						untilMeta = META;
					} else if (line.equals("")) {
						break;
					}
				}
				logger.debug(String.format("Client accept meta: %s", Boolean.valueOf(writeMeta)));

				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
				outputStreamWriter.write("ICY 200 OK\r\n");
				for (Entry<String, String> header : headerMap.entrySet()) {
					outputStreamWriter.write(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
				}
				outputStreamWriter.write("\r\n");
				outputStreamWriter.flush();

				add(new Data(buffer.get()));
				active = true;
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new ActivateException();
			}
			super.activate();
		}

		public void exit() {
			logger.debug("Client exit");
			super.exit();
			clientList.remove(this);
			try {
				inputStream.close();
				outputStream.close();
				socket.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		public void input(Data data) {
			try {
				byte[] bytes = data.get();
				if (writeMeta) {
					int offset = 0;
					while (data.length() - offset >= untilMeta) {
						outputStream.write(bytes, offset, untilMeta);
						writeMeta();
						offset += untilMeta;
						untilMeta = META;
					}
					int length = data.length() - offset;
					outputStream.write(bytes, offset, length);
					untilMeta -= length;
				} else {
					outputStream.write(bytes);
				}
			} catch (SocketException e) {
				exit();
			} catch (IOException e) {
				exit();
			}
		}

		protected void writeMeta() throws IOException {
			if ((circularStringBuffer != null)
					&& (circularStringBuffer.getAvailable() > 0)) {
				try {
					String newMetaData = circularStringBuffer.read();
					if (!newMetaData.isEmpty() && !newMetaData.equals(metaData)) {
						metaData = newMetaData;
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}

			String meta = String.format("StreamTitle='%s';StreamUrl='%s';", metaData, "???");
			byte[] metaBytes = meta.getBytes();

			int length = (int) Math.ceil(metaBytes.length / 16.0F);
			outputStream.write(length);
			outputStream.write(metaBytes);

			int padding = 16 * length - metaBytes.length;
			outputStream.write(new byte[padding], 0, padding);
		}
	}

	protected class Server extends Worker {
		protected int port;
		protected ServerSocket serverSocket;

		public Server(int port) {
			this.port = port;
		}

		public boolean active() {
			return active = serverSocket.isClosed() ? false : true;
		}

		public void activate() throws ActivateException {
			try {
				serverSocket = new ServerSocket(port);
				logger.debug("Server listening at port " + port);
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new ActivateException();
			}
			super.activate();
		}

		public void work() {
			try {
				Socket socket = serverSocket.accept();
				logger.trace("Client connected: " + socket.getInetAddress().toString());
				Shoutcast.Client client = new Shoutcast.Client(socket);
				client.start();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		public void deactivate() throws DeactivateException {
			logger.debug("Server deactivate");
			super.deactivate();
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			for (Shoutcast.Client client : clientList) {
				client.stop();
			}
		}
	}

	public void start(Producer producer) {
		start(producer, THREAD);
	}

	public void start(Producer producer, boolean thread) {
		producerInputStream = producer.getInputStream();
		producer.start();
		start(thread);
	}
}
