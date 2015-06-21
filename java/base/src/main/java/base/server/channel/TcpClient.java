package base.server.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.sender.Sender;
import base.work.Listen;
import base.work.Work;
import base.worker.Worker;

public abstract class TcpClient extends Work implements Sender {
	protected static final String HOST = "localhost";
	protected static final int BUFFER_SIZE = 1024;

	protected String host;
	protected int port;
	protected int bufferSize;
	protected SocketChannel socketChannel;
	protected Selector selector;
    protected ArrayList<Listen<byte[]>> listenList = new ArrayList<Listen<byte[]>>();

    public TcpClient(int port) {
    	this(HOST, port);
    }

	public TcpClient(String host, int port) {
		this(host, port, BUFFER_SIZE);
	}

	public TcpClient(String host, int port, int bufferSize) {
		this.host = host;
		this.port = port;
		this.bufferSize = bufferSize;
	}

	public void activate() throws ActivateException {
		System.out.println("Client: Activate!");
		try {
			InetSocketAddress hostAddress = new InetSocketAddress(host, port);
			socketChannel = SocketChannel.open(hostAddress);
			socketChannel.configureBlocking(false);
			while (!socketChannel.finishConnect()) {
				sleep(Worker.SLEEP);
			}
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
			synchronized (host) {
				host.notifyAll();
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new ActivateException();
		}
		super.activate();
	}

	public void deactivate() throws DeactivateException {
		System.out.println("Client: Deactivate!");
		try {
			selector.close();
			socketChannel.close();
		} catch (IOException e) {
			throw new DeactivateException();
		}
	}

	public void stop() {
		super.stop();
		if (selector != null) {
			selector.wakeup();
		}
	}

	public final void work() {
		try {
			logger.debug("Client: Waiting for select... ");	
			logger.debug("Client: Number of selected keys: " + selector.select());
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();

			while (selectionKeyIterator.hasNext()) {	
				SelectionKey selectionKey = selectionKeyIterator.next();
				if (selectionKey.isReadable()) {
					ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
					socketChannel.read(byteBuffer);
					byte[] buffer = byteBuffer.array();
					input(buffer);
				} else if (selectionKey.isWritable()) {
					byte[] buffer;
					buffer = (byte[]) selectionKey.attachment();
					ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
					socketChannel.write(byteBuffer);
					//selectionKey.cancel();
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
				selectionKeyIterator.remove();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	protected abstract void input(byte[] buffer);

	public void send(byte[] buffer) throws IOException {
		if (selector == null) {
			try {
				synchronized (host) {
					host.wait();
				}
			} catch (InterruptedException e) {}
		}
		selector.wakeup();
		socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, buffer);
	}

	public void close() throws IOException {
		socketChannel.close();		
	}

	/*public void register(Listen<byte[]> listen) {
		listenList.add(listen);
	}

	public void remove(Listen<byte[]> listen) {
		listenList.remove(listen);
	}*/
}