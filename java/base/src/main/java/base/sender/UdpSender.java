package base.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpSender implements Sender {
    protected Logger logger = LoggerFactory.getLogger(getClass());

	protected DatagramSocket datagramSocket;
	protected InetAddress inetAddress;
	protected int port;

	public UdpSender(String host, int port) throws UnknownHostException{
		inetAddress = InetAddress.getByName(host);
		logger.debug(host);
		logger.debug(String.valueOf(port));
		this.port = port;
	}

	protected boolean setup() {
		if (datagramSocket == null) {
			try {
				datagramSocket = new DatagramSocket();
			} catch (SocketException e) {
				logger.error("Failed to create socket", e);
				return false;
			}
			Runtime.getRuntime().addShutdownHook(new Thread() {	
				public void run() {
					datagramSocket.close();
				}
			});
		}
		return true;
	}

	public void send(byte[] buffer) {
		try {
			setup();
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			logger.error("Failed to send buffer", e);
		}		
	}
}
