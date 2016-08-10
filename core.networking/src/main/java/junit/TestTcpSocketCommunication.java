/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.server.socket.TcpClient;
import base.server.socket.TcpServer;
import base.server.socket.TcpServerClient;

public class TestTcpSocketCommunication {
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
