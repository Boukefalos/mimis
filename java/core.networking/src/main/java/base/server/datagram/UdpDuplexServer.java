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

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public class UdpDuplexServer extends UdpMulticastServer {
    protected int bindPort;
    protected UdpDuplexHelper helper;

    public UdpDuplexServer(int sendPort, int bindPort) {
        super(sendPort);
        this.bindPort = bindPort;
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket(bindPort);
            synchronized (this) {
                notifyAll();
            }
            helper = new UdpDuplexHelper(this, socket);
            helper.start();
        } catch (IOException e) {
            throw new ActivateException();
        }
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        helper.stop();
        super.deactivate();
    }

    public void send(byte[] buffer) throws IOException {
        if (socket == null) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        try {
            InetAddress group = InetAddress.getByName(host);
            System.out.println("Send to " + host + " " + port);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        }
        catch (IOException e) {
            logger.error("", e);
        }
    }
}
