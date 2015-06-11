package base.server.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import base.receiver.Receiver;

public class TcpServerClient implements Receiver {
	protected static final int BUFFER_SIZE = 1024;
	
	protected TcpServer server;
	protected SocketChannel socketChannel;
	protected int bufferSize;
	protected ByteBuffer byteBuffer;

	public TcpServerClient(TcpServer server, SocketChannel socketChannel) {
		this(server, socketChannel, BUFFER_SIZE);
	}

	public TcpServerClient(TcpServer server, SocketChannel socketChannel, Integer bufferSize) {
		this.server = server;
		this.socketChannel = socketChannel;
		this.bufferSize = bufferSize;
		byteBuffer = ByteBuffer.allocate(bufferSize);
	}

	public void write(ByteBuffer byteBuffer) throws IOException {
		socketChannel.write(byteBuffer);		
	}

	public void readable() throws IOException {
		int read;		
		while (( read = socketChannel.read(byteBuffer)) > 0) {
			//byteBuffer.flip();
			byte[] buffer = byteBuffer.array();
			receive(buffer);
			System.out.println("readable() " + new String(buffer).trim());
			byteBuffer.clear();
			byteBuffer.put(new byte[bufferSize]);
			byteBuffer.clear();
		}
		if (read < 0) {
			socketChannel.close();
		}
	}

	public void receive(byte[] buffer) {
		// Should be forwarded somewhere?
		String output = new String(buffer).trim();			
		System.err.println("Message read from client: " + output);
		if (output.equals("Bye.")) {
			try {
				socketChannel.close();
			} catch (IOException e) {}
			System.out.println("Client messages are complete; close.");
		}
		
	}

	public void send(byte[] buffer) throws IOException {
		write(ByteBuffer.wrap(buffer));		
	}
}
