
package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * Complaint: A POJO to database 'complaints' table.
 * 
 */
public class Complaint implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 5529440915726546110L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private int m_id;

	private int m_costumerId;

	private int m_costumerServiceEmployeeId;

	private int m_shopManagerId;

	private java.util.Date m_creationDate;

	private String m_complaint;

	private String m_summary;

	private boolean m_opened;

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
		this.m_id = id;
	}

	/**
	 * @return the costumerServiceEmployeeId
	 */
	public int getCostumerServiceEmployeeId()
	{
		return m_costumerServiceEmployeeId;
	}

	/**
	 * @param costumerServiceEmployeeId
	 *            the costumerServiceEmployeeId to set
	 */
	public void setCostumerServiceEmployeeId(int costumerServiceEmployeeId)
	{
		m_costumerServiceEmployeeId = costumerServiceEmployeeId;
	}

	/**
	 * @return the opened
	 */
	public boolean isOpened()
	{
		return m_opened;
	}

	/**
	 * @param opened
	 *            the opened to set
	 */
	public void setOpened(boolean opened)
	{
		m_opened = opened;
	}

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
	 * @return the creationDate
	 */
	public java.util.Date getCreationDate()
	{
		return m_creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(java.util.Date creationDate)
	{
		m_creationDate = creationDate;
	}

	/**
	 * @return the complaint
	 */
	public String getComplaint()
	{
		return m_complaint;
	}

	/**
	 * @param complaint
	 *            the complaint to set
	 */
	public void setComplaint(String complaint)
	{
		m_complaint = complaint;
	}

	/**
	 * @return the summary
	 */
	public String getSummary()
	{
		return m_summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary)
	{
		m_summary = summary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Complaint [id=" + m_id + ", costumerId=" + m_costumerId + ", shopManagerId=" + m_shopManagerId
				+ ", costumerServiceEmployeeId=" + m_costumerServiceEmployeeId  +", creationDate="
				+ s_dateForamt.format(m_creationDate) + ", complaint=" + m_complaint + ", summary=" + m_summary
				+ ", opened=" + m_opened + "]";
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
		if (!(obj instanceof Complaint)) {
			return false;
		}
		Complaint other = (Complaint) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}
}
