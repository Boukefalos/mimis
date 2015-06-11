package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UdpMulticastServer implements Runnable {
	private MulticastSocket socket;

	public void run() {
		try {
			socket = new MulticastSocket(4445);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
	        try {
	            byte[] buf = new byte[256];
                String dString = String.valueOf(Math.random());
	            buf = dString.getBytes();

	            InetAddress group = InetAddress.getByName("239.255.255.255");
	            DatagramPacket packet;
	            packet = new DatagramPacket(buf, buf.length, group, 4446);
	            socket.send(packet);

	            try {
	                Thread.sleep(1000);
	            }
	            catch (InterruptedException e) { }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    //socket.close();
	}
}
