
package newEntities;

import java.io.Serializable;

/**
 *
 * Costumer: A POJO to database 'costumers' table.
 * 
 */
public class Costumer extends User
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -9072452554963769141L;

	private int m_id;

	private String m_creditCard;

	private EntitiesEnums.CostumerSubscription m_costumerSubscription;

	private float m_balance;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		m_id = id;
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
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @return the balance
	 */
	public float getBalance()
	{
		return m_balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(float balance)
	{
		m_balance = balance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Costumer [id=" + m_id + ", creditCard=" + m_creditCard + ", costumerSubscription="
				+ m_costumerSubscription + ", balance=" + m_balance + ", user=" + super.toString() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + m_id;
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Costumer)) {
			return false;
		}
		Costumer other = (Costumer) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
