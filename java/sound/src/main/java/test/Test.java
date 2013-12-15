package test;

import javax.sound.sampled.AudioFormat;

import sound.Consumer;
import sound.Producer;
import sound.Source;
import sound.Target;
import sound.consumer.Port;
import sound.consumer.Shoutcast;
import sound.producer.Stream;

public class Test {
	public static void main(String[] args) {
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000f, 16, 2, 4, 48000f, true);
		try {
			//Producer p1 = new Target("Line-In (Creative SB X-Fi)");
			Producer p2 = new Target("Line 1 (Virtual Audio Cable)", audioFormat);
			Producer p3 = new Stream("http://ics2gss.omroep.nl:80/3fm-bb-mp3");
			Producer p4 = new Stream("http://sc7.mystreamserver.com:8004");

			Consumer c1 = new Source("Java Sound Audio Engine");
			Consumer c2 = new Port("Speakers (Creative SB X-Fi)");
			Consumer c3 = new Shoutcast();

			//Utils.write(p3.getInputStream(), new File("stream.out"));
			//Utils.play(p3.getInputStream());
			c3.start(p3);

			while (true) {
				Thread.sleep(3000);
				c2.stop();
				Thread.sleep(1000);
				c2.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}