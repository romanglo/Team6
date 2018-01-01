
package entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * ReservationEntity: Describes an entity of a reservation.
 * 
 */
public class ReservationEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 895842686810231080L;

	private Integer m_id;

	private CostumerEntity m_costumer;

	private ReservationType m_reservationType;

	private ArrayList<ItemEntity> m_reservationList;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the reservation.
	 */
	public Integer getId()
	{
		return m_id;
	}
	
	/**
	 * @return The costumer entity that is connected to the reservation.
	 */
	public CostumerEntity getCostumer() {
		return m_costumer;
	}

	/**
	 * @return The costumer that the reservation is connected to.
	 */
	public Integer getCostumerId()
	{
		return m_costumer.getId();
	}

	/**
	 * @return A enumerator which describes the reservation type.
	 */
	public ReservationType getType()
	{
		return m_reservationType;
	}

	/**
	 * @return A reservation list.
	 */
	public ArrayList<ItemEntity> getReservationList()
	{
		return m_reservationList;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Upgrade the ID of the reservation.
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public void setId(Integer m_id)
	{
		this.m_id = m_id;
	}
	
	/**
	 * Upgrade the connected costumer to the reservation.
	 *
	 * @param costumer
	 *            the costumer id to set
	 */
	public void setCostumer(CostumerEntity costumer)
	{
		this.m_costumer = costumer;
	}

	/**
	 * Upgrade the id of the connected costumer to the reservation.
	 *
	 * @param costumerId
	 *            the costumer id to set
	 */
	public void setCostumerId(Integer costumerId)
	{
		this.m_costumer.setId(costumerId);
	}

	/**
	 * Upgrade the {@link ReservationType}.
	 *
	 * @param m_reservationType
	 *            the m_reservationType to set
	 */
	public void setSubscription(ReservationType m_reservationType)
	{
		this.m_reservationType = m_reservationType;
	}

	/**
	 * Upgrade the {@link ItemEntity} list of the reservation.
	 *
	 * @param m_reservationList
	 *            the m_reservationList to set
	 */
	public void setReservationList(ArrayList<ItemEntity> m_reservationList)
	{
		this.m_reservationList = m_reservationList;
	}

	/**
	 * Add the {@link ItemEntity} to the list.
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
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * @param costumerId
	 *            The costumer id that is connected to the reservation.
	 * @param reservationList
	 *            The reservation list.
	 */
	public ReservationEntity(Integer id, ReservationType type, Integer costumerId,
			ArrayList<ItemEntity> reservationList)
	{
		m_id = id;
		m_reservationType = type;
		m_costumer = new CostumerEntity(costumerId);
		m_reservationList = reservationList;
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * @param costumerId
	 *            The costumer id that is connected to the reservation.
	 * @param reservationList
	 *            The reservation list.
	 */
	public ReservationEntity(ReservationType type, Integer costumerId, ArrayList<ItemEntity> reservationList)
	{
		this(null, type, costumerId, reservationList);
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * @param costumerId
	 *            The costumer id that is connected to the reservation.
	 */
	public ReservationEntity(ReservationType type, Integer costumerId)
	{
		this(null, type, costumerId, new ArrayList<>());
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
		ReservationEntity other = (ReservationEntity) obj;
		if (m_id != other.m_id) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ReservationEntity [ID=" + m_id + ", reservation type=" + m_reservationType + ", reservation list="
				+ m_reservationList + "]";
	}

	// end region -> Override Object Methods
}
