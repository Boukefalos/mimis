package base.sender;

import java.io.IOException;

public interface Sender {
	public void send(byte[] buffer) throws IOException;
}
