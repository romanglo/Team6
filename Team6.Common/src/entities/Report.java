
package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * Report: An abstract class to all the report entities.
 * 
 */
public abstract class Report implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 7891502407187183477L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("yyyy");

	private int m_shopManagerId;

	private java.util.Date m_year;

	private int m_quarter;

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
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Report [shopManagerId=" + m_shopManagerId + ", year=" + s_dateForamt.format(m_year) + "]";
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
		result = prime * result + ((m_year == null) ? 0 : m_year.hashCode());
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
		if (!(obj instanceof Report)) {
			return false;
		}
		Report other = (Report) obj;
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
		} else if (!m_year.equals(other.m_year)) {
			return false;
		}
		return true;
	}
}
