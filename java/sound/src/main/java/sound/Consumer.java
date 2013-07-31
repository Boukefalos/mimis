package sound;

public interface Consumer {
	public void start(Producer producer);
	public void start();
	public void stop();
	public void exit();
}
