package base.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public abstract class TcpClient extends AbstractClient {
    protected static final int BUFFER = 2048;
	protected String host;
    protected int port;

    public TcpClient(String host, int port) {
    	super(null);
        this.host = host;
        this.port = port;
    }

    public void activate() throws ActivateException {
        try {
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            send("Incoming client!".getBytes());
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

    public final void work() {
    	byte[] buffer = new byte[BUFFER];
    	try {
			while (inputStream.read(buffer) > 0) {
				receive(buffer);
			}
		} catch (IOException e) {
			stop();
		}
    }

    public void send(byte[] buffer) throws IOException {
    	System.out.println("Client writing: " + Charset.defaultCharset().decode(ByteBuffer.wrap(buffer)).toString());
    	outputStream.write(buffer);
    }

    public abstract void receive(byte[] buffer);
}