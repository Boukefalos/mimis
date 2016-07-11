package base.server.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import base.Sender;
import base.work.Listen;

public class TcpServerClient extends Listen<byte[]> implements Sender {
    protected static final int BUFFER_SIZE = 1024;

    protected TcpServer server;
    protected SocketChannel socketChannel;
    protected int bufferSize;
    protected ByteBuffer byteBuffer;

    public TcpServerClient(TcpServer server, SocketChannel socketChannel) {
        this(server, socketChannel, BUFFER_SIZE);
    }

    public TcpServerClient(TcpServer server, SocketChannel socketChannel, Integer bufferSize) {
        super();
        this.server = server;
        this.socketChannel = socketChannel;
        this.bufferSize = bufferSize;
        byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    public void write(ByteBuffer byteBuffer) throws IOException {
         socketChannel.write(byteBuffer);
    }

    public void readable() throws IOException {
        int read;        
        while (( read = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            byte[] buffer = byteBuffer.array();
            input(buffer);
            byteBuffer.clear();
        }
        if (read < 0) {
            socketChannel.close();
        }
    }

    public void input(byte[] buffer) {
        server.input(this, buffer);        
    }

    public void send(byte[] buffer) throws IOException {
        write(ByteBuffer.wrap(buffer));        
    }
}
