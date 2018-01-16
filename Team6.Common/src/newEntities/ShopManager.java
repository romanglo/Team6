
package newEntities;

import java.io.Serializable;

/**
 *
 * ShopManager: A POJO to database 'shop_managers' table.
 * 
 */
public class ShopManager extends User
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 6222005341436360612L;

	private int m_id;

	private String m_name;

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
	 * @return the name
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopManager [id=" + m_id + ", name=" + m_name + ", " + super.toString() + "]";
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
		if (!(obj instanceof ShopManager)) {
			return false;
		}
		ShopManager other = (ShopManager) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
