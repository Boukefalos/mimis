package base.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public abstract class TcpClient extends AbstractClient {
    protected String host;
    protected int port;

    public TcpClient(String ip, int port) {
    	super(null);
        this.host = ip;
        this.port = port;
    }

    public void activate() throws ActivateException {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(SLEEP);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (UnknownHostException e) {
            logger.error("", e);
            throw new ActivateException();
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
        super.activate();
    }

    public synchronized boolean active() {
        if (active && !socket.isConnected()) {
            active = false;
        }
        return active;
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
}