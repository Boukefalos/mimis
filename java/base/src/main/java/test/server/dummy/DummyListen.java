package test.server.dummy;

import base.work.Listen;

public class DummyListen extends Listen<byte[]> {
	public void receive(byte[] buffer) {
		String output = new String(buffer).trim();
		System.out.println("Client: message read: " + output);
	}
}
