package test.server.dummy;

import base.server.socket.TcpClient;

public class DummySocketTcpClient extends TcpClient {
	public DummySocketTcpClient(String host, int port) {
		super(host, port);
	}

	public void input(byte[] buffer) {
		System.out.println("Client: " + new String(buffer).trim());
	}
}
