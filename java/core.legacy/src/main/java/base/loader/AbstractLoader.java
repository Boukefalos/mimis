package base.loader;

import java.io.IOException;
import java.util.Properties;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.parameters.ConstantParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Duplex;
import base.Forwarder;
import base.Sender;
import base.exception.LoaderException;
import base.server.datagram.UdpSender;
import base.server.forwarder.UdpDuplexClientForwarder;
import base.server.forwarder.UdpDuplexServerForwarder;

public class AbstractLoader<T> {
    protected static final String PROPERTIES_FILE = "loader.properties";
    protected static final Properties SERVER = null;

    protected Logger logger = LoggerFactory.getLogger(AbstractLoader.class);
    protected MutablePicoContainer pico;
    
    public AbstractLoader() {
        /* Initialise container */
        pico = new DefaultPicoContainer();
    }

    public AbstractLoader(Properties properties) {
        this();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T getLoader() throws LoaderException {
        return (T) new AbstractLoader(readProperties(PROPERTIES_FILE));
    }

    public static Properties readProperties(String propertiesFile) throws LoaderException {
        /* Read properties file */
        Properties properties = new Properties();        
        try {
            properties.load(AbstractLoader.class.getClassLoader().getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            throw new LoaderException("Faield to read properties file: " + PROPERTIES_FILE);
        }        
        return properties;
    }

    protected Class<?> getSenderClass(String protocol, String implementation) throws LoaderException {
        switch (protocol) {
            case "tcp":
                switch (implementation) {
                    case "channel":
                        return base.server.channel.TcpClient.class; 
                    default:
                    case "socket":
                        return base.server.socket.TcpClient.class; 
                    }
                case "udp":
                    return UdpSender.class;
        }
        throw new LoaderException("Failed to determine <Sender>");
    }

    protected Class<?> getClientForwarderClass(String protocol, String implementation) throws LoaderException {
        switch (protocol) {
            case "tcp":
                switch (implementation) {
                    case "channel":
                        return base.server.forwarder.TcpClientChannelForwarder.class;
                    default:
                    case "socket":
                        return base.server.forwarder.TcpClientSocketForwarder.class;
                }
            case "udp":
                return UdpDuplexClientForwarder.class;
        }
        throw new LoaderException("Failed to determine <Forwarder>");
    }

    protected Class<?> getServerForwarderClass(String protocol, String implementation) throws LoaderException {
        switch (protocol) {
            case "tcp":
                switch (implementation) {
                    case "channel":
                        return base.server.forwarder.TcpChannelServerForwarder.class;
                    default:
                    case "socket":
                        return base.server.forwarder.TcpSocketServerForwarder.class;
                }
            case "udp":
                return UdpDuplexServerForwarder.class;
        }
        throw new LoaderException("Failed to determine <Forwarder>");
    }

    protected void addClientSender(String protocol, String implementation, String host, int port) throws LoaderException {
        Class<?> senderClass = getSenderClass(protocol, implementation);
        logger.debug("Adding " + senderClass);
        pico.addComponent(Sender.class, senderClass, new Parameter[]{
            new ConstantParameter(host),
            new ConstantParameter(port)});
    }

    protected void addServerSender(String protocol, String implementation, int port) throws LoaderException {
        Class<?> senderClass = getSenderClass(protocol, implementation);
        logger.debug("Adding " + senderClass);
        pico.addComponent(Sender.class, senderClass, new Parameter[]{
            new ConstantParameter(port)});
    }

    protected void addClientForwarder(String protocol, String implementation, String host, int port) throws LoaderException {
        Class<?> forwarderClass = getClientForwarderClass(protocol, implementation);
        logger.debug("Adding " + forwarderClass);
        pico.addComponent(Forwarder.class, forwarderClass, new Parameter[]{
            new ConstantParameter(host),
            new ConstantParameter(port)});
    }

    protected void addClientDuplex(String protocol, String implementation, String host, int port) throws LoaderException {
        Class<?> duplexClass = getClientForwarderClass(protocol, implementation);
        logger.debug("Adding " + duplexClass);
        pico.addComponent(Duplex.class, duplexClass, new Parameter[]{
            new ConstantParameter(host),
            new ConstantParameter(port)});
        
    }

    protected void addServerForwarder(String protocol, String implementation, int port) throws LoaderException {
        Class<?> forwarderClass = getServerForwarderClass(protocol, implementation);
        logger.debug("Adding " + forwarderClass);
        pico.addComponent(Forwarder.class, forwarderClass, new Parameter[]{
            new ConstantParameter(port)});        
    }

    protected void addServerDuplex(String protocol, String implementation, int port) throws LoaderException {
        Class<?> duplexClass = getServerForwarderClass(protocol, implementation);
        logger.debug("Adding " + duplexClass);
        pico.addComponent(Duplex.class, duplexClass, new Parameter[]{
            new ConstantParameter(port)});        
    }
}
