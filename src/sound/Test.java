package sound;

import javax.sound.sampled.AudioFormat;

public class Test {
	public static void main(String[] args) {
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000f, 16, 2, 4, 48000f, true);
		try {
			Producer p1 = new Target("Line-In (Creative SB X-Fi)");
			Producer p2 = new Target("Line 1 (Virtual Audio Cable)", audioFormat);
			Producer p3 = new Stream("http://ics2gss.omroep.nl:80/3fm-bb-mp3");
			Consumer c1 = new Source("Java Sound Audio Engine");
			Consumer c2 = new Port("Speakers (Creative SB X-Fi)");

			c2.start(p3);

			/*while (true) {
				Thread.sleep(3000);
				c2.stop();
				Thread.sleep(1000);
				c2.start();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}