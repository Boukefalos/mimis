package base.server.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Control;
import base.Forwarder;
import base.Receiver;

public abstract class AbstractReceiver implements Receiver, Control {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Forwarder forwarder;

    public AbstractReceiver(Forwarder forwarder) {
        this.forwarder = forwarder;
        forwarder.register(this);
    }

    public void start() {
        forwarder.start();        
    }

    public void stop() {
        forwarder.stop();        
    }

    public void exit() {
        forwarder.exit();        
    }

    abstract public void receive(byte[] buffer);
}
