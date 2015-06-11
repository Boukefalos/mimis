package base.server.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.receiver.Receiver;

public abstract class TcpClient extends AbstractClient {
    protected static final int BUFFER = 2048;
	protected String host;
    protected int port;
	protected int bufferSize;
    protected ArrayList<Receiver> receiverList = new ArrayList<Receiver>();

    public TcpClient(String host, int port) {
    	this(host, port, BUFFER);
    }

    public TcpClient(String host, int port, int bufferSize) {
    	super(null);
        this.host = host;
        this.port = port;
        this.bufferSize = bufferSize;
    }

    public void activate() throws ActivateException {
        try {
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            //send("Incoming client!".getBytes());
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
    	byte[] buffer = new byte[bufferSize];
    	try {
			while (inputStream.read(buffer) > 0) {
				for (Receiver receiver : receiverList) {
					receiver.receive(buffer);
				}
			}
		} catch (IOException e) {
			stop();
		}
    }

    public void send(byte[] buffer) throws IOException {
    	System.out.println("Client writing: " + Charset.defaultCharset().decode(ByteBuffer.wrap(buffer)).toString());
    	outputStream.write(buffer);
    }
    
	public void register(Receiver receiver) {
		receiverList.add(receiver);
	}

	public void remove(Receiver receiver) {
		receiverList.remove(receiver);
	}
}