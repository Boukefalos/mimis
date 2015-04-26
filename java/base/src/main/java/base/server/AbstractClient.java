package base.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import base.worker.ThreadWorker;

public abstract class AbstractClient extends ThreadWorker {
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    
    public AbstractClient(Socket socket) {
    	this.socket = socket;
    }
}
