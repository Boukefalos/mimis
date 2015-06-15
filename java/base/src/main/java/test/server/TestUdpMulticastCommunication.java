package test.server;

import test.server.dummy.DummyUdpListen;
import test.server.dummy.DummyWriter;
import base.server.datagram.UdpMulticastClient;
import base.server.datagram.UdpMulticastServer;

public class TestUdpMulticastCommunication {
	public static void main(String[] args) {
		// Test Client (multicast) < Server
		String host = "239.255.255.255";
		int port = 4446;

		UdpMulticastServer y = new UdpMulticastServer(host, port);
		y.start();
		UdpMulticastClient x = new UdpMulticastClient(host, port);
		x.start();
		DummyUdpListen z = new DummyUdpListen();
		x.register(z);
		z.start();
		new DummyWriter(y).start();
	
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {}
	}
}
