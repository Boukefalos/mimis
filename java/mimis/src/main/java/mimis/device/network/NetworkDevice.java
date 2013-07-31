package mimis.device.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import mimis.device.Device;
import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.input.Feedback;
import mimis.input.Input;
import mimis.input.Task;
import mimis.value.Action;
import mimis.value.Target;
import mimis.worker.Component;
import mimis.worker.Worker;

public class NetworkDevice extends Component implements Device {
    protected static final String TITLE = "Network";
    public static final int PORT = 6789;

    protected Server server;
    protected ConcurrentLinkedQueue<Client> clientList;

    public NetworkDevice(int port) {
        super(TITLE);
        clientList = new ConcurrentLinkedQueue<Client>();
        server = new Server(port);
    }

    public NetworkDevice() {
        this(PORT);
    }

    protected void activate() throws ActivateException {
        server.start();
        super.activate();
    }

    public boolean active() {
        for (Client client : clientList) {
            if (!client.active()) {
                client.stop();
            }
        }
        return active = server.active();
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        server.stop();
    }

    public synchronized void exit() {
        super.exit();
        server.exit();
    }

    protected void feedback(Feedback feedback) {
        for (Client client : clientList) {
            client.send(feedback);
        }
    }

    protected class Server extends Worker {
        protected ServerSocket serverSocket;
        protected int port;
        
        public Server(int port) {
            this.port = port;
        }

        protected void activate() throws ActivateException {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                throw new ActivateException();
            }
            super.activate();
        }

        public synchronized boolean active() {
            return active = serverSocket != null && !serverSocket.isClosed();
        }
    
        protected synchronized void deactivate() throws DeactivateException {
            super.deactivate();
            try {
                route(new Feedback("[NetworkDevice] Closing server socket"));
                serverSocket.close();
            } catch (IOException e) {
                logger.error("", e);
            } finally {
                for (Client client : clientList) {
                    client.stop();
                }
            }
        }
    
        public void work() {
            try {
                route(new Feedback("[NetworkDevice] Wating for clients"));
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                client.start();
                route(new Feedback("[NetworkDevice] Client connected: " + socket.getInetAddress()));
            } catch (IOException e) {
                logger.error("", e);
            }
        }

        public synchronized void exit() {
            super.exit();
            for (Client client : clientList) {
                client.exit();
            }
        }
    }

    protected class Client extends Worker {
        protected Socket socket;
        protected InputStream inputStream;
        protected OutputStream outputStream;
        protected ObjectOutputStream objectOutputStream;

        public Client(Socket socket) throws IOException {
            this.socket = socket;
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            clientList.add(this);
        }

        public boolean active() {
            return active = socket.isConnected();
        }

        public void work() {
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(inputStream);
                Object object;
                do {
                    object = objectInputStream.readObject();
                    if (object instanceof Input) {
                        logger.trace("", object);
                        route((Input) object);
                    }
                } while (object != null);
            } catch (IOException e) {
                logger.error("", e);
                stop();
            } catch (ClassNotFoundException e) {
                logger.error("", e);
            }
        }

        protected void deactivate() throws DeactivateException {
            super.deactivate();
            send(new Task(Action.STOP, Target.SELF));
            clientList.remove(this);
            try {
                inputStream.close();
                outputStream.close();
                socket.close();    
            } catch (IOException e) {
                logger.error("", e);
            }
            route(new Feedback("[NetworkDevice] Client disconnected: " + socket.getInetAddress()));
        }

        public void send(Object object) {
            try {
                objectOutputStream.writeObject(object);
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }
}
