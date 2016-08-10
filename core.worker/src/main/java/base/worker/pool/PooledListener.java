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

import base.work.Listen;

public class PooledListener<E> extends PooledWorker implements Listener<E> {
    protected BlockingQueue<Wrapper<E>> poolQueue;
    protected Listen<E> listen;

    public PooledListener(Listen<E> listen) {
        super(listen);
        this.listen = listen;
    }

    public void setPoolQueue(BlockingQueue<Wrapper<E>> poolQueue) {
        this.poolQueue = poolQueue;
    }

    public synchronized void add(E element) {
        Wrapper<E> wrapper = new Wrapper<E>(this, element);
        poolQueue.add(wrapper);
    }

    void input(E element) {
        listen.input(element);
    }
}