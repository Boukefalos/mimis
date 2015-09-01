package base.server.datagram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.exception.worker.ActivateException;

public class UdpMulticastClient extends AbstractUdpClient {
    protected static final String HOST = "239.255.255.255";

    protected String host;
    protected int port;

    public UdpMulticastClient(int port) {
        this(HOST, port);
    }

    public UdpMulticastClient(String host, int port) {
        this(host, port, BUFFER_SIZE);
    }

    public UdpMulticastClient(String host, int port, int bufferSize) {
        this.host = host;
        this.port = port;
        this.bufferSize = BUFFER_SIZE;
        System.out.println("Client bind: " + host + " " + port);
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(host);
            socket.joinGroup(group);
        } catch (IOException e) {
            logger.error("", e);
            throw new ActivateException();
        }
    }

    protected void input(byte[] buffer) {}
}
