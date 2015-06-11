package sound.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import base.server.socket.TcpServer;

import com.Ostermiller.util.CircularObjectBuffer;

public class Shoutcast extends TcpServer {
	protected int metadataInterval = 8192;
	protected int rate;
	protected CircularObjectBuffer<String> metaBuffer;
	protected InputStream inputStream;
	
	public Shoutcast(int rate, int port) {
		super(port, ShoutcastClient.class);
		this.rate = rate;
	}

	public void x() {
		// Accept new clients
		// Transfer buffer
		
		
		StringBuilder response = new StringBuilder();
		response.append("HTTP/1.1 200 OK\r\nContent-Type: audio/mpeg\r\n");
	
		// add the stream name
		response.append("icy-name: " + "hallo" + "\r\n");
		
		// add metadata information
		response.append("icy-metadata:1\r\n");
		response.append("icy-metaint:");
		response.append(metadataInterval );
		response.append("\r\n");
		
		response.append("\r\n");
		
		//out.write(response.toString().getBytes());
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;		
	}

	public void setMetaBuffer(CircularObjectBuffer<String> metaBuffer) {
		this.metaBuffer = metaBuffer;		
	}

	public class ShoutcastClient extends TcpServer.Client {
		int untilMeta = 0;

		public ShoutcastClient(Socket socket) {
			super(socket);
		}

		public void work() {
			// 
			byte[] buffer = new byte[123];
			try {
				outputStream.write(buffer);
				// Write some meta

			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

}
