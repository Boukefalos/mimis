package base.server.datagram;

import java.net.UnknownHostException;

public class UdpDuplexAutoClient extends UdpDuplexClient {
    public UdpDuplexAutoClient(int bindPort, int sendPort) throws UnknownHostException {
        super(HOST, bindPort, null, sendPort);
    }

    public UdpDuplexAutoClient(String bindHost, int bindPort, int sendPort) throws UnknownHostException {
        super(bindHost, bindPort, null, sendPort);
    }

    public UdpDuplexAutoClient(String bindHost, int bindPort, int sendPort, int bufferSize) throws UnknownHostException {
        super(bindHost, bindPort, null, sendPort, bufferSize);
    }
}
