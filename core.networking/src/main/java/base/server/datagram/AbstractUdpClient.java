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
import java.net.MulticastSocket;

import base.work.Work;

public abstract class AbstractUdpClient extends Work {
    protected static final int BUFFER_SIZE = 2048;

    protected int bufferSize;
    protected MulticastSocket socket;
    protected DatagramPacket datagramPacket;

    public AbstractUdpClient() {}

    public AbstractUdpClient(MulticastSocket socket) {
        this(socket, BUFFER_SIZE);
    }

    public AbstractUdpClient(MulticastSocket socket, int bufferSize) {
        this.socket = socket;
        this.bufferSize = bufferSize;
        byte[] buffer = new byte[bufferSize];
        datagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    public void work() {        
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            System.out.println("iets ontvangen!!!!!");
            buffer = packet.getData();
            input(buffer);
        } catch (IOException e) {}
    }

    public void stop() {
        socket.close();
        super.stop();
    }

    protected abstract void input(byte[] buffer);
}
