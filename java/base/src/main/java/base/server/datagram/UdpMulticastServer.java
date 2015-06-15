package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.sender.Sender;
import base.work.Listen;

public class UdpMulticastServer extends Listen<byte[]> implements Sender {
    protected static final int BUFFER_SIZE = 2048;

	protected String host;
	protected int port;
	protected MulticastSocket socket;

	public UdpMulticastServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void activate() throws ActivateException {
		try {
			socket = new MulticastSocket(); // optional, add port and receive as well!!
			// pass socket directly to Server to establish bidirectional
			// couple together capabilities
			// listen to datagrams and deal with writing using nio?
			new XX(socket).start();
		} catch (IOException e) {
			throw new ActivateException();
		}
		super.activate();
	}

	public void deactivate() throws DeactivateException {
		super.deactivate();
		socket.close();
	}

	public boolean active() {
		return socket != null;
		/* Should handle connection state
		if (socket == null) {
			return false;
		} else {
			return socket.isConnected() && !socket.isClosed();
		}*/
	}

	public void input(byte[] buffer) {
		if (socket == null) {
			return;
		}
        try {
            InetAddress group = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        }
        catch (IOException e) {
            logger.error("", e);
        }
	}

	public void send(byte[] buffer) throws IOException {
		add(buffer);		
	}
}
