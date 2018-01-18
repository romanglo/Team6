package configurations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 
 *
 * ServerConfiguration: POJO to configuration of operations base on time.
 *
 */
@XmlRootElement(name = "time")
public class TimeConfiguration {

	// region Fields

	@XmlTransient
	private String m_lastSubsctiptionCheckDate;

	@XmlTransient
	private String m_lastGenerationDate;

	// end region -> Fields

	// region Getters

	/**
	 * @return A string which describes the last date of subscription check. The
	 *         string in format 'dd-mm-yyyy'.
	 */
	@XmlElement(name = "LastSubscriptionCheckDate")
	public String getLastSubsctiptionCheckDate() {
		return m_lastSubsctiptionCheckDate;
	}

	/**
	 * @return A string which describes the last date of reports generation check.
	 *         The string in format 'dd-mm-yyyy'.
	 */
	@XmlElement(name = "LastGenerationDate")
	public String getLastGenerationDate() {
		return m_lastGenerationDate;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param lastSubsctiptionCheckDate
	 *            the lastSubsctiptionCheckDate to set
	 */
	public void setLastSubsctiptionCheckDate(String lastSubsctiptionCheckDate) {
		m_lastSubsctiptionCheckDate = lastSubsctiptionCheckDate;
	}

	/**
	 * @param lastGenerationDate
	 *            the lastGenerationDate to set
	 */
	public void setLastGenerationDate(String lastGenerationDate) {
		m_lastGenerationDate = lastGenerationDate;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Package internal constructor just for ensure that instance of this class
	 * would be created only by classes in this package.
	 */
	TimeConfiguration() {

	}

	// end region -> Constructors

	// region Object Methods Overrides

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "TimeConfiguration [LastSubsctiptionCheckDate=" + m_lastSubsctiptionCheckDate + ", LastGenerationDate="
				+ m_lastGenerationDate + "]";
	}

	// end region -> Object Methods Overrides
}
