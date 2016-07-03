package sound.util;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.sound.sampled.AudioFormat;

import sound.util.SoxBuilder.Option.Combine;
import sound.util.SoxBuilder.Option.Replay;

public final class SoxBuilder {
    protected static SoxBuilder instance;
    protected static HashMap<String, String> optionMap;
    protected static String files;
    protected static String effects;

    static {
        instance = new SoxBuilder();
        reset();
    }

    public static void reset() {
        optionMap = new HashMap<String, String>();
        files = "";
        effects = "";
    }

    public static SoxBuilder setOption(Option option, String value) {
        optionMap.put(option.getCode(), value);
        return instance;
    }

    public static SoxBuilder setOption(Option option) {
        return SoxBuilder.setOption(option, "");
    }

    public static SoxBuilder setOption(Option option, int value) {
        return SoxBuilder.setOption(option, String.valueOf(value));
    }

    public static SoxBuilder setOption(Option option, Combine combine) {
        return SoxBuilder.setOption(option, combine.getCode());
    }

    public static SoxBuilder setOption(Combine combine) {
        return SoxBuilder.setOption(Option.COMBINE, combine);
    }

    public static SoxBuilder setOption(Option option, Replay replay) {
        return SoxBuilder.setOption(option, replay.toString().toLowerCase());
    }

    public static SoxBuilder setOption(Replay replay) {
        return SoxBuilder.setOption(Option.REPLAY, replay);
    }

    public static SoxBuilder addFile(File file) {
        files = String.format("%s %s", files, file.build());
        return instance;        
    }

    public static SoxBuilder addEffect(Effect effect) {
        effects = String.format("%s %s", effects, effect.build());
        return instance;        
    }

    public String build() {
        String build = "sox";
        for (Entry<String, String> entry  : optionMap.entrySet()) {
            String value = entry.getValue();
            if (value.equals("")) {
                build = String.format("%s %s", build, entry.getKey());                
            } else {
                String option = String.format("%s %s", entry.getKey(), value);
                build = String.format("%s %s", build, option);
            }
        }
        build = String.format("%s%s%s", build, files, effects);
        reset();
        return build;
    }

    public enum Environment {
        AUDIODRIVER, AUDIODEV
    }

    public enum Option {
        BUFFER            ("--buffer"),             // default=8192
        INPUT_BUFFER    ("--input-buffer"),
        CLOBBER            ("--clobber"),
        COMBINE         ("--combine"),            // |Combine|
        NO_DITHER        ("--no-dither"),         // (-D)
        EFFECTS_FILE    ("--efects-file"),
        GUARD            ("--guard"),            // (-G)
        MIX                ("-m"),                    // (--combine mix)
        MERGE            ("-M"),                    // (--combine merge)
        MAGIC            ("--magic"),
        MULTI_THREADED    ("--multi-threaded"),
        SINGLE_THREADED    ("--single-threaded"),
        NORM            ("--norm"),                // [=dB-level]
        PLAY_RATE_ARG    ("--play-rate-arg"),
        QUIET            ("--no-show-progress"),    // (-q)
        REPEATABLE        ("-R"),
        REPLAY            ("--replay-gain"),        // |Replay|
        MULTIPLY        ("-T"),                    // (--combine multiply)
        TEMP            ("--temp");

        protected String code;

        private Option(String code) {
            this.code = code;
        }
    
        public String getCode() {
            return code;
        }

        public enum Combine {
            CONCATENATE ("concatenate"),
            MERGE         ("merge"),        // (-M)
            MIX             ("mix"),        // (-m)
            MIX_POWER    ("mix-power"),
            MULTIPLY     ("multiply"),    // (-T)
            SEQUENCE     ("sequence");

            protected String code;

            private Combine(String code) {
                this.code = code;
            }
        
            public String getCode() {
                return code;
            }
        }
        
        public enum Replay {
            TRACK, ALBUM, OFF
        }
    }

    public static class File {
        protected static File instance;

        protected static HashMap<String, String> optionMap;
        protected static Type type;

        static {
            instance = new File();
            reset();
        }

        public static void reset() {
            optionMap = new HashMap<String, String>();
            type = Type.PIPE;
        }
    
        public static File setOption(Option option, String value) {
            optionMap.put(option.getCode(), value);
            return instance;
        }

        public static File setOption(Option option) {
            return File.setOption(option, "");
        }

        public static File setOption(Option option, int value) {
            return File.setOption(option, String.valueOf(value));
        }

        public static File setOption(Option option, Encoding encoding) {
            return File.setOption(option, encoding.getCode());
        }

        public static File setOption(Encoding encoding) {
            return File.setOption(Option.ENCODING, encoding);
        }

        public static File setOption(Option option, Format format) {
            return File.setOption(option, format.toString().toLowerCase());
        }

        public static File setOption( Format format) {
            return File.setOption(Option.FORMAT, format);
        }

        public static File setOption(Option option, Endian endian) {
            return File.setOption(option, endian.toString().toLowerCase());
        }

        public static File setOption(Endian endian) {
            return File.setOption(Option.ENDIAN, endian);
        }
        
        public File setOptions(AudioFormat audioFormat) {
            setOption(Option.CHANNELS, audioFormat.getChannels());
            setOption(Option.RATE, String.format("%sk", String.valueOf(audioFormat.getSampleRate() / 1000f)));
            AudioFormat.Encoding encoding = audioFormat.getEncoding();
            int bits = audioFormat.getSampleSizeInBits();
            if (encoding.equals(AudioFormat.Encoding.ALAW)) {
                setOption(Format.AL);
                setOption(Encoding.A_LAW);
            } else if (encoding.equals(AudioFormat.Encoding.ULAW)) {
                setOption(Format.UL);
                setOption(Encoding.U_LAW);
            } else if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                setOption(Format.valueOf(String.format("S%d", bits)));
                setOption(Encoding.SIGNED_INTEGER);
            } else if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                setOption(Format.valueOf(String.format("U%d", bits)));
                setOption(Encoding.UNSIGNED_INTEGER);
            }
            setOption(audioFormat.isBigEndian() ? Endian.BIG : Endian.LITTLE);
            return instance;
        }

        public static File setType(Type type) {
            File.type = type;
            return instance;
        }

        public String build() {            
            String build = type.getCode();
            for (Entry<String, String> entry  : optionMap.entrySet()) {
                String value = entry.getValue();
                if (value.equals("")) {
                    build = String.format("%s %s", entry.getKey(), build);                
                } else {
                    String option = String.format("%s %s", entry.getKey(), value);
                    build = String.format("%s %s", option, build);
                }
            }
            reset();
            return build;
        }

        public enum Option {
            BITS            ("--bits"),                // (-b)
            CHANNELS        ("--channels"),            // (-c)
            ENCODING         ("--encoding"),            // (-e), |Encoding|
            NO_GLOB            ("--no-glob"),
            RATE            ("--rate"),                // (-r)
            FORMAT            ("--type"),                // (-t), |Format|
            ENDIAN            ("--endian"),            // (-L, -B, -x), |Endian|
            REVERSE_NIBBLES    ("--reverse-nibbles"),    // (-N)
            REVERSE_BITS    ("--reverse-bits"),        // (-X)
            /* Input only */
            IGNORE_LENGTH    ("--ignore-length"),
            VOLUME            ("--volume"),            // (-v)
            /* Output only */
            ADD_COMMENT        ("--add-comment"),
            COMMENT            ("--comment"),
            COMMENT_FILE    ("--comment-file"),
            COMPRESSION        ("--compression");        // -C
            
            protected String code;

            private Option(String code) {
                this.code = code;
            }

            public String getCode() {
                return code;
            }
        }

        public enum Encoding {
            SIGNED_INTEGER        ("signed-integer"),         // PCM data stored as signed integers
            UNSIGNED_INTEGER    ("unsigned-integer"),    // PCM data stored as unsigned integers
            FLOATING_POINT        ("floating-point"),        // PCM data stored as single precision (32-bit) or double precision (64-bit) floating-point numbers
            A_LAW                ("a-lawW"),                // International telephony standard for logarithmic encoding to 8 bits per sample (~13-bit PCM)
            U_LAW                ("u-law"),                // North American telephony standard for logarithmic encoding to 8 bits per sample (~14-bit PCM)
            MU_LAW                ("mu-law"),                // alias for u-law (~14-bit PCM)
            OKI_ADPCM            ("oki-adpcm"),            // OKI (VOX, Dialogic or Intel) 4-bit ADPCM (~12-bit PCM)
            IMA_ADPCM            ("ima-adpcm"),            // IMA (DVI) 4-bit ADPCM (~13-bit PCM)
            MS_ADPCM            ("ms-adpcm"),            // Microsoft 4-bit ADPCM (~14-bit PCM)
            GSM_FULL_RATE        ("gsm-full-rate");        // Several audio formats used for digital wireless telephone calls

            protected String code;

            private Encoding(String code) {
                this.code = code;
            }

            public String getCode() {
                return code;
            }
        }

        public enum Format {
            AIF, AIFC, AIFF, AIFFC, AL, AMB, AMR, ANY, ARL, AU, AVR, BIN, CAF, CDDA, CDR, CVS, CVSD, CVU, DAT, DVMS, EDU, F32, F64, FAP, FLAC, FSSD, GSM, GSRT, HCOM, HTK, IMA, IRCAM, LA, LPC, LPC10, LU, M3U, M4A, MAT, MAT4, MAT5, MAUD, MP2, MP3, MP4, NIST, OGG, PAF, PLS, PRC, PVF, RAW, S16, S24, S32, S8, SD2, SDS, SF, SLN, SMP, SND, SNDR, SNDT, SOU, SOX, SPH, TXW, U16, U24, U32, U8, UL, VMS, VOC, VORBIS, VOX, W64, WAV, WAVPCM, WV, WVE, XA, XI; 
        }

        public enum Endian {
            LITTLE, BIG, SWAP;
        }

        public enum Type {
            STANDARD    ("-"),    // -t must be given
            PIPE         ("-p"), // (--sox-pipe)
            DEVICE        ("-d"), // (--default-device)
            NULL        ("-n"); // (--null)

            protected String code;
        
            private Type(String code) {
                this.code = code;
            }
        
            public String getCode() {
                return code;
            }
        }
    }

    public class Effect {
        public String build() {
            return null;
        }        
    }
}
