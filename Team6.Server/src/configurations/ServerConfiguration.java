package configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	 * The path of the configuration XML resource, relative to current class path.
	 */
	public final static String CONFIGURATION_PATH = "configuration.xml";

	/**
	 * A possible path of external configuration XML file.
	 */
	private final static File file = new File(
			ServerConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getPath() + '\\'
					+ CONFIGURATION_PATH);
	// end region -> Constants

	// region Singleton Pattern

	private static ServerConfiguration s_instance;

	/**
	 * If it is the first call to the method, the method will try read configuration
	 * from XML file (if the reading failed, will be created default configuration),
	 * try first to load XML file from execution folder and then from resources.
	 * Else the method will return an existing configuration.
	 *
	 * @return The application configuration.
	 */
	public static ServerConfiguration getInstance() {
		if (s_instance == null) { // Single Checked
			synchronized (ServerConfiguration.class) {
				if (s_instance == null) { // Double checked
					try {
						InputStream inputStream;
						if (file.exists()) {
							inputStream = new FileInputStream(file);
						} else {
							inputStream = ServerConfiguration.class.getResourceAsStream(CONFIGURATION_PATH);
						}
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						s_instance = XmlUtilities.parseXmlToObject(bufferedReader, ServerConfiguration.class);

					} catch (Exception ex) {
						/*
						 * if the Server configuration failed due to some reason, default configuration
						 * will created.
						 */
						s_instance = new ServerConfiguration();
						s_instance.m_defaultConfiguration = true;
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

	private boolean m_defaultConfiguration = false;

	// end region -> Fields

	// region -> Constructors

	/**
	 * Class internal constructor which set default values to all fields.
	 */
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

	// region Public Methods

	/**
	 * If exist XML configuration file in execution folder the Method will update
	 * it, if does not will create one.
	 *
	 * @return <code>true</code> if the updating succeed and <code>false</code> if
	 *         updating failed.
	 */
	public boolean updateResourceFile() {
		boolean result = true;
		try {
			XmlUtilities.parseObjectToXml(file, this);
		} catch (Exception ex) {
			result = false;
		}

		return result;
	}

	// end region -> Public Methods

	// region Object Methods Overrides

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ServerConfiguration [" + m_connectivityConfiguration + ", " + m_dbConfiguration + "]";
	}

	// end region -> Object Methods Overrides

}
