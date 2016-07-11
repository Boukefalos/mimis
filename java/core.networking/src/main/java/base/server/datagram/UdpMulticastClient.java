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
package base.server.datagram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.exception.worker.ActivateException;

public class UdpMulticastClient extends AbstractUdpClient {
    protected static final String HOST = "239.255.255.255";

    protected String host;
    protected int port;

    public UdpMulticastClient(int port) {
        this(HOST, port);
    }

    public UdpMulticastClient(String host, int port) {
        this(host, port, BUFFER_SIZE);
    }

    public UdpMulticastClient(String host, int port, int bufferSize) {
        this.host = host;
        this.port = port;
        this.bufferSize = BUFFER_SIZE;
        System.out.println("Client bind: " + host + " " + port);
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(host);
            socket.joinGroup(group);
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
    }

    protected void input(byte[] buffer) {}
}
