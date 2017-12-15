package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
	 * The configuration type name.
	 */

	public final static String CONFIGURATION_TYPE_NAME = "Database";

	/**
	 * The name of 'IP' property.
	 */
	public final static String PROPERTY_NAME_IP = "IP";

	/**
	 * The name of 'Schema' property.
	 */
	public final static String PROPERTY_NAME_SCHEMA = "Schema";
	/**
	 * The name of 'Username' property.
	 */
	public final static String PROPERTY_NAME_USERNAME = "Username";
	/**
	 * The name of 'Password' property.
	 */
	public final static String PROPERTY_NAME_PASSWORD = "Password";

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

	@XmlTransient
	private String m_ip;

	@XmlTransient
	private String m_schema;

	@XmlTransient
	private String m_username;

	@XmlTransient
	private String m_password;

	// end region -> Fields

	// region Constructors

	/**
	 * Package internal constructor which set default values to all fields.
	 */
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
	@XmlElement(name = "password")
	public String getPassword() {
		return m_password;
	}

	/**
	 * @return the database user name.
	 */
	@XmlElement(name = "username")
	public String getUsername() {
		return m_username;
	}

	/**
	 * @return the database schema name.
	 */
	@XmlElement(name = "schema")
	public String getSchema() {
		return m_schema;
	}

	/**
	 * @return the database IP.
	 */
	@XmlElement(name = "ip")
	public String getIp() {
		return m_ip;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param ip
	 *            Database IP.
	 */
	public void setIp(String ip) {
		m_ip = ip;
	}

	/**
	 * @param schema
	 *            Database schema name.
	 */
	public void setSchema(String schema) {
		m_schema = schema;
	}

	/**
	 * @param username
	 *            Database user name.
	 */
	public void setUsername(String username) {
		m_username = username;
	}

	/**
	 * @param password
	 *            Database password.
	 */
	public void setPassword(String password) {
		m_password = password;
	}

	// end region -> Getters

	// region Public Methods

	/**
	 * 
	 * The method update an property by his name.
	 *
	 * @param propertyName
	 *            Property name.
	 * @param value
	 *            the new value;
	 * @return true if the update succeed and false if does not.
	 */
	public boolean updateValueByName(String propertyName, String value) {
		if (propertyName == null || propertyName.isEmpty() || value == null || value.isEmpty()) {
			return false;
		}
		boolean succeed = false;
		try {
			switch (propertyName) {
			case PROPERTY_NAME_IP:
				m_ip = value;
				break;
			case PROPERTY_NAME_SCHEMA:
				m_schema = value;
				break;
			case PROPERTY_NAME_USERNAME:
				m_username = value;
				break;
			case PROPERTY_NAME_PASSWORD:
				m_password = value;
				break;
			default:
				break;
			}
		} catch (Exception ignored) {

		}
		return succeed;
	}

	// end region -> Public Methods

	// region Object Methods Overrides

	@Override
	public String toString() {
		return "DbConfiguration [ip=" + m_ip + ", schema=" + m_schema + ", username=" + m_username + ", password="
				+ m_password + "]";
	}

	// end region -> Object Methods Overrides
}