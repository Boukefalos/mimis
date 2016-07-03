package connected;

import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import yaml.Contact;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

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

