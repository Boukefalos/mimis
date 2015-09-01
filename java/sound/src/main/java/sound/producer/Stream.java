package sound.producer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

import sound.Format;
import sound.Producer;
import sound.stream.HoardedInputStream;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

import com.Ostermiller.util.CircularByteBuffer;
import com.Ostermiller.util.CircularObjectBuffer;

public class Stream extends Work implements Producer, Format.Mp3 {
    public static final int STEP = 80; // in milliseconds

    protected String http;
    protected Socket socket;
    protected InputStream socketInputStream;
    protected OutputStreamWriter socketOutputStreamWriter;
    protected HoardedInputStream hoardedInputStream;
    protected int meta;
    protected int rate;
    protected int chunk;
    protected int untilMeta;
    protected CircularByteBuffer audioCircularByteBuffer;
    protected CircularObjectBuffer<String> metaCircularObjectBuffer;
    protected String metaData;

    public Stream(String http) {
        super();
        this.http = http;
        meta = -1;
        rate = -1;
        audioCircularByteBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        metaCircularObjectBuffer = new CircularObjectBuffer<String>();        
    }

    protected void connect(URL url) {
        try {
            /* Open socket communication */
            socket = new Socket(url.getHost(), url.getPort());
            socketInputStream = socket.getInputStream();
            socketOutputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            /* Write stream request */
            if (url.getFile().equals("")) {
                socketOutputStreamWriter.write("GET / HTTP/1.1\r\n");
            } else {
                socketOutputStreamWriter.write("GET " + url.getFile() + " HTTP/1.1\r\n");
            }
            socketOutputStreamWriter.write("Host: " + url.getHost() + "\r\n");
            //socketOutputStreamWriter.write("Icy-MetaData: 1\r\n");
            socketOutputStreamWriter.write("Connection: close\r\n");
            socketOutputStreamWriter.write("\r\n");
            socketOutputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activate() throws ActivateException {
        try {
            /* Initialize connection */
            URL url = new URL(http);

            /* Parse headers */
            connect(url);
            InputStreamReader inputStreamReader = new InputStreamReader(socketInputStream);
            StringBuffer stringBuffer = new StringBuffer();
            char character;
            int skip = 0;
            while ((character = (char) inputStreamReader.read()) > 0) {
                ++skip;
                if (character == '\n') {
                    /* Fetch relevant headers */
                    String line = stringBuffer.toString().trim();
                    if (line.startsWith("icy-metaint")) {
                        meta = Integer.valueOf(line.substring(line.indexOf(":") + 1).trim());
                    } else if (line.startsWith("icy-br")) {
                        rate = Integer.valueOf(line.substring(line.indexOf(":") + 1).trim());
                    } else if (line.equals("")) {
                        break;
                    }
                    stringBuffer = new StringBuffer();
                } else {
                    stringBuffer.append(character);
                }
            }
            inputStreamReader.close();

            /* Reconnect to bypass pre-buffering problems */
            connect(url);
            socketInputStream = socket.getInputStream();
            socketInputStream.skip(skip);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        /* Calculate streaming parameters */
        //untilMeta = meta;
        chunk = STEP * rate / 8;
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        audioCircularByteBuffer.clear();
        metaCircularObjectBuffer.clear();
        try {
            hoardedInputStream.clear();
        } catch (IOException e) {
            logger.error("", e);
            throw new DeactivateException();
        }
    }

    public void work() {
        int left = chunk;

        /* Handle media at appropriate times *
        while (meta > 0 && left >= untilMeta) {
            stream(untilMeta);
            left -= untilMeta;
            meta();
            untilMeta = meta;
        }*/

        /* Stream at fixed rate */
        stream(left);
        //untilMeta -= left;
        sleep(STEP);
    }

    protected void stream(int length) {
        try {
            byte[] bytes = new byte[length];
            int read = 0;
            while (length > 0 && (read = socketInputStream.read(bytes)) > 0) {
                length -= read;
                audioCircularByteBuffer.getOutputStream().write(bytes);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            stop();
        }
    }

    protected void meta() {
        try {
            /* Retrieve data length */
            byte[] data = new byte[1];
            socketInputStream.read(data);

            int length = 16 * data[0];            
            data = new byte[length];
            socketInputStream.read(data);

            /* Check for new data */
            String newMetaData = new String(data);
            if (!newMetaData.isEmpty() && !newMetaData.equals(metaData)) {
                metaData = newMetaData;
                metaCircularObjectBuffer.write(new String(data));
                logger.debug("data: " +  metaData);
            }
            return;
        } catch (IOException e) {
            logger.error("", e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        stop();
        return;
    }

    public InputStream getInputStream() {
        if (hoardedInputStream == null) {
            hoardedInputStream = new HoardedInputStream(audioCircularByteBuffer.getInputStream());
        }
        return hoardedInputStream;
    }

    public CircularObjectBuffer<String> getMetaBufferStream() {
        return metaCircularObjectBuffer;        
    }

    public int getRate() {
        return rate;
    }
}
