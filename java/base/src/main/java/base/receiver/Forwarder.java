package base.receiver;

import base.Control;

public interface Forwarder extends Control {
	public void register(Receiver receiver);
	public void remove(Receiver receiver);
}
