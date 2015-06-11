package base.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import base.sender.Sender;
import base.work.Work;

// Should be Listen, process writes in own thread
public abstract class AbstractClient extends Work implements Sender {
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    public AbstractClient(Socket socket) {
    	this.socket = socket;
    }

	public void send(byte[] buffer) throws IOException {
		outputStream.write(buffer);		
	}
}
