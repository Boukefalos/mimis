package test.lines;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class TargetLine {
    //private Mixer mixer;
    private TargetDataLine line;

    public TargetLine(Mixer mixer, TargetDataLine line) {
        //this.mixer = mixer;
        this.line = line;
        System.out.println("TARGET " + mixer.getMixerInfo().getName() + " || " + line.getLineInfo());
    }

    public int read(byte[] bytes, int offset, int length) {
        return line.read(bytes, offset, length);
    }
}
