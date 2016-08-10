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
package base.server.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import base.Sender;
import base.exception.worker.ActivateException;

public class TcpClient extends AbstractTcpClient implements Sender {
    protected static final String HOST = "localhost";

    protected String host;
    protected int port;

    public TcpClient(int port) {
        this(HOST, port);
    }

    public TcpClient(String host, int port) {
        this(host, port, BUFFER_SIZE);
    }

    public TcpClient(String host, int port, int bufferSize) {
        super(bufferSize);
        this.host = host;
        this.port = port;
    }

    public void activate() throws ActivateException {
        try {
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            synchronized (object) {
                object.notifyAll();
            }
        } catch (UnknownHostException e) {
            logger.error("", e);
            throw new ActivateException();
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
        super.activate();
    }

    protected void input(byte[] buffer) {}
}