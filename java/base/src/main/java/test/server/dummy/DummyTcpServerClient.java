package test.server.dummy;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import base.server.channel.TcpServer;
import base.server.channel.TcpServerClient;

public class DummyTcpServerClient extends TcpServerClient {
	public DummyTcpServerClient(TcpServer server, SocketChannel socketChannel, Integer bufferSize) {
		super(server, socketChannel, bufferSize);
	}

	public void input(byte[] buffer) {
		server.input(this, buffer);		
	}
}
