package base.loader;

import java.io.IOException;
import java.util.Properties;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.parameters.ConstantParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.LoaderException;
import base.sender.UdpSender;
import base.server.forwarder.TcpServerChannelForwarder;
import base.server.forwarder.TcpServerSocketForwarder;
import base.server.forwarder.UdpServerForwarder;

public class AbstractLoader<E> {
	protected static final String PROPERTIES_FILE = "loader.properties";
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
	public static <E> E getLoader() throws LoaderException {
    	return (E) new AbstractLoader(readProperties(PROPERTIES_FILE));
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

	protected void addSender(Properties properties) throws LoaderException {
		Class<?> senderClass = null;
		switch (properties.getProperty("protocol")) {
			case "tcp":
				switch (properties.getOrDefault("tcp.implementation", "socket").toString()) {
					case "channel":
						senderClass = base.server.channel.TcpClient.class; 
						break;
					default:
					case "socket":
						senderClass = base.server.socket.TcpClient.class; 
				}
				break;
			case "udp":
				senderClass = UdpSender.class;
				break;
		}
		if (senderClass == null) {
			throw new LoaderException("Failed to determine <Sender>");
		}
		pico.addComponent(senderClass, senderClass, new Parameter[]{
			new ConstantParameter(properties.getProperty("remote.host")),
			new ConstantParameter(Integer.valueOf(properties.getProperty("remote.port")))});
	}

    protected void addForwarder(Properties properties) throws LoaderException {
		Class<?> forwarderClass = null;
		switch (properties.getProperty("server.protocol", "tcp")) {
				case "tcp":
					switch (properties.getOrDefault("tcp.implementation", "socket").toString()) {
					case "channel":
						forwarderClass = TcpServerChannelForwarder.class;
						break;
					default:
					case "socket":
						forwarderClass = TcpServerSocketForwarder.class;
				}
				break;
			case "udp":
				forwarderClass = UdpServerForwarder.class;
		}
		if (forwarderClass == null) {
			throw new LoaderException("Failed to determine <Forwarder>");
		}
		pico.addComponent(forwarderClass, forwarderClass, new Parameter[]{
			new ConstantParameter(Integer.valueOf(properties.getProperty("server.port")))});		
	}
}
