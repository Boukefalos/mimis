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
package test;

import javax.sound.sampled.AudioFormat;

import sound.Consumer;
import sound.Producer;
import sound.Source;
import sound.consumer.Port;
import sound.consumer.Shoutcast;
import sound.producer.Stream;

public class Test {
    public static void main(String[] args) {
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000f, 16, 2, 4, 48000f, true);
        try {
            //Producer p1 = new Target("Line-In (Creative SB X-Fi)");
            //Producer p2 = new Target("Line 1 (Virtual Audio Cable)", audioFormat);
            //p2.start();
            Producer p3 = new Stream("http://ics2gss.omroep.nl:80/3fm-bb-mp3");
            Producer p4 = new Stream("http://sc7.mystreamserver.com:8004");

            Consumer c1 = new Source("Java Sound Audio Engine");
            Consumer c2 = new Port("Speakers (Creative SB X-Fi)");
            Consumer c3 = new Shoutcast();
            //Consumer c4 = new Player();
            //Consumer c5 = new Writer(new File("stream.out"));

            //Utils.write(p3.getInputStream(), new File("stream.out"));
            //Utils.play(p3.getInputStream());
            c3.start(p4);

            //while (true) {
                //Thread.sleep(300000);
                //c1.stop();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}