package test.server;

import java.net.UnknownHostException;
import java.nio.charset.Charset;

import test.server.dummy.DummyUdpServer;
import base.sender.UdpSender;
import base.server.datagram.UdpServer;

public class TestUdpCommunication {
	public static void main(String[] args) {
		// Test Client > Server
		UdpServer server = new DummyUdpServer(1234);
		server.start();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		try {
			UdpSender sender = new UdpSender("255.255.255.255", 1234);
			sender.send("Ciao!".getBytes(Charset.defaultCharset()));
		} catch (UnknownHostException e) {}
	}
}
