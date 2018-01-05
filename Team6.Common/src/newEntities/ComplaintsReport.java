
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * ComplaintsReport: A POJO to database 'complaints_reports' table.
 * 
 */
public class ComplaintsReport implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -4299624624423416550L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("yyyy");

	private int m_shopManagerId;

	private java.util.Date m_year;

	private int m_quarter;

	private int m_numberOfComplaintsFirstMonth;

	private int m_numberOfComplaintsSecondMonth;

	private int m_numberOfComplaintsThirdMonth;

	/**
	 * @return the shopManagerId
	 */
	public int getShopManagerId()
	{
		return m_shopManagerId;
	}

	/**
	 * @param shopManagerId
	 *            the shopManagerId to set
	 */
	public void setShopManagerId(int shopManagerId)
	{
		m_shopManagerId = shopManagerId;
	}

	/**
	 * @return the year
	 */
	public java.util.Date getYear()
	{
		return m_year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(java.util.Date year)
	{
		m_year = year;
	}

	/**
	 * @return the quarter
	 */
	public int getQuarter()
	{
		return m_quarter;
	}

	/**
	 * @param quarter
	 *            the quarter to set
	 */
	public void setQuarter(int quarter)
	{
		m_quarter = quarter;
	}

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
		return "ComplaintsReport [shopManagerId=" + m_shopManagerId + ", year=" + s_dateForamt.format(m_year)
				+ ", quarter=" + m_quarter + ", numberOfComplaintsFirstMonth=" + m_numberOfComplaintsFirstMonth
				+ ", numberOfComplaintsSecondMonth=" + m_numberOfComplaintsSecondMonth
				+ ", numberOfComplaintsThirdMonth=" + m_numberOfComplaintsThirdMonth + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_quarter;
		result = prime * result + m_shopManagerId;
		result = prime * result + ((m_year == null) ? 0 : s_dateForamt.format(m_year).hashCode());
		return result;
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
		if (m_quarter != other.m_quarter) {
			return false;
		}
		if (m_shopManagerId != other.m_shopManagerId) {
			return false;
		}
		if (m_year == null) {
			if (other.m_year != null) {
				return false;
			}
		} else if (!s_dateForamt.format(m_year).equals(s_dateForamt.format(other.m_year))) {
			return false;
		}
		return true;
	}

}
