package base.server.forwarder;

import java.util.ArrayList;

import base.receiver.Forwarder;
import base.receiver.Receiver;
import base.server.datagram.UdpServer;

public class UdpServerForwarder extends UdpServer implements Forwarder {
	protected ArrayList<Receiver> receiverList;

	public UdpServerForwarder(int port) {
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
