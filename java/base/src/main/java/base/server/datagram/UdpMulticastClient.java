package base.server.datagram;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UdpMulticastClient implements Runnable {
	public void run() {
		try {
			MulticastSocket socket = new MulticastSocket(4446);
			InetAddress group = InetAddress.getByName("239.255.255.255");
			socket.joinGroup(group);
	
			DatagramPacket packet;
			while (true) {
			    byte[] buf = new byte[256];
			    packet = new DatagramPacket(buf, buf.length);
			    socket.receive(packet);
	
			    String received = new String(packet.getData()).trim();
			    System.out.println("Quote of the Moment: " + received);
			}
	
			//socket.leaveGroup(group);
			//socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
