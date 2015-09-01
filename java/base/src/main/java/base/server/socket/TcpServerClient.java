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
