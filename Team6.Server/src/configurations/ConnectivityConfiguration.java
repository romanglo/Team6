package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	 * Default connection port.
	 */
	public final static int DEFAULT_PORT = 1234;

	// end region -> Constants

	// region Fields

	@XmlElement(name = "port")
	private int m_port;

	// end region -> Fields

	// region Getters

	/**
	 * @return the application port.
	 */
	public int getPort() {
		return m_port;
	}

	// end region -> Getters

	// region Object Methods Overrides

	@Override
	public String toString() {
		return "ServerConfiguration [port=" + m_port + "]";
	}

	// end region -> Object Methods Overrides
}
