package base.worker;

public abstract class AbstractWorker {
	public abstract void start();

	public abstract void stop();

	protected abstract void work();
}
