
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
	 * @return The name of the product.
	 */
	public String getComlaint()
	{
		return m_complaint;
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
	public ComplaintEntity(int id, String complaint)
	{
		m_id = id;
		m_complaint = complaint;
	}

	// end region -> Constructors

	// region Public Methods

	@Override
	public String toString()
	{
		return "ComplaintEntity [ID=" + m_id + ", Complaint=" + m_complaint + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		if (m_id != other.m_id) return false;
		return true;
	}

	// end region -> Public Methods
}
