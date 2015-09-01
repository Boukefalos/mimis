package base.util;

import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayCycle<E> extends CopyOnWriteArrayList<E> {
    protected static final long serialVersionUID = 1L;

    protected int index = 0;

    @SuppressWarnings("unchecked")
    public ArrayCycle(E... elementArray) {
        if (elementArray != null) {
            for (E element : elementArray) {
                add(element);
            }
        }
    }

    public E current() {
        return this.get(index);
    }

    public synchronized E previous() {
        if (--index < 0) {
            index = Math.max(0, size() - 1);
        }
        return get(index);
    }

    public synchronized E next() {
        if (++index >= size()) {
            index = 0;
        }
        return size() == 0 ? null : get(index);
    }

    public E reset() {
        return get(index = 0);
    }
}
