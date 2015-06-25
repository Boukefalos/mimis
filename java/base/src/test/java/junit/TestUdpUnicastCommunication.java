package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.server.datagram.UdpSender;
import base.server.datagram.UdpServer;

public class TestUdpUnicastCommunication {
	protected TestUdpServer server;
	protected UdpSender sender;

	@Before
	public void setUp() throws Exception {
		server = new TestUdpServer(1234);
		server.start();
		sender = new UdpSender(1234);
	}

	@After
	public void tearDown() throws Exception {
		server.exit();

		// Should add blocking stop and exit to worker
		while (server.active()) {
			Thread.sleep(100);
		}		
	}

	@Test
	public void testSendClientToServer() throws Exception {
		String message = "test";
		sender.send(message.getBytes());
		synchronized (server) {
			server.wait(2000);
		}
		byte[] buffer = server.buffer;
		assertNotNull("Received input", buffer);
		assertEquals("Message intact", message, new String(buffer).trim());
	}

	class TestUdpServer extends UdpServer {
		public byte[] buffer;

		public TestUdpServer(int port) {
			super(port);
		}

		@Override
		protected void input(byte[] buffer) {
			this.buffer = buffer;
			synchronized (this) {
				notifyAll();
			}			
		}
	}
}
