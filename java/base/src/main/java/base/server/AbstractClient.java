package base.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import base.worker.Worker;

public abstract class AbstractClient extends Worker {
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    
    public AbstractClient(Socket socket) {
    	this.socket = socket;
    }
}
