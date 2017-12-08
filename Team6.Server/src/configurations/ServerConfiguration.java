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
 *
 * ServerConfiguration: POJO to server application configuration. Also implement
 * singleton pattern, so created only by
 * {@link ServerConfiguration#getInstance()} method.
 *
 */
@XmlRootElement(name = "configuration")
public class ServerConfiguration {

	// region Constants

	/**
	 * The path of the configuration XML.
	 */
	public final static String CONFIGURATION_PATH = "configuration.xml";

	// end region -> Constants

	// region Singleton Pattern

	private static ServerConfiguration s_instance;

	/**
	 * If it is the first call to the method, the method will try read configuration
	 * from XML file (if the reading failed, will be created default configuration).
	 * Else the method will return an existing configuration.
	 *
	 * @return The application configuration.
	 */
	public static ServerConfiguration getInstance() {
		if (s_instance == null) { // Single Checked
			synchronized (LogManager.class) {
				if (s_instance == null) { // Double checked
					try {
						InputStream inputStream = ServerConfiguration.class.getResourceAsStream(CONFIGURATION_PATH);
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						s_instance = XmlUtilities.parseXmlToObject(bufferedReader, ServerConfiguration.class);

					} catch (Exception ex) {
						/*
						 * if the Server configuration failed due to some reason, default configuration
						 * will created.
						 */
						s_instance = new ServerConfiguration();
						s_instance.m_defaultConfiguration=true;
					}
				}
			}
		}
		return s_instance;
	}

	// end region -> Singleton Pattern

	// region Fields

	@XmlElement(name = "connectivity")
	private ConnectivityConfiguration m_connectivityConfiguration;

	@XmlElement(name = "db")
	private DbConfiguration m_dbConfiguration;

	private boolean m_defaultConfiguration=false;

	// end region -> Fields

	// region -> Constructors

	private ServerConfiguration() {
		m_connectivityConfiguration = new ConnectivityConfiguration();
		m_dbConfiguration = new DbConfiguration();
	}

	// end region -> Constructors

	// region Getters
	/**
	 * @return the application database configuration.
	 */
	public DbConfiguration getDbConfiguration() {
		return m_dbConfiguration;
	}

	/**
	 * @return the application connectivity configuration.
	 */
	public ConnectivityConfiguration getConnectivityConfiguration() {
		return m_connectivityConfiguration;
	}

	/**
	 * @return <code>true</code> if the loaded default configuration,
	 *         <code>false</code> if does not.
	 */
	public boolean isDefaultConfiguration() {
		return m_defaultConfiguration;
	}

	// end region -> Getters

	// region Object Methods Overrides

	@Override
	public String toString() {
		return "ServerConfiguration [" + m_connectivityConfiguration + ", " + m_dbConfiguration + "]";
	}

	// end region -> Object Methods Overrides

}
