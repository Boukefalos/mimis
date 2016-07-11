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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.Sender;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Listen;
import base.worker.Worker;

public class UdpMulticastServer extends Listen<byte[]> implements Sender {
    protected static final String HOST = "239.255.255.255";
    protected static final int BUFFER_SIZE = 2048;

    protected String host;
    protected int port;
    protected MulticastSocket socket;

    public UdpMulticastServer(int port) {
        this(HOST, port);
    }

    public UdpMulticastServer(String host, int port) {
        super(Worker.Type.BACKGROUND);
        this.host = host;
        this.port = port;
        System.out.println("Server send: " + host + " " + port);
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            throw new ActivateException();
        }
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        socket.close();
        super.deactivate();
    }

    public void input(byte[] buffer) {
        try {
            InetAddress group = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        }
        catch (IOException e) {
            logger.error("", e);
        }    
    }

    public void send(byte[] buffer) throws IOException {
        add(buffer);
    }
}
