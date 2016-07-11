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
import java.net.DatagramSocket;
import java.net.SocketException;

import base.exception.worker.ActivateException;
import base.work.Work;

public class UdpServer extends Work {
    protected static final int BUFFER_SIZE = 1024;
    protected static final int TIMEOUT = 1000;

    protected int port;
    protected int bufferSize;
    protected DatagramSocket diagramSocket;

    public UdpServer(int port) {
        this(port, BUFFER_SIZE);
    }

    public UdpServer(int port, int bufferSize) {
        super();
        this.port = port;
        this.bufferSize = bufferSize;
    }

    public void activate() throws ActivateException {
        try {
            logger.debug("Starting datagram socket on port " + port);
            diagramSocket = new DatagramSocket(port);
            diagramSocket.setSoTimeout(TIMEOUT);
            super.activate();
        } catch (SocketException e) {
            logger.error("Failed to initialize socket", e);
            throw new ActivateException();
        }
    }

    public void stop() {
        super.stop();
        if (diagramSocket != null) {
            diagramSocket.close();
        }
    }

    public void work() {
        byte[] buffer = new byte[bufferSize];        
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        try {
            diagramSocket.receive(datagramPacket);
        } catch (SocketException e) {
            stop();
        } catch (IOException e) {
            logger.error("Failed to receive packet", e);
            stop();
            return;
        }
        input(buffer);
    }

    protected void input(byte[] buffer) {}
}
