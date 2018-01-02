
package entities;

import java.io.Serializable;

/**
 *
 * ComplaintEntity: Describes an entity of Complaint.
 * 
 */
public class ComplaintEntity implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -9060640992825870520L;

	// region Fields
	
	private int m_id;

	private String m_complaint;
	
	private String m_summary;
	
	private CostumerEntity m_costumer;
	
	private boolean m_active;
	
	

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the product.
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @return The costumer complaint.
	 */
	public String getComlaint()
	{
		return m_complaint;
	}
	
	/**
	 * @return The costumer service employee summary.
	 */
	public String getSumerry()
	{
		return m_summary;
	}
	
	/**
	 * @return The costumer entity
	 */
	public CostumerEntity getCostumer()
	{
		return m_costumer;
	}
	
	/**
	 * @return The Complaint status.
	 */
	public boolean getActive()
	{
		return m_active;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Set costumer unique ID
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		m_id = id;
	}

	/**
	 * set costumer complaint
	 *
	 * @param complaint
	 *            the complaint to set
	 */
	public void setComplaint(String complaint)
	{
		m_complaint = complaint;
	}
	
	/**
	 * set costumer complaint
	 *
	 * @param summary
	 *            The summary to set
	 */
	public void setSumerry(String summary)
	{
		m_summary = summary;
	}
	
	/**
	 * set costumer entity
	 *
	 * @param m_costumer
	 * 				The costumer entity to set.
	 */
	public void setCostumer(CostumerEntity m_costumer)
	{
		this.m_costumer=m_costumer;
	}
	
	/**
	 *  set complaint status
	 *
	 * @param m_active
	 * 				The complaint status to set.
	 */
	public void setActive( boolean m_active)
	{
		this.m_active=m_active;
	}

	// end region -> Setters

	// region Constructors
	/**
	 * Complaint Entity Constructor
	 *
	 * @param id
	 *            the costumer id to set
	 * @param complaint
	 *            the costumer complaint to set
	 */
	public ComplaintEntity(int id, String complaint, CostumerEntity costumer )
	{
		m_id = id;
		m_complaint = complaint;
		m_costumer=costumer;
		m_active=true;
	}
	
	/**
	 * Complaint Entity Empty Constructor 
	 * 						 to get all message. 
	 *
	 */
	public ComplaintEntity()
	{
		
	}
	// end region -> Constructors

	// region Public Methods

	@Override
	public String toString()
	{
		if(m_summary==null)
		return "ComplaintEntity [ID=" + m_id + ", Complaint=" + m_complaint + ", Summary= In process." + "]";
		else
			return "ComplaintEntity [ID=" + m_id + ", Complaint=" + m_complaint + ", Summary="+ m_summary + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_complaint == null) ? 0 : m_complaint.hashCode());
		result = prime * result + m_id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ComplaintEntity)) return false;
		ComplaintEntity other = (ComplaintEntity) obj;
		if (m_complaint == null) {
			if (other.m_complaint != null) return false;
		} else if (!m_complaint.equals(other.m_complaint)) return false;
		if (m_id != other.m_id) return false;
		if (m_summary == null) {
			if (other.m_summary != null) return false;
		} else if (!m_summary.equals(other.m_summary)) return false;
		return true;
	}


	// end region -> Public Methods
}
