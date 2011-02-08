package pm.util;

import java.util.ArrayList;

public class ArrayCycle<E> extends ArrayList<E> {
    protected static final long serialVersionUID = 1L;

    protected int index = 0;

    public E current() {
        return this.get(index);
    }

    public E previous() {
        if (--index < 0) {
            index = Math.max(0, size() - 1);
        }
        return get(index);
    }

    public E next() {
        if (++index >= size()) {
            index = 0;
        }
        return get(index);
    }

    public E reset() {
        return get(index = 0);
    }
}