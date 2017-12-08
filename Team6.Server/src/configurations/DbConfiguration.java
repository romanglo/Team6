package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 *
 * ServerConfiguration: POJO to database configuration.
 *
 */
@XmlRootElement(name = "db")
public class DbConfiguration {

	// region Constants

	/**
	 * Default database IP.
	 */
	public final static String DEFAULT_IP = "localhost";
	
	/**
	 * Default database schema name.
	 */
	public final static String DEFAULT_SCHEMA = "zer-li";

	/**
	 * Default database user name.
	 */
	public final static String DEFAULT_USERNAME = "root";
	
	/**
	 * Default database password.
	 */
	public final static String DEFAULT_PASSWORD = "123456";

	// end region -> Constants

	// region Fields

	@XmlElement(name = "ip")
	private String m_ip;

	@XmlElement(name = "schema")
	private String m_schema;

	@XmlElement(name = "username")
	private String m_username;

	@XmlElement(name = "password")
	private String m_password;

	// end region -> Fields

	// region Constructors

	DbConfiguration() {
		m_ip = DEFAULT_IP;
		m_schema = DEFAULT_SCHEMA;
		m_username = DEFAULT_USERNAME;
		m_password = DEFAULT_PASSWORD;
	}

	// end region -> Constructors

	// region Getters

	/**
	 * @return the database user password.
	 */
	public String getPassword() {
		return m_password;
	}

	/**
	 * @return the database user name.
	 */
	public String getUsername() {
		return m_username;
	}

	/**
	 * @return the database schema name.
	 */
	public String getSchema() {
		return m_schema;
	}

	/**
	 * @return the database IP.
	 */
	public String getIp() {
		return m_ip;
	}

	// end region -> Getters

	// region Object Methods Overrides

	@Override
	public String toString() {
		return "DbConfiguration [ip=" + m_ip + ", schema=" + m_schema + ", username=" + m_username + ", password="
				+ m_password + "]";
	}

	// end region -> Object Methods Overrides
}