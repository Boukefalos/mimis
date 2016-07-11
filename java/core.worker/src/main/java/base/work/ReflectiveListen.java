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
package base.work;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import base.worker.Worker;

public class ReflectiveListen extends Listen<Object> {
    public ReflectiveListen() {
        super();
    }
    
    public ReflectiveListen(Worker.Type workerType) {
        super(workerType);
    }

    public void input(Object object) {
        Class<?> clazz = object.getClass();        
        MethodType methodType = MethodType.methodType(void.class, clazz);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(getClass(), "input", methodType);
            methodHandle.invoke(this, object);
        } catch (Exception e) {
            logger.error("", e);
        } catch (Throwable e) {
            logger.error("", e);
        }
    }
}
