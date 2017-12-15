package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 
 *
 * ServerConfiguration: POJO to connectivity configuration.
 *
 */
@XmlRootElement(name = "connectivity")
public class ConnectivityConfiguration {

	// region Constants

	/**
	 * The configuration type name.
	 */

	public final static String CONFIGURATION_TYPE_NAME = "Connectivity";

	/**
	 * The name of 'Port' property.
	 */
	public final static String PROPERTY_NAME_PORT = "Port";

	/**
	 * Default listening port.
	 */
	public final static int DEFAULT_PORT = 1234;

	// end region -> Constants

	// region Fields

	@XmlTransient
	private int m_port;

	// end region -> Fields

	// region Getters

	/**
	 * @return The Server listening port.
	 */
	@XmlElement(name = "port")
	public int getPort() {
		return m_port;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param port
	 *            Server listening port.
	 */
	public void setPort(int port) {
		m_port = port;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Package internal constructor which set default values to all fields.
	 */
	ConnectivityConfiguration() {
		m_port = DEFAULT_PORT;
	}

	// end region -> Constructors

	// region Object Methods Overrides

	@Override
	public String toString() {
		return "ServerConfiguration [port=" + m_port + "]";
	}

	// end region -> Object Methods Overrides
}
