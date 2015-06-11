package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.receiver.Receiver;
import base.work.Work;

public abstract class UdpServer extends Work {
	protected static final int BUFFER_SIZE = 1024;
	protected static final int TIMEOUT = 1000;
	protected int port;
	protected int bufferSize;
	protected DatagramSocket diagramSocket;
    protected ArrayList<Receiver> receiverList = new ArrayList<Receiver>();

	public UdpServer(int port) {
		this(port, BUFFER_SIZE);
	}

	public UdpServer(int port, int bufferSize) {
		super();
		this.port = port;
		this.bufferSize = bufferSize;
	}

	public void activate() throws ActivateException {
		try {
			logger.debug("Starting datagram socket on port " + port);
			diagramSocket = new DatagramSocket(port);
			diagramSocket.setSoTimeout(TIMEOUT);
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
		} catch (SocketTimeoutException e) {
			return;
		} catch (IOException e) {
			logger.error("Failed to receive packet", e);
			stop();
			return;
		}
		for (Receiver receiver : receiverList) {
			receiver.receive(buffer);
		}	
	}
	
	public void addReceiver(Receiver receiver) {
		receiverList.add(receiver);
	}

	public void removeReceiver(Receiver receiver) {
		receiverList.remove(receiver);
	}
}