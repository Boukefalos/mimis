package test.server;

import test.server.dummy.DummyChannelTcpClient;
import test.server.dummy.DummySocketTcpClient;
import test.server.dummy.DummyTcpServerClient;
import test.server.dummy.DummyWriter;
import base.server.channel.TcpServer;

public class TestTcpCommunication {
	public static void main(String[] args) {
		try {
			// Test client > server
			new TcpServer(1234, DummyTcpServerClient.class).start();
			DummySocketTcpClient client1 = new DummySocketTcpClient("localhost", 1234);	
			DummyChannelTcpClient client2 = new DummyChannelTcpClient("localhost", 1234);
			
			client1.start();
			client2.start();
			Thread.sleep(1000);
			client1.send("Succes!".getBytes());
			new DummyWriter(client1).start();
		} catch (Exception e) {}
	}
}
