package base.server.forwarder;

import java.util.ArrayList;

import base.Duplex;
import base.Receiver;
import base.server.datagram.UdpDuplexClient;

public class UdpDuplexClientForwarder extends UdpDuplexClient implements Duplex {
	protected ArrayList<Receiver> receiverList;

	public UdpDuplexClientForwarder(String host, int port) {
		 super(port);
		 receiverList = new ArrayList<Receiver>();
	 }

	public void register(Receiver receiver) {
		receiverList.add(receiver);
	}

	public void remove(Receiver receiver) {
		receiverList.remove(receiver);
	}

	public void input(byte[] buffer) {
		for (Receiver receiver: receiverList) {
			receiver.receive(buffer);
		}
	}
}
