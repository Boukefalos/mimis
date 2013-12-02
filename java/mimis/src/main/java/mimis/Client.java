package mimis;

import base.exception.worker.ActivateException;
import mimis.router.GlobalRouter;
import mimis.util.swing.Dialog;

public class Client extends Main {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 6789;

    public Client(String ip, int port) {
        super();
        router = new GlobalRouter(ip, port);
    }

    public void activate() throws ActivateException {
        super.activate();
    }

    public static void main(String[] args) {
        String ip = Dialog.question("Server IP:", IP);
        int port = Integer.valueOf(Dialog.question("Server Port:", PORT));
        new Client(ip, port).start();
    }
}