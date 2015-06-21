package base.server.socket;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.sender.Sender;
import base.work.Work;

public class TcpServer extends Work implements Sender {
	protected static final Class<?> CLIENT_CLASS = TcpServerClient.class;

	protected int port;
	protected ServerSocket serverSocket;
	protected Constructor<?> clientConstructor;
	protected ArrayList<TcpServerClient> clientList;

    public TcpServer(int port) {
    	this(port, CLIENT_CLASS);
    }

	public TcpServer(int port, Class<?> clientClass) {
		this.port = port;
		try {
			clientConstructor = Class.forName(clientClass.getName()).getConstructor(TcpServer.class, Socket.class);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			logger.error("Failed to initialise client constructor");
		}
		clientList = new ArrayList<TcpServerClient>();
	}

	public void activate() throws ActivateException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("", e);
			throw new ActivateException();
		}
	}

	public void deactivate() throws DeactivateException {
		for (TcpServerClient client : clientList) {
			client.stop();
		}
	}

	public void exit() {
		super.exit();
		try {
			serverSocket.close();
			// Should check if clients exit as well
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void work() {
		try {
			Socket socket = serverSocket.accept();
			TcpServerClient client = (TcpServerClient) clientConstructor.newInstance(this, socket);
			clientList.add(client);
			client.start();
			System.out.println("Accepted new connection from client: " + socket);
		} catch (IOException e) {
			stop();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void send(byte[] buffer) throws IOException {
		logger.debug("Number of clients = " + clientList.size());
		for (TcpServerClient client : clientList) {
			// Should be dealt with in clients own thread
			client.send(buffer);
		}		
	}

	public void input(TcpServerClient client, byte[] buffer) {}
}
