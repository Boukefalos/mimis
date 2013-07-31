package pipe;
/**
 * 
 * @author Vikram S Khatri vikram.khatri@us.ibm.com
 *
 */
public class Pipe
{
	static final int ERROR_PIPE_CONNECTED = 535;
	static final int ERROR_BROKEN_PIPE = 109;
	static final int PIPE_ACCESS_DUPLEX = 0x00000003;
	static final int PIPE_WAIT = 0x00000000;
	static
	{
		System.loadLibrary("pipe");
	}

	public static final native int CreateNamedPipe(String pipeName,
			int ppenMode, int pipeMode, int maxInstances,
			int outBufferSize, int inBufferSize, int defaultTimeOut,
			int securityAttributes);

	public static final native boolean ConnectNamedPipe(int namedPipeHandle, 
			int overlapped);
	public static final native int GetLastError();
	public static final native boolean CloseHandle(int bbject);
	public static final native byte[] ReadFile(int file, int numberOfBytesToRead);
	public static final native int WriteFile(int file, byte[] buffer, 
			int numberOfBytesToWrite);
	public static final native boolean FlushFileBuffers(int file);
	public static final native boolean DisconnectNamedPipe(int namedPipeHandle);
	public static final native int CreateFile(String fileName,
			int desiredAccess, int shareMode, int securityAttributes,
			int creationDisposition, int flagsAndAttributes,
			int templateFile);

	public static final native boolean WaitNamedPipe(String namedPipeName, int timeOut);
	public static final native String FormatMessage(int errorCode);
	public static final native void Print(String message);
}
