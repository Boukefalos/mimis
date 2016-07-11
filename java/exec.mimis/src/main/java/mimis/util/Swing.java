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
package mimis.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class Swing {
    protected static ClassLoader classLoader;
    protected static Toolkit toolkit;

    static {
        classLoader = Swing.class.getClassLoader();
        toolkit = Toolkit.getDefaultToolkit();
    }

    public static URL getResource(String name) {
        return classLoader.getResource(name);
    }

    public static Image getImage(String name) {
        return toolkit.getImage(getResource(name));
    }

    public static ImageIcon getImageIcon(String name) {
        return new ImageIcon(getResource(name));
    }
}
