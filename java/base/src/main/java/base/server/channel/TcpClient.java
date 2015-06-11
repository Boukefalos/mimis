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
import base.receiver.Receiver;
import base.sender.Sender;
import base.work.Work;
import base.worker.Worker;

public class TcpClient extends Work implements Sender {
	protected static final int BUFFER_SIZE = 1024;

	protected String host;
	protected int port;
	protected int bufferSize;
	protected SocketChannel socketChannel;
	protected Selector selector;
    protected ArrayList<Receiver> receiverList = new ArrayList<Receiver>();

	public TcpClient(String host, int port) {
		this(host, port, BUFFER_SIZE);
	}

	public TcpClient(String host, int port, int bufferSize) {
		this.host = host;
		this.port = port;
		this.bufferSize = bufferSize;
	}

	public void activate() throws ActivateException {
		try {
			InetSocketAddress hostAddress = new InetSocketAddress(host, port);
			socketChannel = SocketChannel.open(hostAddress);
			socketChannel.configureBlocking(false);
			while (!socketChannel.finishConnect()) {
				sleep(Worker.SLEEP);
			}
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			logger.error("", e);
			throw new ActivateException();
		}
		super.activate();
	}

	public final void work() {
		try {
			//System.out.println("Client: Waiting for select...");	
			//System.out.println("Client: Number of selected keys: " + selector.select());
			selector.select();
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();	
			while (selectionKeyIterator.hasNext()) {	
				SelectionKey selectionKey = selectionKeyIterator.next();	
				if (selectionKey.isReadable()) {
					ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
					socketChannel.read(byteBuffer);
					byte[] buffer = byteBuffer.array();
					for (Receiver receiver : receiverList) {
						receiver.receive(buffer);
					}
				} else if (selectionKey.isWritable()) {
					byte[] buffer;
					buffer = (byte[]) selectionKey.attachment();
					System.out.println("poll() " + new String(buffer).trim());
					ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
					socketChannel.write(byteBuffer);
					selectionKey.cancel();
				}
				selectionKeyIterator.remove();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void send(byte[] buffer) throws IOException {
		selector.wakeup();
		socketChannel.register(selector, SelectionKey.OP_WRITE, buffer);
	}

	public void close() throws IOException {
		socketChannel.close();		
	}

	public void register(Receiver receiver) {
		receiverList.add(receiver);
	}

	public void remove(Receiver receiver) {
		receiverList.remove(receiver);
	}
}