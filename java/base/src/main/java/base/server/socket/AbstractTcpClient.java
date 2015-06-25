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
