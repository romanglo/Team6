
package entities;

import java.io.Serializable;

/**
 *
 * ShopEmployee: A POJO to database 'shop_employees' table.
 * 
 * @see IEntity
 */
public class ShopEmployee extends User
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -8848315065956372838L;

	private int m_id;

	private int m_shopManagerId;

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
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopEmployee [id=" + m_id + ", shopManagerId=" + m_shopManagerId + ", " + super.toString() + "]";
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
		if (!(obj instanceof ShopEmployee)) {
			return false;
		}
		ShopEmployee other = (ShopEmployee) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}
	
	

}
