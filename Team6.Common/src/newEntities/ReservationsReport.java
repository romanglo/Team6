
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * ReservationsReport: A POJO to database 'reservations_reports' table.
 * 
 */
public class ReservationsReport implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -1468949329954991176L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("yyyy");

	private int m_shopManagerId;

	private java.util.Date m_year;

	private int m_quarter;

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
		return "ReservationsReport [shopManagerId=" + m_shopManagerId + ", year=" + s_dateForamt.format(m_year)
				+ ", quarter=" + m_quarter + ", numberOfOrderedFlowersInFirstMonth="
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
		if (!(obj instanceof ReservationsReport)) {
			return false;
		}
		ReservationsReport other = (ReservationsReport) obj;
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
