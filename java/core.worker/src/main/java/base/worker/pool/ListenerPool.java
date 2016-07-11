/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package base.worker.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ListenerPool<E> {
    protected int poolSize;
    protected BlockingQueue<Wrapper<E>> queue;
    protected ExecutorService executorService;

    public ListenerPool(int poolSize) {
        this.poolSize = poolSize;
        queue = new LinkedBlockingQueue<Wrapper<E>>();
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    public PooledListener<E> add(PooledListener<E> listener) {
        listener.setPoolQueue(queue);
        return listener;
    }

    public void start() {
        for (int i = 0; i < poolSize; ++i) {
            Runnable runnable = new ListenerPoolRunnable<E>(queue, i);
            executorService.execute(runnable);
        }
    }

    public void await() {
        try {
            executorService.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {}        
    }
    
}
