package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Listen;
import base.work.Work;

public class UdpMulticastClient extends Work {
    protected static final int BUFFER_SIZE = 2048;

	protected String host;
	protected int port;
	protected int bufferSize;
	protected MulticastSocket socket;
	protected InetAddress group;
	protected ArrayList<Listen<byte[]>> listenList;

	public UdpMulticastClient(String host, int port) {
		this(host, port, BUFFER_SIZE);
	}

	public UdpMulticastClient(String host, int port, int bufferSize) {
		this.host = host;
		this.port = port;
		this.bufferSize = BUFFER_SIZE;
		listenList = new ArrayList<Listen<byte[]>>();		
	}

	public void work() {	    
	    try {
	    	byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
		    buffer = packet.getData();
		    for (Listen<byte[]> listen : listenList) {
		    	listen.add(buffer);
		    }
		} catch (IOException e) {}
	}

	public void activate() throws ActivateException {
		try {
			socket = new MulticastSocket(port);
			group = InetAddress.getByName(host);
			socket.joinGroup(group);
		} catch (IOException e) {
			throw new ActivateException();
		}
	}

	public void deactivate() throws DeactivateException {
		try {
			socket.leaveGroup(group);
		} catch (IOException e) {
			throw new DeactivateException();
		}
		socket.close();
	}

	public void register(Listen<byte[]> listen) {
		listenList.add(listen);		
	}

	public void remove(Listen<byte[]> listen) {
		listenList.remove(listen);
	}
}
