package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class XX extends Thread {

	private MulticastSocket socket;

	public XX(MulticastSocket socket) {
		this.socket = socket;
	}

	public void run() {
		while (true) {
			byte[] b = new byte[1024];
			DatagramPacket dgram = new DatagramPacket(b, b.length);


			while(true) {
			  try {
				socket.receive(dgram);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // blocks until a datagram is received
			  System.err.println("Received " + dgram.getLength() +
			    " bytes from " + dgram.getAddress());
			  dgram.setLength(b.length); // must reset length field!
			}
		}
	}
}
