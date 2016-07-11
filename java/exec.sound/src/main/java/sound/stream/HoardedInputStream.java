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
package sound.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoardedInputStream extends BufferedInputStream {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final int SLEEP = 500; // in milliseconds
    protected static final int BUFFER_SIZE = 30000; // in bytes
    protected static final int MINIMUM_SIZE = 1000; // in bytes

    protected int bufferSize;
    protected int minimumSize;
    protected boolean hoard;

    public HoardedInputStream(InputStream inputStream) {
        this(inputStream, BUFFER_SIZE, MINIMUM_SIZE);
    }

    public HoardedInputStream(InputStream inputStream, int bufferSize) {
        super(inputStream, bufferSize);        
        this.bufferSize = bufferSize;
        hoard = true;
    }
    
    public HoardedInputStream(InputStream inputStream, int bufferSize, int minimumSize) {
        this(inputStream, bufferSize);        
        this.minimumSize = minimumSize;
    }

    public int read() throws IOException {
        hoard();
        byte[] buffer = new byte[1];
        in.read(buffer, 0, 1);
        return (int) buffer[0];
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        hoard();
        in.read(buffer, offset, length);
        return length;
    }

    public void hoard() throws IOException {
        int available =  available();
        if (hoard && available < MINIMUM_SIZE) {
            long time = System.currentTimeMillis();
            do {
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {
                    logger.warn("", e);
                }
            } while (available() < BUFFER_SIZE);
            logger.debug(String.format("Buffered %d bytes in %s milliseconds", BUFFER_SIZE - available, System.currentTimeMillis() - time));
        }
    }

    public void clear() throws IOException {
        this.buf = new byte[buf.length];
        reset();
    }

    public void drain() {
        drain(true);
    }

    public void drain(boolean drain) {
        hoard = !drain;
    }
}
