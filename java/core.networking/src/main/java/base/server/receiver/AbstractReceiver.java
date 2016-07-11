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
package base.server.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Control;
import base.Forwarder;
import base.Receiver;

public abstract class AbstractReceiver implements Receiver, Control {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Forwarder forwarder;

    public AbstractReceiver(Forwarder forwarder) {
        this.forwarder = forwarder;
        forwarder.register(this);
    }

    public void start() {
        forwarder.start();        
    }

    public void stop() {
        forwarder.stop();        
    }

    public void exit() {
        forwarder.exit();        
    }

    abstract public void receive(byte[] buffer);
}
