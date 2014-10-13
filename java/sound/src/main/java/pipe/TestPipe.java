package pipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestPipe {

	private int namedPipeHandle;
	private String pipeName, srcFile;
	private int pipeBuffer = 131072, fileBuffer = 8192;

	public TestPipe(String pipeName, String srcFile) {
		this.pipeName = pipeName;
		this.srcFile = srcFile;
	}

	private void log(String message) {
		System.out.println(message);
	}

	private boolean createPipe() {
		namedPipeHandle = Pipe.CreateNamedPipe(
			pipeName,
			Pipe.PIPE_ACCESS_DUPLEX,
			Pipe.PIPE_WAIT,
			5,
			pipeBuffer,
			pipeBuffer,
			0xffffffff,
			0);
		if (namedPipeHandle == -1) {
			log("CreateNamedPipe failed for " + pipeName + " for error Message " + Pipe.FormatMessage(Pipe.GetLastError()));
		} else {
			log("Named Pipe " + pipeName + " created successfully Handle=" + namedPipeHandle);
		}
		return namedPipeHandle != -1;
	}

	private boolean connectToPipe() {
		log("Waiting for a client to connect to pipe " + pipeName);
		boolean connected = Pipe.ConnectNamedPipe(namedPipeHandle, 0);
		if (!connected) {
			int lastError = Pipe.GetLastError();
			if (lastError == Pipe.ERROR_PIPE_CONNECTED)
				connected = true;
		}
		log((connected ? "Connected to the pipe " : "Falied to connect to the pipe ") + pipeName);
		return connected;
	}

	public void runPipe() {
		if (createPipe() && connectToPipe()) {
			log("Client connected.");
			try {
				File f1 = new File(this.srcFile);
				InputStream in = new FileInputStream(f1);
				log("Sending data to the pipe");
				byte[] buf = new byte[fileBuffer];
				int len, bytesWritten;
				while ((len = in.read(buf)) > 0) {
					bytesWritten = Pipe.WriteFile(namedPipeHandle, buf, len);
					log("Sent " + len + "/" + bytesWritten 	+ " bytes to the pipe");
					if (bytesWritten == -1) {
						int errorNumber = Pipe.GetLastError();
						log("Error Writing to pipe " + Pipe.FormatMessage(errorNumber));
					}
				}
				in.close();
				Pipe.FlushFileBuffers(namedPipeHandle);
				Pipe.CloseHandle(namedPipeHandle);
				Pipe.DisconnectNamedPipe(namedPipeHandle);
				log("Writing to the pipe completed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String pipeName = "\\\\.\\pipe\\detest";
		String fileName = "txt/bla.txt";
		//fileName = "C:\\Users\\Rik\\Music\\Artists\\+44\\When Your Heart Stops Beating\\+44 - 155.mp3";
		TestPipe testPipe = new TestPipe(pipeName, fileName);
		testPipe.runPipe();
	}
}
