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
package test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Test {
    public static void main(String[] args) {
        try {
            new Test().start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void start() throws Throwable {
        input((Object) new A());
        input((Object) new B());
        input((Object) new String[] {"a", "b"});
    }

    public void input(Object object) throws Throwable {
        System.out.println("Object");
        MethodType methodType = MethodType.methodType(void.class, object.getClass());
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle = lookup.findVirtual(getClass(), "input", methodType);
        try {
            methodHandle.invoke(this, object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void input(A object) {
        System.out.println("A");
    }

    public void input(B object) {
        System.out.println("B");
    }

    public void input(String[] object) {
        System.out.println("String[]");
    }

    public class A {
        
    }

    public class B {
        
    }
}
