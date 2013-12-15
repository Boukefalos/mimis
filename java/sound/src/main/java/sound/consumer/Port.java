package sound.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;

import sound.Consumer;
import sound.Format;
import sound.Format.Standard;
import sound.Producer;
import sound.util.SoxBuilder;
import sound.util.SoxBuilder.File;
import sound.util.SoxBuilder.File.Type;
import sound.util.SoxBuilder.Option;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;

public class Port extends Worker implements Consumer {
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

		logger.debug(String.format("Build process (\"%s\")", command));
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
				logger.error("", e);
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
			logger.error("", e);
			throw new DeactivateException();
		}
    }

    public void exit() {
    	try {
			logger.debug("close process output stream");
			processOutputStream.close();

			logger.debug("wait for process to terminate");
			process.waitFor();
		} catch (IOException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
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
			logger.error("", e);
			exit();
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
