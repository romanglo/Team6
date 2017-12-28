package entities;

import java.io.Serializable;

/**
 *
 * ComplaintEntity:
 * Describes an entity of Complaint.
 * 
 */
public class ComplaintEntity implements IEntity
{
	// region Fields
	/**
	 * Serial version unique ID, necessary due to the class implements {@link Serializable
	 */
	@SuppressWarnings("javadoc") private static final long serialVersionUID = 1L;
	
	private int costumer_id;
	
	private String costumer_complaint;

	// end region -> Fields

	// region Getters
	
	/**
	 * @return An unique ID of the product.
	 */
	public int getId()
	{
		return costumer_id;
	}

	/**
	 * @return The name of the product.
	 */
	public String getComlaint()
	{
		return costumer_complaint;
	}

	// end region -> Getters

	// region Setters
	
	/**
	 * Set costumer id
	 *
	 * @param costumer_id
	 *            the costumer_id to set
	 */
	public void setId(int costumer_id)
	{
		this.costumer_id = costumer_id;
	}

	/**
	 * set costumer complaint
	 *
	 * @param costumer_complaint
	 *            the costumer_complaint to set
	 */
	public void setComplaint(String costumer_complaint)
	{
		this.costumer_complaint = costumer_complaint;
	}
	

	// end region -> Setters

	// region Constructors
	/**
	 * Complaint Entity Constructor
	 *
	 * @param costumer_id
	 * 			the costumer id to set
	 * @param costumer_complaint
	 * 			the costumer complaint to set
	 */
	public ComplaintEntity(int costumer_id, String costumer_complaint)
	{
		this.costumer_id=costumer_id;
		this.costumer_complaint=costumer_complaint;
	}
	
	// end region -> Constructors

	// region Public Methods
	
	

	
	@Override
	public String toString()
	{
		return "ProductEntity [Costumer ID=" + costumer_id + ", Costumer Complaint=" + costumer_complaint + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + costumer_id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ComplaintEntity)) return false;
		ComplaintEntity other = (ComplaintEntity) obj;
		if (costumer_complaint == null) {
			if (other.costumer_complaint != null) return false;
		} else if (!costumer_complaint.equals(other.costumer_complaint)) return false;
		if (costumer_id != other.costumer_id) return false;
		return true;
	}
	
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
