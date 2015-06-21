package base.sender;

import java.io.IOException;

import base.Control;

public interface Sender extends Control {
	public void send(byte[] buffer) throws IOException;
}
