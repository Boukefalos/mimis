/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
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
package mimis.router;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mimis.input.Feedback;
import mimis.input.Task;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public abstract class GlobalRouter extends Router {
    protected String ip;
    protected int port;
    protected Client client;

    public GlobalRouter(String ip, int port)  {
        this.ip = ip;
        this.port = port;
    }

    public void activate() throws ActivateException {
        try {
            client = new Client(ip, port);
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        client.stop();
    }

    public void task(Task task) {
        try {
            client.send(task);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    class Client extends Work {
        protected Socket socket;
        protected ObjectInputStream objectInputStream;
        protected ObjectOutputStream objectOutputStream;

        public Client(String ip, int port) throws IOException {
            socket = new Socket(ip, port);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }

        public void work() {
            try {
                Object object;
                do {
                    object = objectInputStream.readObject();
                    if (object instanceof Feedback) {
                        add((Feedback) object);
                    }                 
                } while (object != null);
            } catch (IOException e) {
                logger.error("", e);
            } catch (ClassNotFoundException e) {
                logger.error("", e);
            }
        }

        public void deactivate() throws DeactivateException {
            super.deactivate();
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();      
            } catch (IOException e) {
                logger.error("", e);
            }
        }

        public void send(Object object) throws IOException {
            objectOutputStream.writeObject(object);
        }
    }
}
