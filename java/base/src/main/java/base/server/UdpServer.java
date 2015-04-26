package base.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import base.exception.worker.ActivateException;
import base.worker.Worker;

public abstract class UdpServer extends Worker {
	protected static final int BUFFER_SIZE = 1024;
	protected int port;
	protected int bufferSize;
	protected DatagramSocket diagramSocket;

	public UdpServer(int port) {
		this(port, BUFFER_SIZE);
	}

	public UdpServer(int port, int bufferSize) {
		this.port = port;
		this.bufferSize = bufferSize;
	}

	protected void activate() throws ActivateException {
		try {
			logger.debug("Starting datagram socket on port " + port);
			diagramSocket = new DatagramSocket(port);
			super.activate();
		} catch (SocketException e) {
			logger.error("Failed to initialize socket", e);
			throw new ActivateException();
		}
	}

	public void work() {
		byte[] buffer = new byte[bufferSize];
		DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
		try {
			diagramSocket.receive(datagramPacket);
		} catch (IOException e) {
			logger.error("Failed to receive packet");
			stop();
			return;
		}
		receive(buffer);		
	}

	public synchronized void stop() {
		diagramSocket.close();
	}

	abstract protected void receive(byte[] buffer);
}
