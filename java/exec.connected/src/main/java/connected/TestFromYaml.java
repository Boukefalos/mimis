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
package connected;

import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import yaml.Contact;

public class TestFromYaml {
    public static void main(String[] args) throws Exception {
        YamlReader reader = new YamlReader(new FileReader("test.yml"));

       // while (true) {
            //Map contact = (Map) reader.read();
            //if (contact == null) break;
            //System.out.println(contact.get("age"));
            Class<?> clazz = Contact.class;
            for (Field field : clazz.getDeclaredFields()) {
                Class<?> type = field.getType();
                if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
                    System.out.println(field.getName());
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> x = (Class<?>) listType.getActualTypeArguments()[0];
                    System.out.println(x);
                    reader.getConfig().setPropertyElementType(clazz, field.getName(), x);
                }                
            }
            //reader.getConfig().setPropertyElementType(Contact.class, "phoneNumbers", Phone.class);
            
            Contact x = reader.read(Contact.class);
            System.out.println(x.phoneNumbers.get(0));
            System.out.println(x.phoneNumbers.get(0).number);
            //System.out.println(x.phoneNumbers.get(0).get("number"));
            YamlWriter writer = new YamlWriter(new OutputStreamWriter(System.out));
            //writer.write(contact);
            //writer.close();
      //  }

    }

}

