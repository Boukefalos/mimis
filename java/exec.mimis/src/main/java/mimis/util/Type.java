/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
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
package mimis.util;

public class Type {
    public static final int BITS_PER_BYTE = 8;
    
    public static byte[] intToBytes(int paramInt) {
        return intToBytes(paramInt, true);
    }

    public static byte[] intToBytes(int paramInt, boolean paramBoolean) {
        byte[] arrayOfByte = new byte[4];
        int i;
        if (paramBoolean) {
            for (i = 0; i < arrayOfByte.length; i++) {
                arrayOfByte[(arrayOfByte.length - i - 1)] = (byte) (paramInt >> i
                        * BITS_PER_BYTE & 0xFF);
            }
        } else {
            for (i = 0; i < arrayOfByte.length; i++) {
                arrayOfByte[i] = (byte) (paramInt >> i * BITS_PER_BYTE & 0xFF);
            }
        }
        return arrayOfByte;
    }
}
