package yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact {
    public String name;
    public int age;
    public String address;
    public List<Phone> phoneNumbers; // HashMap<String,String>
    public Map<String,String> other;
    //public HashMap<Phone> x;
}
