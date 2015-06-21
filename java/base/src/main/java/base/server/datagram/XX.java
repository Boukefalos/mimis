package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import base.work.Work;

public class XX extends Work {

	protected int bufferSize = 1024;
	private MulticastSocket socket;
	private DatagramPacket dgram;

	public XX(MulticastSocket socket) {
		this.socket = socket;
		byte[] b = new byte[1024];
		dgram = new DatagramPacket(b, b.length);
	}


	public void work() {
		  try {
			socket.receive(dgram);
		} catch (IOException e) {
			stop();
		} // blocks until a datagram is received
		  System.err.println("Received " + dgram.getLength() +
		    " bytes from " + dgram.getAddress());
		  dgram.setLength(bufferSize); // must reset length field!
		}
	
	public void stop() {
		super.stop();
		socket.close();
	}
}
