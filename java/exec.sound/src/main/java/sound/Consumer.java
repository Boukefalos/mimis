package sound;

public interface Consumer {
    public void start(Producer producer);
    public void stop();
    public void exit();
}
