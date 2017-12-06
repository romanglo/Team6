package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 *
 * ServerConfiguration: POJO to server application configuration.
 *
 */
@XmlRootElement(name = "configuration")
public class ServerConfiguration {

	@XmlElement(name = "connectivity")
	private ConnectivityConfiguration m_connectivityConfiguration;

	@XmlElement(name = "db")
	private DbConfiguration m_dbConfiguration;

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

	@Override
	public String toString() {
		return "ServerConfiguration [" + m_connectivityConfiguration + ", " + m_dbConfiguration + "]";
	}
}
