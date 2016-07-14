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
package pipe;

import com.github.boukefalos.jlibloader.Native;

/**
 * @author Vikram S Khatri vikram.khatri@us.ibm.com
 */
public class Pipe {
    static final int ERROR_PIPE_CONNECTED = 535;
    static final int ERROR_BROKEN_PIPE = 109;
    static final int PIPE_ACCESS_DUPLEX = 0x00000003;
    static final int PIPE_WAIT = 0x00000000;
    
    static {
        Native.load("com.github.boukefalos", "jlibpipe");
    }

    public static final native int CreateNamedPipe(
            String pipeName,
            int ppenMode,
            int pipeMode,
            int maxInstances,
            int outBufferSize,
            int inBufferSize,
            int defaultTimeOut,
            int securityAttributes);

    public static final native boolean ConnectNamedPipe(int namedPipeHandle, int overlapped);

    public static final native int GetLastError();

    public static final native boolean CloseHandle(int bbject);

    public static final native byte[] ReadFile(int file, int numberOfBytesToRead);

    public static final native int WriteFile(int file, byte[] buffer, int numberOfBytesToWrite);

    public static final native boolean FlushFileBuffers(int file);

    public static final native boolean DisconnectNamedPipe(int namedPipeHandle);

    public static final native int CreateFile(
            String fileName,
            int desiredAccess,
            int shareMode,
            int securityAttributes,
            int creationDisposition,
            int flagsAndAttributes,
            int templateFile);

    public static final native boolean WaitNamedPipe(String namedPipeName, int timeOut);

    public static final native String FormatMessage(int errorCode);

    public static final native void Print(String message);
}
