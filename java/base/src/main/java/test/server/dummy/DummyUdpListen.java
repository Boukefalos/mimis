package test.server.dummy;

import base.work.Listen;

public class DummyUdpListen extends Listen<byte[]> {
	public DummyUdpListen() {
		super();
	}

	public void input(byte[] buffer) {
	    String received = new String(buffer).trim();	
	    System.out.println("Quote of the Moment: " + received);
	}
}
