package base.worker.pool;


class Wrapper<E> {
    protected PooledListener<E> listener;
    protected E element;

    public Wrapper(PooledListener<E> listener, E element) {
        this.listener = listener;
        this.element = element;
    }

    public void deliver() {
        listener.input(element);            
    }
}