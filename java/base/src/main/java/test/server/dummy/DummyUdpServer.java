package test.server.dummy;

import base.server.datagram.UdpServer;

public class DummyUdpServer extends UdpServer {
	public DummyUdpServer(int port) {
		super(port);
	}

	protected void listen(byte[] buffer) {
		logger.debug("Server: " + new String(buffer).trim());		
	}
}
