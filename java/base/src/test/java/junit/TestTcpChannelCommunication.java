package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.server.channel.TcpClient;
import base.server.channel.TcpServer;
import base.server.channel.TcpServerClient;

public class TestTcpChannelCommunication {
    protected TestTcpServer server;
    protected TestTcpClient client;

    @Before
    public void setUp() throws Exception {
        server = new TestTcpServer(1234);
        server.start();
        client = new TestTcpClient(1234);
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.exit();
        server.exit();

        // Should add blocking stop and exit to worker
        while (client.active() || server.active()) {
            Thread.sleep(100);
        }        
    }

    @Test
    public void testSendClientToServer() throws Exception {
        String message = "test";
        client.send(message.getBytes());
        synchronized (server) {
            server.wait(2000);
        }
        byte[] buffer = server.buffer;
        assertNotNull("Received input", buffer);
        assertEquals("Message intact", message, new String(buffer).trim());
    }

    @Test
    public void testSendServerToClient() throws Exception {
        // If client can send, connection has been established
        client.send("init".getBytes());

        String message = "test";
        server.send(message.getBytes());
        synchronized (client) {
            client.wait(2000);
        }
        byte[] buffer = client.buffer;
        assertNotNull("Received input", buffer);
        assertEquals("Message intact", message, new String(buffer).trim());
    }

    class TestTcpServer extends TcpServer {
        public byte[] buffer;

        public TestTcpServer(int port) {
            super(port);
        }

        public void input(TcpServerClient client, byte[] buffer) {
            this.buffer = buffer;
            synchronized (this) {
                notifyAll();
            }                
        }
    }

    class TestTcpClient extends TcpClient {
        public byte[] buffer;

        public TestTcpClient(int port) {
            super(port);
        }

        protected void input(byte[] buffer) {
            this.buffer = buffer;
            synchronized (this) {
                notifyAll();
            }                
        }
        
    }
}
