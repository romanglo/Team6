
package configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import utilities.XmlUtilities;

/**
 *
 * ClientConfiguration: POJO to client application configuration.
 *
 */
@XmlRootElement(name = "configuration") public class ClientConfiguration
{

	/* Constants region */

	/**
	 * The name of 'IP' property.
	 */
	public static final String PROPERTY_NAME_IP = "IP";

	/**
	 * The name of 'Port' property.
	 */
	public static final String PROPERTY_NAME_PORT = "Port";

	/**
	 * Default connection IP.
	 */
	public final static String DEFAULT_IP = "localhost";

	/**
	 * Default connection port.
	 */
	public final static int DEFAULT_PORT = 1234;

	/**
	 * The path of the configuration XML resource, relative to current class path.
	 */
	public final static String CONFIGURATION_PATH = "client-configuration.xml";

	/**
	 * A possible path of external configuration XML file.
	 */
	private final static File file = new File(
			ClientConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getPath() + '\\'
					+ CONFIGURATION_PATH);
	// end region -> Constants

	// region Singleton Pattern

	private static ClientConfiguration s_instance;

	/**
	 * If it is the first call to the method, the method will try read configuration
	 * from XML file (if the reading failed, will be created default configuration),
	 * try first to load XML file from execution folder and then from resources.
	 * Else the method will return an existing configuration.
	 *
	 * @return The application configuration.
	 */
	public static ClientConfiguration getInstance()
	{
		if (s_instance == null) { // Single Checked
			synchronized (ClientConfiguration.class) {
				if (s_instance == null) { // Double checked
					try {
						InputStream inputStream;
						if (file.exists()) {
							inputStream = new FileInputStream(file);
						} else {
							inputStream = ClientConfiguration.class.getResourceAsStream(CONFIGURATION_PATH);
						}
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						s_instance = XmlUtilities.parseXmlToObject(bufferedReader, ClientConfiguration.class);

					}
					catch (Exception ex) {
						/*
						 * if the Server configuration failed due to some reason, default configuration
						 * will created.
						 */
						s_instance = new ClientConfiguration();
						s_instance.m_defaultConfiguration = true;
					}
				}
			}
		}
		return s_instance;
	}

	// end region -> Singleton Pattern

	/* Fields region */

	private boolean m_defaultConfiguration = false;

	@XmlTransient private String m_ip;

	@XmlTransient private int m_port;

	/* End of --> Fields region */

	/* region Constructors */

	private ClientConfiguration()
	{
		m_ip = DEFAULT_IP;
		m_port = DEFAULT_PORT;
	}

	/* End of --> Constructors */

	/* region Getters */

	/**
	 * @return the connectivity port.
	 */
	@XmlElement(name = "port")
	public int getPort()
	{
		return m_port;
	}

	/**
	 * @return the connectivity IP.
	 */
	@XmlElement(name = "ip")
	public String getIp()
	{
		return m_ip;
	}

	/**
	 * Identifies whether or not the configuration is default.
	 * 
	 * @return <code>true</code> if the loaded default configuration,
	 *         <code>false</code> if does not.
	 */
	public boolean isDefaultConfiguration()
	{
		return m_defaultConfiguration;
	}
	/* End of --> Getters */

	/* region Setters */

	/**
	 * @param ip
	 *            the connectivity IP.
	 */
	public void setIp(String ip)
	{
		m_ip = ip.trim();
	}

	/**
	 * @param port
	 *            the connectivity port.
	 */
	public void setPort(int port)
	{
		m_port = port;
	}

	/* End of --> Setters */

	/* region Public Methods */

	/**
	 * If exist XML configuration file in execution folder the Method will update
	 * it, if does not will create one.
	 *
	 * @return <code>true</code> if the updating succeed and <code>false</code> if
	 *         updating failed.
	 */
	public boolean updateResourceFile()
	{
		boolean result = true;
		try {
			XmlUtilities.parseObjectToXml(file, this);
		}
		catch (Exception ex) {
			result = false;
		}

		return result;
	}
	/* end region -> Public Methods */

	/* Override region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ClientConfiguration [ip=" + m_ip + ", port=" + m_port + "]";
	}

	/* End of --> Override region */
}
