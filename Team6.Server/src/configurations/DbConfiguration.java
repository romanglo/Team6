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

	@XmlElement(name = "ip")
	private String m_ip;

	@XmlElement(name = "schema")
	private String m_schema;

	@XmlElement(name = "username")
	private String m_username;
	
	@XmlElement(name = "password")
	private String m_password;
	
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

	@Override
	public String toString() {
		return "DbConfiguration [ip=" + m_ip + ", schema=" + m_schema + ", username=" + m_username
				+ ", password=" + m_password + "]";
	}
}