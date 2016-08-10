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
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Sender;

public class UdpSender implements Sender {
    protected static final String HOST = "localhost";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected DatagramSocket datagramSocket;
    protected InetAddress inetAddress;
    protected int port;

    public UdpSender(int port) throws UnknownHostException {
        this(HOST, port);
    }

    public UdpSender(String host, int port) throws UnknownHostException {
        System.out.println("Sender use: " + host + " " + port);
        inetAddress = InetAddress.getByName(host);
        this.port = port;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            logger.error("Failed to create socket", e);
        }        
    }

    public void start() {}

    public void stop() {
        datagramSocket.close();
    }

    public void exit() {
        stop();
    }

    public void send(byte[] buffer) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            logger.error("Failed to send buffer", e);
        }        
    }
}
