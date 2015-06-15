package base.loader;

import java.io.IOException;
import java.util.Properties;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLoader {
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

	public static AbstractLoader getLoader() throws IOException {
    	return new AbstractLoader(readProperties(PROPERTIES_FILE));
    }

	public static Properties readProperties(String propertiesFile) throws IOException {
		/* Read properties file */
		Properties properties = new Properties();
		properties.load(AbstractLoader.class.getClassLoader().getResourceAsStream(propertiesFile));
		return properties;
	}
}
