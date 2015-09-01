package test.lines;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class SourceLine {
    //private Mixer mixer;
    private SourceDataLine line;

    public SourceLine(Mixer mixer, SourceDataLine line) {
        //this.mixer = mixer;
        this.line = line;
        System.out.println("SOURCE " + mixer.getMixerInfo().getName() + " || " + line.getLineInfo());
    }

    public int write(byte[] bytes, int offset, int length) {
        return line.write(bytes, offset, length);
    }
}
