package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public class UdpDuplexServer extends UdpMulticastServer {
    protected int bindPort;
    protected UdpDuplexHelper helper;

    public UdpDuplexServer(int sendPort, int bindPort) {
        super(sendPort);
        this.bindPort = bindPort;
    }

    public void activate() throws ActivateException {
        try {
            socket = new MulticastSocket(bindPort);
            synchronized (this) {
                notifyAll();
            }
            helper = new UdpDuplexHelper(this, socket);
            helper.start();
        } catch (IOException e) {
            throw new ActivateException();
        }
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        helper.stop();
        super.deactivate();
    }

    public void send(byte[] buffer) throws IOException {
        if (socket == null) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        try {
            InetAddress group = InetAddress.getByName(host);
            System.out.println("Send to " + host + " " + port);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        }
        catch (IOException e) {
            logger.error("", e);
        }
    }
}
