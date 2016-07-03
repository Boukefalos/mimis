package base.server.channel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import base.Sender;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.server.channel.TcpServerClient;
import base.work.Work;

public class TcpServer extends Work implements Sender {
    protected static final Class<?> CLIENT_CLASS = TcpServerClient.class;
    protected static final int BUFFER_SIZE = 1024;

    protected int port;
    protected int bufferSize;
    protected Constructor<?> clientConstructor;
    protected Selector selector;
    protected ServerSocketChannel serverSocket;
    protected ArrayList<TcpServerClient> clientList;

    public TcpServer(int port) {
        this(port, CLIENT_CLASS);
    }

    public TcpServer(int port, Class<?> clientClass) {
        this(port, clientClass, BUFFER_SIZE);
    }

    public TcpServer(int port, Class<?> clientClass, int bufferSize) {
        this.port = port;
        this.bufferSize = bufferSize;
        try {
            // Allow dependency injection, constructor arguments
            clientConstructor = Class.forName(clientClass.getName()).getConstructor(TcpServer.class, SocketChannel.class, Integer.class);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            logger.error("Failed to initialise client constructor", e);
        }
        clientList = new ArrayList<TcpServerClient>();
    }

    public void activate() throws ActivateException {
        System.out.println("Server: Activate!");    
        try {
            // Get selector
            selector = Selector.open();

            // Get server socket channel and register with selector
            serverSocket = ServerSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress(port);
            serverSocket.bind(hostAddress);
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            synchronized (clientConstructor) {
                clientConstructor.notifyAll();
            }
            return;
        } catch (BindException e) {
            logger.error("Address already in use", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        throw new ActivateException();
    }

    public void deactivate() throws DeactivateException {
        System.out.println("Server: Deactivate!");
        try {
            selector.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new DeactivateException();
        } finally {
            for (TcpServerClient client : clientList) {
                client.stop();
            }
        }
    }

    public void stop() {
        super.stop();
        if (selector != null) {
            selector.wakeup();
        }
    }

    public void work() {
        try {
            System.out.println("Server: Waiting for select... ");    
            System.out.println("Server: Number of selected keys: " + selector.select());
    
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
    
            while (selectionKeyIterator.hasNext()) {    
                SelectionKey selectionKey = selectionKeyIterator.next();    
                if (selectionKey.isAcceptable()) {    
                    // Accept the new client connection
                    SocketChannel socketChannel = serverSocket.accept();
                    socketChannel.configureBlocking(false);

                    // Add the new connection to the selector
                    TcpServerClient client = (TcpServerClient) clientConstructor.newInstance(this, socketChannel, bufferSize);
                    clientList.add(client);
                    socketChannel.register(selector, SelectionKey.OP_READ, client);
                    //initClient(client);
                    System.out.println("Accepted new connection from client: " + socketChannel);
                } else if (selectionKey.isReadable()) {    
                    // Read the data from client
                    TcpServerClient serverClient = (TcpServerClient) selectionKey.attachment();
                    serverClient.readable();    
                } else if (selectionKey.isWritable()) {
                    // Write to client?
                }
                selectionKeyIterator.remove();
            }
        }/* catch (IOException e) {} catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        } catch (InvocationTargetException e) {
            logger.error("", e);
        } */catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initClient(TcpServerClient client) {
        try {
            client.write(ByteBuffer.wrap(new String("Hi there!").getBytes()));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public void send(byte[] buffer) throws IOException {
        logger.debug("Number of clients = " + clientList.size());
        for (TcpServerClient client : clientList) {
            // Should be dealt with in clients own thread
            client.send(buffer);
        }        
    }

    public void input(TcpServerClient client, byte[] buffer) {}
}