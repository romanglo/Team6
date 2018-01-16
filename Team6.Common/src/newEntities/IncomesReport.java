
package newEntities;

import java.io.Serializable;

/**
 *
 * IncomesReport: A POJO to database 'incomes_reports' table.
 * 
 * @see Report
 * @see IEntity
 * @see Serializable
 */
public class IncomesReport extends Report
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4085405329432872045L;

	private float m_incomesInFirstMonth;

	private float m_incomesInSecondMonth;

	private float m_incomesInThirdMonth;

	/**
	 * @return the incomesInFirstMonth
	 */
	public float getIncomesInFirstMonth()
	{
		return m_incomesInFirstMonth;
	}

	/**
	 * @param incomesInFirstMonth
	 *            the incomesInFirstMonth to set
	 */
	public void setIncomesInFirstMonth(float incomesInFirstMonth)
	{
		m_incomesInFirstMonth = incomesInFirstMonth;
	}

	/**
	 * @return the incomesInSecondMonth
	 */
	public float getIncomesInSecondMonth()
	{
		return m_incomesInSecondMonth;
	}

	/**
	 * @param incomesInSecondMonth
	 *            the incomesInSecondMonth to set
	 */
	public void setIncomesInSecondMonth(float incomesInSecondMonth)
	{
		m_incomesInSecondMonth = incomesInSecondMonth;
	}

	/**
	 * @return the incomesInThirdMonth
	 */
	public float getIncomesInThirdMonth()
	{
		return m_incomesInThirdMonth;
	}

	/**
	 * @param incomesInThirdMonth
	 *            the incomesInThirdMonth to set
	 */
	public void setIncomesInThirdMonth(float incomesInThirdMonth)
	{
		m_incomesInThirdMonth = incomesInThirdMonth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "IncomesReport [" + super.toString() + ", incomesInFirstMonth=" + m_incomesInFirstMonth
				+ ", incomesInSecondMonth=" + m_incomesInSecondMonth + ", incomesInThirdMonth=" + m_incomesInThirdMonth
				+ "]";
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
		if (!(obj instanceof IncomesReport)) {
			return false;
		}
		IncomesReport other = (IncomesReport) obj;

		return super.equals(other);
	}

}
