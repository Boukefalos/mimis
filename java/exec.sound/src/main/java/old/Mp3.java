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
package old;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import sound.util.Utils;
import base.exception.worker.ActivateException;

public class Mp3 extends Converter {
    protected File file;
    protected String title;

    public Mp3(File file) {
        this(file, -1);        
    }

    public Mp3(File file, int targetRate) {
        super(null, targetRate);
        setFile(file);
        title = "";
    }

    public synchronized void activate() throws ActivateException {
        /* Open file */
        try {
            sourceInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("", e);
            throw new ActivateException();
        }

        /* Read ID3V2 tags */
        try {
            MP3File mp3File = new MP3File(file);
            String album = clean(mp3File.getID3v2Tag().getAlbumTitle());
            String artist = clean(mp3File.getID3v2Tag().getLeadArtist());
            String track = clean(mp3File.getID3v2Tag().getSongTitle());
            if (album.isEmpty()) {
                title = String.format("%s - %s", artist, track, album);
            } else {
                title = String.format("%s - %s {%s}", artist, track, album);
            }
            logger.debug("Title: " + title);
        } catch (IOException e) {
            logger.error("", e);
        } catch (TagException e) {
            logger.error("", e);
        }
        try {
            sourceInputStream.skip(100000);
        } catch (IOException e) {}
        super.activate();
    }

    protected String clean(String input) {
        String output = input.replace("\0", "");
        return output.replace("ÿþ", "");
    }

    public String getTitle() {
        return title;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        final Mp3 mp3 = new Mp3(new File("input.mp3"), 128);
        Utils.write(mp3.getInputStream(), new File("one.mp3"));
        mp3.setFile(new File("stream.mp3"));
        Utils.write(mp3.getInputStream(), new File("two.mp3"));
        mp3.exit();
    }
}
