
package newEntities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * ShopCostumer: A POJO to database 'costumers_in_shops' table.
 * 
 */
public class ShopCostumer implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -196650145665452647L;

	private static SimpleDateFormat s_simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private int m_costumerId;

	private int m_shopManagerId;

	private EntitiesEnums.CostumerSubscription m_costumerSubscription;

	private Date m_subscriptionStartDate;

	private String m_creditCard;

	private float m_cumulativePrice;

	/**
	 * @return the costumerId
	 */
	public int getCostumerId()
	{
		return m_costumerId;
	}

	/**
	 * @param costumerId
	 *            the costumerId to set
	 */
	public void setCostumerId(int costumerId)
	{
		m_costumerId = costumerId;
	}

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
	 * @return the costumerSubscription
	 */
	public EntitiesEnums.CostumerSubscription getCostumerSubscription()
	{
		return m_costumerSubscription;
	}

	/**
	 * @param costumerSubscription
	 *            the costumerSubscription to set
	 */
	public void setCostumerSubscription(EntitiesEnums.CostumerSubscription costumerSubscription)
	{
		m_costumerSubscription = costumerSubscription;
	}

	/**
	 * @return the subscriptionStartDate
	 */
	public Date getSubscriptionStartDate()
	{
		return m_subscriptionStartDate;
	}

	/**
	 * @param subscriptionStartDate
	 *            the subscriptionStartDate to set
	 */
	public void setSubscriptionStartDate(Date subscriptionStartDate)
	{
		m_subscriptionStartDate = subscriptionStartDate;
	}

	/**
	 * @return the creditCard
	 */
	public String getCreditCard()
	{
		return m_creditCard;
	}

	/**
	 * @param creditCard
	 *            the creditCard to set
	 */
	public void setCreditCard(String creditCard)
	{
		m_creditCard = creditCard;
	}

	/**
	 * @return the cumulative price
	 */
	public float getCumulativePrice()
	{
		return m_cumulativePrice;
	}

	/**
	 * @param cumulativePrice
	 *            the cumulative price to set
	 */
	public void setCumulativePrice(float cumulativePrice)
	{
		m_cumulativePrice = cumulativePrice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_costumerId;
		result = prime * result + ((m_costumerSubscription == null) ? 0 : m_costumerSubscription.hashCode());
		result = prime * result + ((m_creditCard == null) ? 0 : m_creditCard.hashCode());
		result = prime * result + Float.floatToIntBits(m_cumulativePrice);
		result = prime * result + m_shopManagerId;
		result = prime * result + ((m_subscriptionStartDate == null) ? 0 : m_subscriptionStartDate.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ShopCostumer other = (ShopCostumer) obj;
		if (m_costumerId != other.m_costumerId) return false;
		if (m_costumerSubscription != other.m_costumerSubscription) return false;
		if (m_creditCard == null) {
			if (other.m_creditCard != null) return false;
		} else if (!m_creditCard.equals(other.m_creditCard)) return false;
		if (Float.floatToIntBits(m_cumulativePrice) != Float.floatToIntBits(other.m_cumulativePrice)) return false;
		if (m_shopManagerId != other.m_shopManagerId) return false;
		if (m_subscriptionStartDate == null) {
			if (other.m_subscriptionStartDate != null) return false;
		} else if (!m_subscriptionStartDate.equals(other.m_subscriptionStartDate)) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopCostumer [CostumerId=" + m_costumerId + ", ShopManagerId=" + m_shopManagerId
				+ ", CostumerSubscription=" + m_costumerSubscription + ", SubscriptionStartDate="
				+ m_subscriptionStartDate + ", CreditCard=" + m_creditCard + ", CumulativePrice=" + m_cumulativePrice
				+ "]";
	}
}
