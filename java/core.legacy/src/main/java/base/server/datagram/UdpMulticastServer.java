package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.Sender;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Listen;
import base.worker.Worker;

public class UdpMulticastServer extends Listen<byte[]> implements Sender {
    protected static final String HOST = "239.255.255.255";
    protected static final int BUFFER_SIZE = 2048;

    protected String host;
    protected int port;
    protected MulticastSocket socket;

    public UdpMulticastServer(int port) {
        this(HOST, port);
    }

    public UdpMulticastServer(String host, int port) {
        super(Worker.Type.BACKGROUND);
        this.host = host;
        this.port = port;
        System.out.println("Server send: " + host + " " + port);
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            throw new ActivateException();
        }
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        socket.close();
        super.deactivate();
    }

    public void input(byte[] buffer) {
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