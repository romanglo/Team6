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

	@XmlElement(name = "ip")
	private String m_ip;

	@XmlElement(name = "port")
	private String m_port;

	/**
	 * @return the application port.
	 */
	public String getPort() {
		return m_port;
	}
	
	/**
	 * @return the application IP.
	 */
	public String getIp() {
		return m_ip;
	}

	@Override
	public String toString() {
		return "ServerConfiguration [ip=" + m_ip + ", port=" + m_port + "]";
	}
}
