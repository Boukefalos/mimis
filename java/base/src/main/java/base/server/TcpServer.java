package base.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.work.Work;

public class TcpServer extends Work {
	protected int port;
	protected Socket socket;
	protected Constructor<?> clientConstructor;
	protected ArrayList<Client> clientList;
	protected ServerSocket serverSocket;

	public TcpServer(int port, Class<?> clientClass) {
		this.port = port;
		try {
			clientConstructor = Class.forName(clientClass.getName()).getConstructor(Socket.class);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			logger.error("Failed to initialise client constructor");
		}
		clientList = new ArrayList<Client>();
	}

	public void activate() throws ActivateException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("", e);
			throw new ActivateException();
		}
		super.activate();
	}

	public void work() {
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			logger.error("", e);
			return;
		}
		try {
			Client client = (Client) clientConstructor.newInstance(socket);
			clientList.add(client);
			client.start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static abstract class Client extends AbstractClient {
	
		public Client(Socket socket) {
			super(socket);
		}

		public void setSocket(Socket socket) {			
		}
	
		public void work() {
		}
	}

}
