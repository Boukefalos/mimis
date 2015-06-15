package test.server.dummy;

import base.server.socket.TcpClient;

public class DummyChannelTcpClient extends TcpClient {
	public DummyChannelTcpClient(String host, int port) {
		super(host, port);
	}

	public void input(byte[] buffer) {
		System.out.println("Client: " + new String(buffer).trim());
	}
}
