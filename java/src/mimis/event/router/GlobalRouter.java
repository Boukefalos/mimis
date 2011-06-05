package mimis.event.router;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mimis.Event;
import mimis.Worker;
import mimis.event.EventRouter;
import mimis.event.Feedback;
import mimis.exception.event.router.GlobalRouterException;

public class GlobalRouter extends EventRouter {
    protected Socket socket;
    protected ObjectOutputStream objectOutputStream;
    protected ObjectInputStream objectInputStream;

    public GlobalRouter(String ip, int port) throws GlobalRouterException {
        try {
            socket = new Socket(ip, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            new Worker() {
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
                        log.error(e);
                    } catch (ClassNotFoundException e) {
                        log.error(e);
                    }
                }
            };
            return;
        } catch (UnknownHostException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        throw new GlobalRouterException();
    }

    public void event(Event event) {
        try {
            objectOutputStream.writeObject(event);
        } catch (IOException e) {
            log.error(e);
        }
    }
}
