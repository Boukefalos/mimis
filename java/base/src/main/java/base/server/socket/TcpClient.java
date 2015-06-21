package base.server.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import base.exception.worker.ActivateException;
import base.sender.Sender;

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