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
package base.util;

public class BufferedArrayCycle<E extends Bufferable> extends ArrayCycle<E> {
    protected static final long serialVersionUID = 1L;

    protected ArrayCycle<? extends Bufferable> buffer;
    protected int before;
    protected int after;
    protected int indexFirst;
    protected int indexLast;
    //protected int indexBuffer;
    //protected Bufferable[] bufferableArray;

    @SuppressWarnings("unchecked")
    public BufferedArrayCycle(int before, int after) {
        this.before = before;
        this.after = after;
        indexFirst = 0;
        indexLast = 0;
        //bufferableArray = new Bufferable[before + after + 1];
        //buffer = new ArrayCycle<Bufferable>();
        
    }

    public E previous() {
        get(indexFirst).unload();
        indexFirst = previous(indexFirst);
        indexLast = previous(indexLast);
        get(indexLast).load();
        // eerste before weg

        // eerste after wordt huidig
        
        // voeg laatste after toe
        
        return current();
    }

    public E next() {

        // eerste before weg

        // eerste after wordt huidig        
        
        // voeg laatste after toe
        
        return size() == 0 ? null : get(index);
    }

    protected int previous(int index) {
        if (--index < 0) {
            index = Math.max(0, size() - 1);
        }
        return index;
    }

    protected int next(int index) {
        System.out.println(index);
        if (++index >= size()) {
            index = 0;
        
        }
        return index;
    }

    public static void main(String[] args) {
        BufferedArrayCycle<Dummy> bac = new BufferedArrayCycle<Dummy>(2, 3);
        for (int i = 1; i <= 10; ++i) {
            bac.add(new Dummy(i));
        }
        bac.remove(0);
        System.out.println(bac.get(2).id);
    }
}