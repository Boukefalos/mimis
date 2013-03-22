package sound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;

import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.worker.Worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sound.Format.Standard;
import sound.SoxBuilder.File;
import sound.SoxBuilder.Option;
import sound.SoxBuilder.File.Type;

public class Port extends Worker implements Consumer {
	protected Log log = LogFactory.getLog(getClass());

	protected static final int BUFFER_SIZE = 1024 * 4; // in bytes

	protected String device;
	protected Producer producer;
	protected Process process;
	protected InputStream producerInputStream;
	protected OutputStream processOutputStream;
	protected ProcessBuilder processBuilder;

	public Port() {
		this("0");
	}

	public Port(String device) {
		this.device = device;
	}

    @SuppressWarnings("static-access")
	public void start(Producer producer) {
		this.producer = producer;
		producerInputStream = producer.getInputStream();
		
		String command = "";
		if (producer instanceof Standard) {
			AudioFormat audioFormat = ((Standard) producer).getAudioFormat();
			SoxBuilder.addFile(File.setType(Type.STANDARD).setOptions(audioFormat));			
		} else if (producer instanceof Format.Mp3) {
			SoxBuilder.addFile(File.setType(Type.STANDARD).setOption(File.Format.MP3));
		}
		command = SoxBuilder
			.setOption(Option.QUIET)
			.addFile(File.setType(Type.DEVICE))
			.build();
		
		log.debug(String.format("Build process (\"%s\")", command));
		processBuilder = new ProcessBuilder(command.split(" "));
		processBuilder.environment().put("AUDIODEV", device);

		start(true);
	}

	protected void activate() throws ActivateException {
    	producer.start();
    	if (process == null) {
			try {
				process = processBuilder.start();
			} catch (IOException e) {
				log.error(e);
				throw new ActivateException();
			}
			processOutputStream = process.getOutputStream();
    	}
        super.activate();
    }

    protected void deactivate() throws DeactivateException {
    	super.deactivate();
    	try {
			processOutputStream.flush();
		} catch (IOException e) {
			log.error(e);
			throw new DeactivateException();
		}
    }

    public void exit() {
    	try {
			log.debug("close process output stream");
			processOutputStream.close();

			log.debug("wait for process to terminate");
			process.waitFor();
		} catch (IOException e) {
			log.error(e);
		} catch (InterruptedException e) {
			log.error(e);
		} finally {
			process = null;
		}
    }

	protected void work() {
		try {			
			byte[] buffer = new byte[BUFFER_SIZE];
	        int read = producerInputStream.read(buffer, 0, buffer.length);
	        if (read > 0) {
	        	processOutputStream.write(buffer, 0, read);
	        } else {
	        	exit();
	        }
		} catch (IOException e) {
			log.error(e);
			exit();
		}
	}
}
