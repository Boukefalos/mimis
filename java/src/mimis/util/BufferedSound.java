package mimis.util;

public class BufferedSound {
    private byte[][] soundData;
    private int sampleRate;
    private int sampleSize;

    public BufferedSound(byte[][] soundData, int sampleRate, int sampleSize) {
        this.soundData = soundData;
        this.sampleRate = sampleRate;
        this.sampleSize = sampleSize;
    }

    public byte[] getReport(int paramInt) {
        return soundData[paramInt];
    }

    public int numReports() {
        return soundData.length;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getSampleSize() {
        return sampleSize;
    }
}
