package old;

import sound.Consumer;
import sound.Producer;

public abstract class Transducer implements Consumer, Producer {
    public int rate;

    public Transducer(Producer producer) {
        //setProducer(producer);
    }

    public int getRate() {
        return rate;
    }
}
