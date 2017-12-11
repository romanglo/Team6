package configurations;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logs.LogManager;
import utilities.XmlUtilities;


/**
 *
 * ClientConfiguration: POJO to client application configuration.
 *
 */
@XmlRootElement(name = "configuration")
public class ClientConfiguration {
	
	/* Constants region */
	
	/**
	 * The path of the configuration XML file.
	 */
	public final static String CONFIGURATION_PATH = "configuration.xml";
	
	/* End of --> Constants region */
	
	/* Singleton region */
	
	private static ClientConfiguration s_instance;

	/**
	 * If it is the first call of this method, it will try read the configuration
	 * from the XML file (if the reading fails, it will be created with default configuration).
	 * Otherwise the method will return an existing configuration.
	 *
	 * @return The configuration of the application.
	 */
	public static ClientConfiguration getInstance() {
		/* Check if this is the first call */
		if (s_instance == null) { 
			synchronized (LogManager.class) {
				/* Make sure that this is the first call */
				if (s_instance == null) {
					try {
						InputStream inputStream = ClientConfiguration.class.getResourceAsStream(CONFIGURATION_PATH);
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						s_instance = XmlUtilities.parseXmlToObject(bufferedReader, ClientConfiguration.class);

					} catch (Exception ex) {
						/*
						 * if the Server configuration failed for some reason, 
						 * default configuration will be created.
						 */
						s_instance = new ClientConfiguration();
						s_instance.m_isDefaultConfiguration = true;
					}
				}
			}
		}
		return s_instance;
	}

	/* End of --> Singleton region */
	
	/* Fields region */
	
	private boolean m_isDefaultConfiguration = false;
	
	@XmlElement(name = "ip")
	private String m_ip;

	@XmlElement(name = "port")
	private String m_port;

	/**
	 * @return the application port.
	 */
	public String getPort() {
		return m_port.trim();
	}
	
	/**
	 * @return the application IP.
	 */
	public String getIp() {
		return m_ip.trim();
	}
	
	/**
	 * Identifies whether or not the configuration is default.
	 * 
	 * @return <code>true</code> if the loaded default configuration,
	 *         <code>false</code> if does not.
	 */
	public boolean isDefaultConfiguration() {
		return m_isDefaultConfiguration;
	}
	
	/* End of --> Fields region */
	
	/* Override region */
	
	@Override
	public String toString() {
		return "ClientConfiguration [ip=" + m_ip + ", port=" + m_port + "]";
	}
	
	/* End of --> Override region */
}
