
package entities;

import java.io.Serializable;

/**
 *
 * Costumer: A POJO to database 'costumers' table.
 * 
 * @see IEntity
 */
public class Costumer extends User
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -9072452554963769141L;

	private int m_id;

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
	 * Empty constructor.
	 */
	public Costumer()
	{
		
	}
	
	/**
	 * Copy constructor.
	 *
	 * @param costumer
	 *            The {@link Costumer} to set in the new instance.
	 */
	public Costumer(Costumer costumer)
	{
		m_id = costumer.getId();
		m_balance = costumer.getBalance();
		setEmail(costumer.getEmail());
		setImage(costumer.getImage());
		setPassword(costumer.getPassword());
		setPrivilege(costumer.getPrivilege());
		setStatus(costumer.getStatus());
		setUserName(costumer.getUserName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Costumer [id=" + m_id + ", balance=" + m_balance + ", " + super.toString() + "]";
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
