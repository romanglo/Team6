
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * IncomesReport: A POJO to database 'incomes_reports' table.
 * 
 */
public class IncomesReport implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4085405329432872045L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("yyyy");

	private int m_shopManagerId;

	private java.util.Date m_year;

	private int m_quarter;

	private int m_incomesInFirstMonth;

	private int m_incomesInSecondMonth;

	private int m_incomesInThirdMonth;

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
	 * @return the incomesInFirstMonth
	 */
	public int getIncomesInFirstMonth()
	{
		return m_incomesInFirstMonth;
	}

	/**
	 * @param incomesInFirstMonth
	 *            the incomesInFirstMonth to set
	 */
	public void setIncomesInFirstMonth(int incomesInFirstMonth)
	{
		m_incomesInFirstMonth = incomesInFirstMonth;
	}

	/**
	 * @return the incomesInSecondMonth
	 */
	public int getIncomesInSecondMonth()
	{
		return m_incomesInSecondMonth;
	}

	/**
	 * @param incomesInSecondMonth
	 *            the incomesInSecondMonth to set
	 */
	public void setIncomesInSecondMonth(int incomesInSecondMonth)
	{
		m_incomesInSecondMonth = incomesInSecondMonth;
	}

	/**
	 * @return the incomesInThirdMonth
	 */
	public int getIncomesInThirdMonth()
	{
		return m_incomesInThirdMonth;
	}

	/**
	 * @param incomesInThirdMonth
	 *            the incomesInThirdMonth to set
	 */
	public void setIncomesInThirdMonth(int incomesInThirdMonth)
	{
		m_incomesInThirdMonth = incomesInThirdMonth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "IncomesReport [shopManagerId=" + m_shopManagerId + ", year=" + s_dateForamt.format(m_year)
				+ ", quarter=" + m_quarter + ", incomesInFirstMonth=" + m_incomesInFirstMonth
				+ ", incomesInSecondMonth=" + m_incomesInSecondMonth + ", incomesInThirdMonth=" + m_incomesInThirdMonth
				+ "]";
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
		if (!(obj instanceof IncomesReport)) {
			return false;
		}
		IncomesReport other = (IncomesReport) obj;
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
