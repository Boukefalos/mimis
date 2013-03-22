package sound;

import javax.sound.sampled.AudioFormat;

public interface Format extends Cloneable {
	public interface Standard extends Format {
		public AudioFormat getAudioFormat();
	}

	public interface Mp3 extends Format {
		public int getRate();
	}
}