
package entities;

import java.io.Serializable;

/**
 *
 * ReservationsReport: A POJO to database 'reservations_reports' table.
 * 
 * @see Report
 * @see IEntity
 * @see Serializable
 */
public class ReservationsReport extends Report
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -1468949329954991176L;

	private int m_numberOfOrderedFlowersInFirstMonth;

	private int m_numberOfOrderedFlowerPotsInFirstMonth;

	private int m_numberOfOrderedBridalBouquetsInFirstMonth;

	private int m_numberOfOrderedFlowerArrangementsInFirstMonth;

	private int m_numberOfOrderedFlowersInSecondMonth;

	private int m_numberOfOrderedFlowerPotsInSecondMonth;

	private int m_numberOfOrderedFlowerArrangementsInSecondMonth;

	private int m_numberOfOrderedBridalBouquetsInSecondMonth;

	private int m_numberOfOrderedFlowersInThirdMonth;

	private int m_numberOfOrderedFlowerPotsInThirdMonth;

	private int m_numberOfOrderedFlowerArrangementsInThirdMonth;

	private int m_numberOfOrderedBridalBouquetsInThirdMonth;

	/**
	 * @return the numberOfOrderedFlowersInFirstMonth
	 */
	public int getNumberOfOrderedFlowersInFirstMonth()
	{
		return m_numberOfOrderedFlowersInFirstMonth;
	}

	/**
	 * @param numberOfOrderedFlowersInFirstMonth
	 *            the numberOfOrderedFlowersInFirstMonth to set
	 */
	public void setNumberOfOrderedFlowersInFirstMonth(int numberOfOrderedFlowersInFirstMonth)
	{
		m_numberOfOrderedFlowersInFirstMonth = numberOfOrderedFlowersInFirstMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerPotsInFirstMonth
	 */
	public int getNumberOfOrderedFlowerPotsInFirstMonth()
	{
		return m_numberOfOrderedFlowerPotsInFirstMonth;
	}

	/**
	 * @param numberOfOrderedFlowerPotsInFirstMonth
	 *            the numberOfOrderedFlowerPotsInFirstMonth to set
	 */
	public void setNumberOfOrderedFlowerPotsInFirstMonth(int numberOfOrderedFlowerPotsInFirstMonth)
	{
		m_numberOfOrderedFlowerPotsInFirstMonth = numberOfOrderedFlowerPotsInFirstMonth;
	}

	/**
	 * @return the numberOfOrderedBridalBouquetsInFirstMonth
	 */
	public int getNumberOfOrderedBridalBouquetsInFirstMonth()
	{
		return m_numberOfOrderedBridalBouquetsInFirstMonth;
	}

	/**
	 * @param numberOfOrderedBridalBouquetsInFirstMonth
	 *            the numberOfOrderedBridalBouquetsInFirstMonth to set
	 */
	public void setNumberOfOrderedBridalBouquetsInFirstMonth(int numberOfOrderedBridalBouquetsInFirstMonth)
	{
		m_numberOfOrderedBridalBouquetsInFirstMonth = numberOfOrderedBridalBouquetsInFirstMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerArrangementsInFirstMonth
	 */
	public int getNumberOfOrderedFlowerArrangementsInFirstMonth()
	{
		return m_numberOfOrderedFlowerArrangementsInFirstMonth;
	}

	/**
	 * @param numberOfOrderedFlowerArrangementsInFirstMonth
	 *            the numberOfOrderedFlowerArrangementsInFirstMonth to set
	 */
	public void setNumberOfOrderedFlowerArrangementsInFirstMonth(int numberOfOrderedFlowerArrangementsInFirstMonth)
	{
		m_numberOfOrderedFlowerArrangementsInFirstMonth = numberOfOrderedFlowerArrangementsInFirstMonth;
	}

	/**
	 * @return the numberOfOrderedFlowersInSecondMonth
	 */
	public int getNumberOfOrderedFlowersInSecondMonth()
	{
		return m_numberOfOrderedFlowersInSecondMonth;
	}

	/**
	 * @param numberOfOrderedFlowersInSecondMonth
	 *            the numberOfOrderedFlowersInSecondMonth to set
	 */
	public void setNumberOfOrderedFlowersInSecondMonth(int numberOfOrderedFlowersInSecondMonth)
	{
		m_numberOfOrderedFlowersInSecondMonth = numberOfOrderedFlowersInSecondMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerPotsInSecondMonth
	 */
	public int getNumberOfOrderedFlowerPotsInSecondMonth()
	{
		return m_numberOfOrderedFlowerPotsInSecondMonth;
	}

	/**
	 * @param numberOfOrderedFlowerPotsInSecondMonth
	 *            the numberOfOrderedFlowerPotsInSecondMonth to set
	 */
	public void setNumberOfOrderedFlowerPotsInSecondMonth(int numberOfOrderedFlowerPotsInSecondMonth)
	{
		m_numberOfOrderedFlowerPotsInSecondMonth = numberOfOrderedFlowerPotsInSecondMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerArrangementsInSecondMonth
	 */
	public int getNumberOfOrderedFlowerArrangementsInSecondMonth()
	{
		return m_numberOfOrderedFlowerArrangementsInSecondMonth;
	}

	/**
	 * @param numberOfOrderedFlowerArrangementsInSecondMonth
	 *            the numberOfOrderedFlowerArrangementsInSecondMonth to set
	 */
	public void setNumberOfOrderedFlowerArrangementsInSecondMonth(int numberOfOrderedFlowerArrangementsInSecondMonth)
	{
		m_numberOfOrderedFlowerArrangementsInSecondMonth = numberOfOrderedFlowerArrangementsInSecondMonth;
	}

	/**
	 * @return the numberOfOrderedBridalBouquetsInSecondMonth
	 */
	public int getNumberOfOrderedBridalBouquetsInSecondMonth()
	{
		return m_numberOfOrderedBridalBouquetsInSecondMonth;
	}

	/**
	 * @param numberOfOrderedBridalBouquetsInSecondMonth
	 *            the numberOfOrderedBridalBouquetsInSecondMonth to set
	 */
	public void setNumberOfOrderedBridalBouquetsInSecondMonth(int numberOfOrderedBridalBouquetsInSecondMonth)
	{
		m_numberOfOrderedBridalBouquetsInSecondMonth = numberOfOrderedBridalBouquetsInSecondMonth;
	}

	/**
	 * @return the numberOfOrderedFlowersInThirdMonth
	 */
	public int getNumberOfOrderedFlowersInThirdMonth()
	{
		return m_numberOfOrderedFlowersInThirdMonth;
	}

	/**
	 * @param numberOfOrderedFlowersInThirdMonth
	 *            the numberOfOrderedFlowersInThirdMonth to set
	 */
	public void setNumberOfOrderedFlowersInThirdMonth(int numberOfOrderedFlowersInThirdMonth)
	{
		m_numberOfOrderedFlowersInThirdMonth = numberOfOrderedFlowersInThirdMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerPotsInThirdMonth
	 */
	public int getNumberOfOrderedFlowerPotsInThirdMonth()
	{
		return m_numberOfOrderedFlowerPotsInThirdMonth;
	}

	/**
	 * @param numberOfOrderedFlowerPotsInThirdMonth
	 *            the numberOfOrderedFlowerPotsInThirdMonth to set
	 */
	public void setNumberOfOrderedFlowerPotsInThirdMonth(int numberOfOrderedFlowerPotsInThirdMonth)
	{
		m_numberOfOrderedFlowerPotsInThirdMonth = numberOfOrderedFlowerPotsInThirdMonth;
	}

	/**
	 * @return the numberOfOrderedFlowerArrangementsInThirdMonth
	 */
	public int getNumberOfOrderedFlowerArrangementsInThirdMonth()
	{
		return m_numberOfOrderedFlowerArrangementsInThirdMonth;
	}

	/**
	 * @param numberOfOrderedFlowerArrangementsInThirdMonth
	 *            the numberOfOrderedFlowerArrangementsInThirdMonth to set
	 */
	public void setNumberOfOrderedFlowerArrangementsInThirdMonth(int numberOfOrderedFlowerArrangementsInThirdMonth)
	{
		m_numberOfOrderedFlowerArrangementsInThirdMonth = numberOfOrderedFlowerArrangementsInThirdMonth;
	}

	/**
	 * @return the numberOfOrderedBridalBouquetsInThirdMonth
	 */
	public int getNumberOfOrderedBridalBouquetsInThirdMonth()
	{
		return m_numberOfOrderedBridalBouquetsInThirdMonth;
	}

	/**
	 * @param numberOfOrderedBridalBouquetsInThirdMonth
	 *            the numberOfOrderedBridalBouquetsInThirdMonth to set
	 */
	public void setNumberOfOrderedBridalBouquetsInThirdMonth(int numberOfOrderedBridalBouquetsInThirdMonth)
	{
		m_numberOfOrderedBridalBouquetsInThirdMonth = numberOfOrderedBridalBouquetsInThirdMonth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ReservationsReport [" + super.toString() + ", numberOfOrderedFlowersInFirstMonth="
				+ m_numberOfOrderedFlowersInFirstMonth + ", numberOfOrderedFlowerPotsInFirstMonth="
				+ m_numberOfOrderedFlowerPotsInFirstMonth + ", numberOfOrderedBridalBouquetsInFirstMonth="
				+ m_numberOfOrderedBridalBouquetsInFirstMonth + ", numberOfOrderedFlowerArrangementsInFirstMonth="
				+ m_numberOfOrderedFlowerArrangementsInFirstMonth + ", numberOfOrderedFlowersInSecondMonth="
				+ m_numberOfOrderedFlowersInSecondMonth + ", numberOfOrderedFlowerPotsInSecondMonth="
				+ m_numberOfOrderedFlowerPotsInSecondMonth + ", numberOfOrderedFlowerArrangementsInSecondMonth="
				+ m_numberOfOrderedFlowerArrangementsInSecondMonth + ", numberOfOrderedBridalBouquetsInSecondMonth="
				+ m_numberOfOrderedBridalBouquetsInSecondMonth + ", numberOfOrderedFlowersInThirdMonth="
				+ m_numberOfOrderedFlowersInThirdMonth + ", numberOfOrderedFlowerPotsInThirdMonth="
				+ m_numberOfOrderedFlowerPotsInThirdMonth + ", numberOfOrderedFlowerArrangementsInThirdMonth="
				+ m_numberOfOrderedFlowerArrangementsInThirdMonth + ", numberOfOrderedBridalBouquetsInThirdMonth="
				+ m_numberOfOrderedBridalBouquetsInThirdMonth + "]";
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
		if (!(obj instanceof ReservationsReport)) {
			return false;
		}
		ReservationsReport other = (ReservationsReport) obj;
		return super.equals(other);
	}

}
