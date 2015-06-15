package test.server.dummy;

import java.io.IOException;

import base.sender.Sender;
import base.work.Work;

public class DummyWriter extends Work implements Sender {

	private Sender sender;

	public DummyWriter(Sender sender) {
		this.sender = sender;
	}

	public void work() {
		System.out.println("Client sending messages to server...");
		String [] messages = new String[] {"Time goes fast.", "What now?", "Bye."};
		try {
			for (int i = 0; i < messages.length; i++) {	
				System.out.println(messages[i]);
				send(new String(messages[i]).getBytes());
				sleep(200);
			}
			stop();			
		} catch (Exception e) {}
	}

	public void send(byte[] buffer) throws IOException {
		sender.send(buffer);
	}
}
