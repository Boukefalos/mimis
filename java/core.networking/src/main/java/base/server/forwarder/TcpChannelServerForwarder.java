package base.server.forwarder;

import java.util.ArrayList;

import base.Duplex;
import base.Receiver;
import base.server.channel.TcpServer;
import base.server.channel.TcpServerClient;

public class TcpChannelServerForwarder extends TcpServer implements Duplex {
    protected ArrayList<Receiver> receiverList;

    public TcpChannelServerForwarder(int port) {
         super(port);
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
