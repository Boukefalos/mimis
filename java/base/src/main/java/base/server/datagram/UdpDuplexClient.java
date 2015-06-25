package base.server.datagram;

import java.io.IOException;

import base.Sender;

public class UdpDuplexClient extends UdpMulticastClient implements Sender {

	public UdpDuplexClient(int port) {
		super(port);
	}

	public void send(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	protected void input(byte[] buffer) {
		// TODO Auto-generated method stub
		
	}
}
