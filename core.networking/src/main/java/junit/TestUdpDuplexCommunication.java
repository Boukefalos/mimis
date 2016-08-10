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

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.server.datagram.UdpDuplexAutoClient;
import base.server.datagram.UdpDuplexServer;

public class TestUdpDuplexCommunication {
    protected TestUdpDuplexServer server;
    protected TestUdpDuplexClient client;

    @Before
    public void setUp() throws Exception {
        server = new TestUdpDuplexServer(1234, 1235);
        server.start();
        client = new TestUdpDuplexClient(1234, 1235);
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

    @Test
    public void testClientToServerCommunication() throws Exception {
        // Let client discover server address
        testServerToClientCommunication();

        String message = "test";
        client.send(message.getBytes());
        System.err.println("send");
        synchronized (server) {
            server.wait(2000);
        }
        byte[] buffer = server.buffer;
        assertNotNull("Received input", buffer);
        assertEquals("Message intact", message, new String(buffer).trim());
    }

    public class TestUdpDuplexServer extends UdpDuplexServer {
        public byte[] buffer;

        public TestUdpDuplexServer(int sendPort, int bindPort) {
            super(sendPort, bindPort);
        }

        public void input(byte[] buffer) {
            this.buffer = buffer;
            synchronized (this) {
                notifyAll();
            }            
        }
    }

    class TestUdpDuplexClient extends UdpDuplexAutoClient {
        public byte[] buffer;

        public TestUdpDuplexClient(int bindPort, int sendPort) throws UnknownHostException {
            super(bindPort, sendPort);
        }

        public void input(byte[] buffer) {
            this.buffer = buffer;
            synchronized (this) {
                notifyAll();
            }            
        }
    }
}
