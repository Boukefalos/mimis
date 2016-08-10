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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import base.Sender;
import base.exception.worker.DeactivateException;
import base.work.Work;

public abstract class AbstractTcpClient extends Work implements Sender {
    protected static final int BUFFER_SIZE = 1024;

    protected Object object = new Object();
    protected int bufferSize;
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    public AbstractTcpClient(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public boolean active() {
        return super.active() && socket.isConnected();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public void exit() {
        super.exit();
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public void work() {
        byte[] buffer = new byte[bufferSize];
        try {
            while (inputStream.read(buffer) > 0) {
                input(buffer);
            }
        } catch (IOException e) {
            stop();
        }
    }

    protected abstract void input(byte[] buffer);

    public void send(byte[] buffer) throws IOException {
        if (outputStream == null) {
            try {
                synchronized (object) {
                    object.wait();
                }
            } catch (InterruptedException e) {}
        }
        outputStream.write(buffer);
    }
}
