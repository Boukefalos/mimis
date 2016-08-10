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

import base.exception.worker.ActivateException;

public class TcpServerClient extends AbstractTcpClient {
    private TcpServer server;

    public TcpServerClient(TcpServer server, Socket socket) {
        this(server, socket, BUFFER_SIZE);
    }

    public TcpServerClient(TcpServer server, Socket socket, Integer bufferSize) {
        super(bufferSize);
        this.server = server;
        this.socket = socket;
    }

    public void activate() throws ActivateException {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            synchronized (object) {
                object.notifyAll();
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
    }

    public void input(byte[] buffer) {
        server.input(this, buffer);        
    }
}
