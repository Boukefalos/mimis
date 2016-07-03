package sound.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.Port.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tool {
    protected static Logger logger = LoggerFactory.getLogger(Tool.class);

    protected static HashMap<String, Device<TargetDataLine>> targetMap;
    protected static HashMap<String, Device<SourceDataLine>> sourceMap;
    protected static ArrayList<String> portList;

    protected static ArrayList<String> targetList;
    protected static ArrayList<String> sourceList;

    static {
        Tool tool = new Tool();

        targetMap = new HashMap<String, Device<TargetDataLine>>();
        sourceMap = new HashMap<String, Device<SourceDataLine>>();
        targetList = new ArrayList<String>();
        sourceList = new ArrayList<String>();
        portList = new ArrayList<String>();

        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            String name = mixerInfo.getName();
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
    
            for (Line.Info lineInfo :  mixer.getSourceLineInfo()) {
                String lineClassName = lineInfo.getLineClass().getName();
                if (lineClassName.equals("javax.sound.sampled.SourceDataLine")) {
                    if (mixer.isLineSupported(lineInfo)) {
                        logger.debug("<Source> " + name);
                        sourceMap.put(name, tool.new Device<SourceDataLine>(mixer, lineInfo));
                    }
                }
            }
            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
                String lineClassName = lineInfo.getLineClass().getName();
                if (lineClassName.equals("javax.sound.sampled.TargetDataLine")) {
                    if (mixer.isLineSupported(lineInfo)) {
                        logger.debug("<Target> " + name);
                        targetMap.put(name, tool.new Device<TargetDataLine>(mixer, lineInfo));
                    }
                } else if (lineClassName.equals("javax.sound.sampled.Port")) {
                    name = name.substring(5);
                    try {
                        Port port = (Port) mixer.getLine(lineInfo);
                        Port.Info portInfo =  (Info) port.getLineInfo();
                        if (!targetMap.containsKey(name) || portInfo.equals(Port.Info.LINE_OUT) || portInfo.equals(Port.Info.SPEAKER)) {
                            logger.debug("<Port> " + name);
                            portList.add(name);
                        }
                    } catch (LineUnavailableException e) {
                        logger.error("", e);
                    }
                }
            }
        }
    }

    public static String[] getTargets() {
        return targetMap.keySet().toArray(new String[0]);        
    }

    public static String[] getSources() {
        return sourceMap.keySet().toArray(new String[0]);        
    }

    public static String[] getPorts() {
        return portList.toArray(new String[0]);        
    }

    public static TargetDataLine getTargetDataLine(String name) throws LineUnavailableException {
        if (targetMap.containsKey(name)) {
            return targetMap.get(name).getLine();
        } else {
            throw new LineUnavailableException();
        }
    }

    public static TargetDataLine getTargetDataLine(String name, AudioFormat audioFormat) throws LineUnavailableException {
        if (targetMap.containsKey(name)) {
            return targetMap.get(name).getLine(audioFormat);
        } else {
            throw new LineUnavailableException();
        }
    }

    public static SourceDataLine getSourceDataLine(String name) throws LineUnavailableException {
        if (sourceMap.containsKey(name)) {
            return sourceMap.get(name).getLine();
        } else {
            throw new LineUnavailableException();
        }
    }

    public static SourceDataLine getSourceDataLine(String name, AudioFormat audioFormat) throws LineUnavailableException {
        if (sourceMap.containsKey(name)) {
            return sourceMap.get(name).getLine(audioFormat);
        } else {
            throw new LineUnavailableException();
        }
    }

    public class Device<T> {
        protected Mixer mixer;
        protected Line.Info lineInfo;

        public Device(Mixer mixer, Line.Info lineInfo) {
            this.mixer = mixer;
            this.lineInfo = lineInfo;
        }

        @SuppressWarnings("unchecked")
        public T getLine() throws LineUnavailableException {
            return (T) mixer.getLine(lineInfo);
        }

        @SuppressWarnings("unchecked")
        public T getLine(AudioFormat audioFormat) throws LineUnavailableException {        
            DataLine.Info dataLineInfo = new DataLine.Info(lineInfo.getLineClass(), audioFormat);
            return (T) mixer.getLine(dataLineInfo);
        }
    }
}
