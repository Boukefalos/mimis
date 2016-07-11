package base.server.datagram;

import java.net.MulticastSocket;

import base.work.Listen;

public class UdpDuplexHelper extends AbstractUdpClient {
    protected Listen<byte[]> listen;

    public UdpDuplexHelper(Listen<byte[]> listen, MulticastSocket socket) {
        super(socket);
        this.listen = listen;
    }

    public void input(byte[] buffer) {
        System.out.println("jajajaja");
        listen.add(buffer);
    }
}
