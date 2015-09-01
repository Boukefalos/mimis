package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.server.datagram.UdpMulticastClient;
import base.server.datagram.UdpMulticastServer;

public class TestUdpMulticastCommunication {
    protected UdpMulticastServer server;
    protected TestUdpMulticastClient client;

    @Before
    public void setUp() throws Exception {
        server = new UdpMulticastServer(1234);
        server.start();
        client = new TestUdpMulticastClient(1234);
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.exit();
        server.exit();

        // Should add blocking stop and exit to worker
        while (client.active() || server.active()) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void testServerToClientCommunication() throws Exception {
        String message = "test";
        server.send(message.getBytes());
        System.err.println("send");
        synchronized (client) {
            client.wait(2000);
        }
        byte[] buffer = client.buffer;
        assertNotNull("Received input", buffer);
        assertEquals("Message intact", message, new String(buffer).trim());
    }

    class TestUdpMulticastClient extends UdpMulticastClient {
        public byte[] buffer;

        public TestUdpMulticastClient(int port) {
            super(port);
        }

        public void input(byte[] buffer) {
            this.buffer = buffer;
            synchronized (this) {
                notifyAll();
            }            
        }
    }
}
