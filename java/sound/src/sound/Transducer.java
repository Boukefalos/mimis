package sound;

import java.io.InputStream;

public class Transducer implements Consumer, Producer {
	public int rate;

	public Transducer(Producer producer) {
		//setProducer(producer);
	}

	public int getRate() {
		return rate;
	}

	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public void start(Producer producer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
}
