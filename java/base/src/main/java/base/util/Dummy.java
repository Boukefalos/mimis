package base.util;

public class Dummy implements Bufferable {
    public int id;
    
    public Dummy(int id) {
        this.id = id;
    }

    public void load() {
        System.out.println("Dummy #" + id + ": load()");        
    }

    public void unload() {
        System.out.println("Dummy #" + id + ": load()");        
    }

}
