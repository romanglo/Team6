package entities;

import java.io.Serializable;

/**
*
* CostumerServiceEmployee: A POJO to database 'costumer_service_employees' table.
* 
* @see IEntity
*/
public class CostumerServiceEmployee extends User
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -6360427450521124338L;


	private int m_id;


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
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Costumer [id=" + m_id + ", " + super.toString() + "]";
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
		if (!(obj instanceof CostumerServiceEmployee)) {
			return false;
		}
		CostumerServiceEmployee other = (CostumerServiceEmployee) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}
}
