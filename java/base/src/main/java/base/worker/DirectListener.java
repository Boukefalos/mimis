package base.worker;

import base.work.Listen;
import base.worker.pool.Listener;

public class DirectListener<E> extends ThreadListener<E> implements Listener<E> {
	public DirectListener(Listen<E> listen) {
		super(listen, false);
	}

	@Override
	public void add(Object element) {
		// TODO Auto-generated method stub
		
	}
}