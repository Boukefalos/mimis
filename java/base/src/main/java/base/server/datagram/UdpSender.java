package base.server.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Sender;

public class UdpSender implements Sender {
    protected static final String HOST = "localhost";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected DatagramSocket datagramSocket;
    protected InetAddress inetAddress;
    protected int port;

    public UdpSender(int port) throws UnknownHostException {
        this(HOST, port);
    }

    public UdpSender(String host, int port) throws UnknownHostException {
        System.out.println("Sender use: " + host + " " + port);
        inetAddress = InetAddress.getByName(host);
        this.port = port;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            logger.error("Failed to create socket", e);
        }        
    }

    public void start() {}

    public void stop() {
        datagramSocket.close();
    }

    public void exit() {
        stop();
    }

    public void send(byte[] buffer) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            logger.error("Failed to send buffer", e);
        }        
    }
}
