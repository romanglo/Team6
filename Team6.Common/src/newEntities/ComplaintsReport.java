
package newEntities;

import java.io.Serializable;

/**
 *
 * ComplaintsReport: A POJO to database 'complaints_reports' table.
 * 
 * @see Report
 * @see IEntity
 * @see Serializable
 */
public class ComplaintsReport extends Report
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -4299624624423416550L;

	private int m_numberOfComplaintsFirstMonth;

	private int m_numberOfComplaintsSecondMonth;

	private int m_numberOfComplaintsThirdMonth;

	/**
	 * @return the numberOfComplaintsFirstMonth
	 */
	public int getNumberOfComplaintsFirstMonth()
	{
		return m_numberOfComplaintsFirstMonth;
	}

	/**
	 * @param numberOfComplaintsFirstMonth
	 *            the numberOfComplaintsFirstMonth to set
	 */
	public void setNumberOfComplaintsFirstMonth(int numberOfComplaintsFirstMonth)
	{
		m_numberOfComplaintsFirstMonth = numberOfComplaintsFirstMonth;
	}

	/**
	 * @return the numberOfComplaintsSecondMonth
	 */
	public int getNumberOfComplaintsSecondMonth()
	{
		return m_numberOfComplaintsSecondMonth;
	}

	/**
	 * @param numberOfComplaintsSecondMonth
	 *            the numberOfComplaintsSecondMonth to set
	 */
	public void setNumberOfComplaintsSecondMonth(int numberOfComplaintsSecondMonth)
	{
		m_numberOfComplaintsSecondMonth = numberOfComplaintsSecondMonth;
	}

	/**
	 * @return the numberOfComplaintsThirdMonth
	 */
	public int getNumberOfComplaintsThirdMonth()
	{
		return m_numberOfComplaintsThirdMonth;
	}

	/**
	 * @param numberOfComplaintsThirdMonth
	 *            the numberOfComplaintsThirdMonth to set
	 */
	public void setNumberOfComplaintsThirdMonth(int numberOfComplaintsThirdMonth)
	{
		m_numberOfComplaintsThirdMonth = numberOfComplaintsThirdMonth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ComplaintsReport [" + super.toString() + ", numberOfComplaintsFirstMonth="
				+ m_numberOfComplaintsFirstMonth + ", numberOfComplaintsSecondMonth=" + m_numberOfComplaintsSecondMonth
				+ ", numberOfComplaintsThirdMonth=" + m_numberOfComplaintsThirdMonth + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ComplaintsReport)) {
			return false;
		}
		ComplaintsReport other = (ComplaintsReport) obj;

		return super.equals(other);
	}
}
