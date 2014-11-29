package mimis.util;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import wiiusej.Wiimote;
import mimis.device.wiimote.WiimoteDevice;
import mimis.device.wiimote.WiimoteService;

//Sample Rate - equation is y = -280x + 7280, where x is the actual sample rate. ex: sample rate of 4200 = 0x0B
//Freq (y=real, x=wii) y=-1070*ln(x)+4442.6 of 2788.1*e^(-0.041*x)

public class Sound {
    public static final byte PCM = 0x40;  // signed 8-bit PCM
    public static final byte ADPCM = 0x00; // Yamaha 4-bit ADPCM
    public static final byte BLOCK_SIZE = 20;

    public static final int yamaha_indexscale[] = {
        230, 230, 230, 230, 307, 409, 512, 614,
        230, 230, 230, 230, 307, 409, 512, 614};

    public static final int yamaha_difflookup[] = {
        1, 3, 5, 7, 9, 11, 13, 15,
        -1, -3, -5, -7, -9, -11, -13, 15};
        
    public Object object = new Object();

    public static void main(String[] args) {
        /*File file = new File("sound.wav");
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat au = inputStream.getFormat();
            System.out.println(au.getSampleRate());// Hz
            System.out.println(au.getSampleSizeInBits());// bits
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        new Sound().start();
    }

    public void start() {
        File file = new File("1kSine16 (3130).wav");
        play(file);
        System.exit(0);//if (true) return;

        WiimoteService wiimoteService = new WiimoteService();
        try {
            WiimoteDevice wiimoteDevice = new WiimoteDevice();
            Wiimote wiimote = wiimoteService.getDevice(wiimoteDevice);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat audioFormat = audioInputStream.getFormat();
           // BufferedSound bufferedSound = bufferSound(inputStream);
            System.out.println(audioInputStream.getFormat().getSampleRate());
            System.out.println(audioInputStream.getFormat().getFrameRate());

            //System.out.println(bufferedSound.getSampleRate());
            //byte rate = (byte) (48000 / bufferedSound.getSampleRate());
            //wiimote.setSpeakerRate((byte) rate, (byte) 0x00);

            wiimote.setSpeakerFormat(PCM);
            //wiimote.setSpeakerRate((byte) 0, rate);
            wiimote.setSpeakerRate((byte) 0x00, (byte) (48000 / audioFormat.getSampleRate())); // pcm
            //wiimote.setSpeakerRate((byte) 0xd0, (byte) 0x07); // adpcm
            wiimote.setSpeakerVolume(1);

            wiimote.activateSpeaker();

           /* File file = new File("volbeat_pcm_u8_32_1500.raw");
            FileInputStream fin = new FileInputStream(file);
            byte[] block = new byte[20];            
            while (fin.read(block) != -1) {
                wiimote.streamSpeakerData(block);
            }*/

            //playBufferedSound(wiimote, bufferedSound, step);
            playSound(wiimote, audioInputStream);
            synchronized (object) {
                object.wait();
            }
            wiimoteService.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(final Wiimote wiimote, final AudioInputStream audioInputStream) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {                
                try {
                    byte[] buffer = new byte[20];
                    if (audioInputStream.read(buffer) != -1) {
                        buffer[0] = -2;
                        System.out.format("%2x\n", buffer[0]);
                        wiimote.streamSpeakerData(buffer);
                    } else {
                        cancel();
                        synchronized (object) {
                            object.notifyAll();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }        
            }
        };
        AudioFormat audioFormat = audioInputStream.getFormat();
        double sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8D;
        double samplesPerBlock = BLOCK_SIZE / sampleSizeInBytes;
        int step = (int) Math.round(1000 * samplesPerBlock / (float) audioFormat.getSampleRate());
        System.out.println("step: " + step);
        timer.scheduleAtFixedRate(timerTask, 0, step);        
    }

    public void playBufferedSound(final Wiimote wiimote, final BufferedSound bufferedSound, int step) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int i = 0;
            public void run() {
                wiimote.streamSpeakerData(bufferedSound.getReport(i));
                /*if (i % 10 == 0) {
                    wiimote.setSpeakerVolume(Math.random());
                    wiimote.setSpeakerConfig();
                }*/
                if (++i > bufferedSound.numReports()) {
                    cancel();
                    object.notifyAll();                    
                }                
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, step);
    }

    public BufferedSound bufferSound(AudioInputStream audioInputStream) throws IOException {
        AudioFormat audioFormat = audioInputStream.getFormat();
        int size = (int) (audioInputStream.getFrameLength() * audioFormat.getFrameSize());
        byte[][] sound = new byte[size / BLOCK_SIZE + 1][BLOCK_SIZE];
        for (int i = 0; i < sound.length; ++i) {
            byte[] block = new byte[BLOCK_SIZE];
            int j = 0;
            do {
                int read = audioInputStream.read(block, j, BLOCK_SIZE - j);
                if (read == -1) {
                    break;
                }
                j += read;
            } while (j < BLOCK_SIZE);
            for (j = 0; j < BLOCK_SIZE; ++j) {
                if ((i * BLOCK_SIZE + j) > size) {
                    break;
                }
                //sound[i][j] = block[j];
                sound[i][j] = (byte) ((j % 2 == 0) ? 0x33 : 0xcc);
                //sound[i][j] = (byte) (Math.random() * 0xff);
            }
        }
        audioInputStream.close();
        return new BufferedSound(sound, (int) audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits());
    }

    public void play(File file) {

        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);            
            AudioFormat audioFormat = audioInputStream.getFormat();
            System.out.println(audioFormat.getEncoding());
            System.out.println(audioFormat.getSampleRate());
            System.out.println(audioFormat.getFrameRate());
            AudioFormat newFormat = new AudioFormat(
                 AudioFormat.Encoding.PCM_SIGNED,
                 3130, 8, 1, 1, 3130, false);
            AudioInputStream newAudioInputStream = new AudioInputStream(
                audioInputStream,
                newFormat,
                audioInputStream.getFrameLength() * audioFormat.getFrameSize());
            play(newAudioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(AudioInputStream audioInputStream) {
        try {
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            int i;
            byte[] buffer = new byte[128];
            while ((i = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                sourceDataLine.write(buffer, 0, i);
            }
            sourceDataLine.drain();
            sourceDataLine.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
