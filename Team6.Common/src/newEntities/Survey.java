
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * Survey: A POJO to database 'surveys' table.
 * 
 */
public class Survey implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -2059679189113267694L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private int m_id;

	private int m_managerId;

	private java.util.Date m_startDate;

	private java.util.Date m_endDate;

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
	 * @return the managerId
	 */
	public int getManagerId()
	{
		return m_managerId;
	}

	/**
	 * @param managerId
	 *            the managerId to set
	 */
	public void setManagerId(int managerId)
	{
		m_managerId = managerId;
	}

	/**
	 * @return the startDate
	 */
	public java.util.Date getStartDate()
	{
		return m_startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(java.util.Date startDate)
	{
		m_startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public java.util.Date getEndDate()
	{
		return m_endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(java.util.Date endDate)
	{
		m_endDate = endDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Survey [id=" + m_id + ", managerId=" + m_managerId + ", startDate=" + s_dateForamt.format(m_startDate)
				+ ", endDate=" + s_dateForamt.format(m_endDate) + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Survey)) {
			return false;
		}
		Survey other = (Survey) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}
}
