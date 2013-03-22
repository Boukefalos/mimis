package sound;

import java.io.InputStream;

public interface Producer extends Format {
	public InputStream getInputStream();
	public void start();
	public void stop();
	public void exit();
}