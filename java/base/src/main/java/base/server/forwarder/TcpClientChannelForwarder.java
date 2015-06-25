package base.server.forwarder;

import java.util.ArrayList;

import base.Duplex;
import base.Receiver;
import base.server.socket.TcpClient;
import base.server.socket.TcpServerClient;

public class TcpClientChannelForwarder extends TcpClient implements Duplex {
	protected ArrayList<Receiver> receiverList;

	public TcpClientChannelForwarder(String host, int port) {
		 super(host, port);
		 receiverList = new ArrayList<Receiver>();
	 }

	public void register(Receiver receiver) {
		receiverList.add(receiver);
	}

	public void remove(Receiver receiver) {
		receiverList.remove(receiver);
	}

	public void input(TcpServerClient client, byte[] buffer) {
		for (Receiver receiver: receiverList) {
			receiver.receive(buffer);
		}
	}
}
