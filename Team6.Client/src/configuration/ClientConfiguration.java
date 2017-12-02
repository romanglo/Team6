package configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 
 *
 * ServerConfiguration: POJO to server application.
 *
 */
@XmlRootElement(name = "configuration")
public class ClientConfiguration {

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
		return "ClientConfiguration [ip=" + m_ip + ", port=" + m_port + "]";
	}

	
}
