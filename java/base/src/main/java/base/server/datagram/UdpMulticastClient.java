package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.exception.worker.ActivateException;
import base.work.Work;

public abstract class UdpMulticastClient extends Work {
	protected static final String HOST = "239.255.255.255";
    protected static final int BUFFER_SIZE = 2048;

	protected String host;
	protected int port;
	protected int bufferSize;
	protected MulticastSocket socket;
	protected InetAddress group;

	public UdpMulticastClient(int port) {
		this(HOST, port);
	}

	public UdpMulticastClient(String host, int port) {
		this(host, port, BUFFER_SIZE);
	}

	public UdpMulticastClient(String host, int port, int bufferSize) {
		this.host = host;
		this.port = port;
		this.bufferSize = BUFFER_SIZE;	
	}

	public void work() {	    
	    try {
	    	byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
		    buffer = packet.getData();
		    input(buffer);
		} catch (IOException e) {}
	}

	public void activate() throws ActivateException {
		try {
			socket = new MulticastSocket(port);
			group = InetAddress.getByName(host);
			socket.joinGroup(group);
		} catch (IOException e) {
			logger.error("", e);
			throw new ActivateException();
		}
	}

	public void stop() {
		socket.close();
		super.stop();
	}

	protected abstract void input(byte[] buffer);	
}
