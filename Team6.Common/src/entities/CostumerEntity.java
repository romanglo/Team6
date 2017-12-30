
package entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * CostumerEntity: Describes an entity of a costumer.
 * 
 */
public class CostumerEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -1575231046663959076L;

	private Integer m_id;

	private String m_creditCard;

	private CostumerSubscription m_subscription;

	private Boolean m_approved;

	private ArrayList<ItemEntity> m_reservationList;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the costumer.
	 */
	public Integer getId()
	{
		return m_id;
	}

	/**
	 * @return The credit card of the costumer.
	 */
	public String getCreditCard()
	{
		return m_creditCard;
	}

	/**
	 * @return A enumerator which describes the costumer subscription.
	 */
	public CostumerSubscription getSubscription()
	{
		return m_subscription;
	}

	/**
	 * @return A boolean which describes the costumer approval.
	 */
	public Boolean getCostumerApproved()
	{
		return m_approved;
	}

	/**
	 * @return An image of the costumer.
	 */
	public ArrayList<ItemEntity> getcostumerImage()
	{
		return m_reservationList;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Upgrade the ID of the costumer.
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public void setId(Integer m_id)
	{
		this.m_id = m_id;
	}

	/**
	 * Upgrade the credit card of the costumer.
	 *
	 * @param m_creditCard
	 *            the m_creditCard to set
	 */
	public void setCreditCard(String m_creditCard)
	{
		this.m_creditCard = m_creditCard;
	}

	/**
	 * Upgrade the {@link CostumerSubscription} of the costumer.
	 *
	 * @param m_subscription
	 *            the m_subscription to set
	 */
	public void setSubscription(CostumerSubscription m_subscription)
	{
		this.m_subscription = m_subscription;
	}

	/**
	 * Upgrade the approval status of the costumer.
	 *
	 * @param m_approved
	 *            the m_approved to set
	 */
	public void setApproved(Boolean m_approved)
	{
		this.m_approved = m_approved;
	}

	/**
	 * Upgrade the {@link ItemEntity} of the costumer.
	 *
	 * @param m_reservationList
	 *            the m_reservationList to set
	 */
	public void setReservationList(ArrayList<ItemEntity> m_reservationList)
	{
		this.m_reservationList = m_reservationList;
	}

	/**
	 * Add the {@link ItemEntity} to the reservation list of the costumer.
	 *
	 * @param m_reservation
	 *            the m_reservation to add.
	 */
	public void addReservation(ItemEntity m_reservation)
	{
		this.m_reservationList.add(m_reservation);
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param creditCard
	 *            The credit card number of the costumer.
	 * @param subscription
	 *            An enumerator which describes the costumer subscription.
	 * @param approved
	 *            A boolean which describes the costumer approval.
	 * @param reservationList
	 *            The reservation list.
	 */
	public CostumerEntity(Integer id, String creditCard, CostumerSubscription subscription, Boolean approved,
			ArrayList<ItemEntity> reservationList)
	{
		m_id = id;
		m_creditCard = creditCard;
		m_subscription = subscription;
		m_approved = approved;
		m_reservationList = reservationList;
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param subscription
	 *            An enumerator which describes the costumer subscription.
	 */
	public CostumerEntity(Integer id, CostumerSubscription subscription)
	{
		this(id, null, subscription, null, new ArrayList<>());
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param creditCard
	 *            The credit card number of the costumer.
	 */
	public CostumerEntity(Integer id, String creditCard)
	{
		this(id, creditCard, CostumerSubscription.None, null, new ArrayList<>());
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param approved
	 *            The approval of the costumer.
	 */
	public CostumerEntity(Integer id, Boolean approved)
	{
		this(id, null, CostumerSubscription.None, approved, null);
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param list
	 *            The reservation list of the costumer.
	 */
	public CostumerEntity(Integer id, ArrayList<ItemEntity> list)
	{
		this(id, null, CostumerSubscription.None, null, list);
	}

	/**
	 * Create instance of {@link CostumerEntity} only with id. Dedicated for get or
	 * remove messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 */
	public CostumerEntity(Integer id)
	{
		this(id, null, CostumerSubscription.None, null, new ArrayList<>());
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 *
	 * @param id
	 *            An unique ID of the costumer.
	 * @param creditCard
	 *            The credit card number of the costumer.
	 * @param reservationList
	 *            The reservation list of the costumer.
	 */
	public CostumerEntity(Integer id, String creditCard, ArrayList<ItemEntity> reservationList)
	{
		this(id, creditCard, CostumerSubscription.None, null, reservationList);
	}

	/**
	 * Create instance of {@link CostumerEntity}. Dedicated for update messages.
	 *
	 * @param id
	 *            An unique ID of the costumer.
	 * @param subscription
	 *            The subscription of the costumer.
	 * @param reservationList
	 *            The reservation list of the costumer.
	 */
	public CostumerEntity(Integer id, CostumerSubscription subscription, ArrayList<ItemEntity> reservationList)
	{
		this(id, null, subscription, null, reservationList);
	}

	// end region -> Constructors

	// region Override Object Methods

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
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CostumerEntity other = (CostumerEntity) obj;
		if (m_id != other.m_id) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "CostumerEntity [ID=" + m_id + ", credit card=" + m_creditCard + ", costumer subscription="
				+ m_subscription + ", costumer approval=" + m_approved + ", reservation list=" + m_reservationList
				+ "]";
	}

	// end region -> Override Object Methods
}
