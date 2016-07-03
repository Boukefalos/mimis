package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import base.Sender;

public class UdpDuplexClient extends UdpMulticastClient implements Sender {
    protected int sendPort;
    protected Sender sender;

    public UdpDuplexClient(int bindPort, String sendHost, int sendPort) throws UnknownHostException {
        this(HOST, bindPort, sendHost, sendPort);
    }

    public UdpDuplexClient(String bindHost, int bindPort, String sendHost, int sendPort) throws UnknownHostException {
        this(bindHost, bindPort, sendHost, sendPort, BUFFER_SIZE);
    }

    public UdpDuplexClient(String bindHost, int bindPort, String sendHost, int sendPort, int bufferSize) throws UnknownHostException {
        super(bindHost, bindPort, bufferSize);
        this.sendPort = sendPort;
        if (sendHost != null) {
            sender = new UdpSender(sendHost, sendPort);
        }
    }

    public void work() {        
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            buffer = packet.getData();
            System.out.println("Receive from " + packet.getAddress().getHostAddress());
            if (sender == null) {
                String sendHost = packet.getAddress().getHostAddress();
                sender = new UdpSender(sendHost, sendPort);
            }
            input(buffer);
        } catch (IOException e) {}
    }

    public void send(byte[] buffer) throws IOException {
        if (sender != null) {
            sender.send(buffer);
        }
    }

    public void input(byte[] buffer) {}
}
