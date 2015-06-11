package base.server.channel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import base.exception.worker.ActivateException;
import base.sender.Sender;
import base.work.Work;

public class TcpServer extends Work implements Sender {
	protected static final int BUFFER_SIZE = 1024;

	protected int port;
	protected int bufferSize;
	protected Constructor<?> clientConstructor;
	protected Selector selector;
	protected ServerSocketChannel serverSocket;
	protected ArrayList<TcpServerClient> clientList;

	public TcpServer(int port) {
		this(port, TcpServerClient.class);
	}

	public TcpServer(int port, Class<?> clientClass) {
		this(port, clientClass, BUFFER_SIZE);
	}

	public TcpServer(int port, Class<?> clientClass, int bufferSize) {
		this.port = port;
		this.bufferSize = bufferSize;
		try {
			logger.error(clientClass.getName());
			clientConstructor = Class.forName(clientClass.getName()).getConstructor(getClass(), SocketChannel.class, Integer.class);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			logger.error("Failed to initialise client constructor");
			e.printStackTrace();
		}
		clientList = new ArrayList<TcpServerClient>();
	}

	public void activate() throws ActivateException {
		try {
			// Get selector
			selector = Selector.open();

			// Get server socket channel and register with selector
			serverSocket = ServerSocketChannel.open();
			InetSocketAddress hostAddress = new InetSocketAddress(port);
			serverSocket.bind(hostAddress);
			serverSocket.configureBlocking(false);
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception e) {
			throw new ActivateException();
		}
	}

	public void work() {
		try {
			System.out.println("Server: Number of selected keys: " + selector.select());
	
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
	
			while (selectionKeyIterator.hasNext()) {	
				SelectionKey selectionKey = selectionKeyIterator.next();	
				if (selectionKey.isAcceptable()) {	
					// Accept the new client connection
					SocketChannel socketChannel = serverSocket.accept();
					socketChannel.configureBlocking(false);
	
					// Add the new connection to the selector
					TcpServerClient serverClient = (TcpServerClient) clientConstructor.newInstance(this, socketChannel, bufferSize);
					clientList.add(serverClient);
					socketChannel.register(selector, SelectionKey.OP_READ, serverClient);
					//initClient(serverClient);
					System.out.println("Accepted new connection from client: " + socketChannel);
				} else if (selectionKey.isReadable()) {	
					// Read the data from client
					TcpServerClient serverClient = (TcpServerClient) selectionKey.attachment();
					serverClient.readable();	
				} else if (selectionKey.isWritable()) {
					// Write to client?
				}
				selectionKeyIterator.remove();
			}
		} catch (IOException e) {} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (IllegalArgumentException e) {
			logger.error("", e);
		} catch (InvocationTargetException e) {
			logger.error("", e);
		}
	}

	protected void initClient(TcpServerClient serverClient) {
		/*try {
			serverClient.write(ByteBuffer.wrap(new String("Hi there!").getBytes()));
		} catch (IOException e) {
			logger.error("", e);
		}*/	
	}

	public void send(byte[] buffer) throws IOException {
		for (TcpServerClient client : clientList) {
			// Should be dealt with in clients own thread
			client.send(buffer);
		}		
	}
}