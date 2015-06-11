package base.sender;

import java.io.IOException;

import base.server.socket.TcpClient;

public abstract class TcpSender extends TcpClient implements Sender {
	public TcpSender(String host, int port) {
		super(host, port);
	}

	public void send(byte[] buffer) throws IOException {
		if (!active()) {
			start();
			// Control over threads here?
		}
		outputStream.write(buffer);		
	}
}
