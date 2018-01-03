
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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
	private static final long serialVersionUID = 1L;

	private Integer m_id;

	private Double m_totalPrice;

	private CostumerEntity m_costumer;

	private ReservationType m_reservationType;

	private ArrayList<ItemEntity> m_reservationList;

	private Date m_deliveryDate;

	private ReservationDeliveryType m_deliveryType;

	private String m_blessingCard;

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
	 * @return The total price of the reservation.
	 */
	public Double getTotalPrice()
	{
		return m_totalPrice;
	}

	/**
	 * @return The costumer entity that is connected to the reservation.
	 */
	public CostumerEntity getCostumer()
	{
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

	/**
	 * @return Delivery date requested.
	 */
	public Date getDeliveryDate()
	{
		return m_deliveryDate;
	}

	/**
	 * @return Delivery type.
	 */
	public ReservationDeliveryType getDeliveryType()
	{
		return m_deliveryType;
	}

	/**
	 * @return Blessing card string.
	 */
	public String getBlessingCard()
	{
		return m_blessingCard;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Upgrade the ID of the reservation.
	 *
	 * @param id
	 *            the m_id to set
	 */
	public void setId(Integer id)
	{
		m_id = id;
	}

	/**
	 * Upgrade the total price of the reservation.
	 *
	 * @param totalPrice
	 *            the m_totalPrice to set
	 */
	public void setTotalPrice(Double totalPrice)
	{
		m_totalPrice = totalPrice;
	}

	/**
	 * Upgrade the connected costumer to the reservation.
	 *
	 * @param costumer
	 *            the costumer id to set
	 */
	public void setCostumer(CostumerEntity costumer)
	{
		m_costumer = costumer;
	}

	/**
	 * Upgrade the id of the connected costumer to the reservation.
	 *
	 * @param costumerId
	 *            the costumer id to set
	 */
	public void setCostumerId(Integer costumerId)
	{
		m_costumer.setId(costumerId);
	}

	/**
	 * Upgrade the {@link ReservationType}.
	 *
	 * @param reservationType
	 *            the m_reservationType to set
	 */
	public void setType(ReservationType reservationType)
	{
		m_reservationType = reservationType;
	}

	/**
	 * Upgrade the {@link ItemEntity} list of the reservation.
	 *
	 * @param reservationList
	 *            the m_reservationList to set
	 */
	public void setReservationList(ArrayList<ItemEntity> reservationList)
	{
		m_reservationList = reservationList;
	}

	/**
	 * Add the {@link ItemEntity} to the list.
	 *
	 * @param reservation
	 *            the m_reservation to add.
	 */
	public void addReservation(ItemEntity reservation)
	{
		m_reservationList.add(reservation);
	}

	/**
	 * Upgrade the {@link Date} of the delivery.
	 *
	 * @param deliveryDate
	 *            The date of the delivery.
	 */
	public void setDeliveryDate(Date deliveryDate)
	{
		m_deliveryDate = deliveryDate;
	}

	/**
	 * Upgrade the {@link ReservationDeliveryType} of the delivery.
	 *
	 * @param deliveryType
	 *            The delivery type.
	 */
	public void setDeliveryType(ReservationDeliveryType deliveryType)
	{
		m_deliveryType = deliveryType;
	}

	/**
	 * Upgrade the blessing card of the reservation.
	 *
	 * @param blessing
	 *            The blessing string.
	 */
	public void setBlessingCard(String blessing)
	{
		m_blessingCard = blessing;
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
	 * @param costumerEntity
	 *            The costumer entity that is connected to the reservation.
	 * @param reservationList
	 *            The reservation list.
	 * @param total
	 *            The total price of the reservation.
	 * @param deliveryDate
	 *            The delivery date.
	 * @param deliveryType
	 *            The delivery type.
	 * @param blessingCard
	 *            The blessing card in the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType type, CostumerEntity costumerEntity,
			ArrayList<ItemEntity> reservationList, Double total, Date deliveryDate,
			ReservationDeliveryType deliveryType, String blessingCard)
	{
		m_id = id;
		m_reservationType = type;
		m_costumer = costumerEntity;
		m_reservationList = reservationList;
		m_totalPrice = total;
		m_deliveryDate = deliveryDate;
		m_deliveryType = deliveryType;
		m_blessingCard = blessingCard;
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * 
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * 
	 * @param costumerId
	 *            The costumer id that is connected to the reservation.
	 * 
	 * @param reservationList
	 *            The reservation list.
	 * 
	 * @param total
	 *            The total price of the reservation.
	 * 
	 * @param deliveryDate
	 *            The delivery date.
	 * 
	 * @param deliveryType
	 *            The delivery type.
	 * 
	 * @param blessingCard
	 *            The blessing card in the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType type, int costumerId, ArrayList<ItemEntity> reservationList,
			Double total, Date deliveryDate, ReservationDeliveryType deliveryType, String blessingCard)
	{
		m_id = id;
		m_reservationType = type;
		m_costumer = new CostumerEntity(costumerId);
		m_reservationList = reservationList;
		m_totalPrice = total;
		m_deliveryDate = deliveryDate;
		m_deliveryType = deliveryType;
		m_blessingCard = blessingCard;
	}

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
	 * @param total
	 *            The total price of the reservation.
	 * @param deliveryDate
	 *            The delivery date.
	 * @param deliveryType
	 *            The delivery type.
	 */
	public ReservationEntity(Integer id, ReservationType type, Integer costumerId,
			ArrayList<ItemEntity> reservationList, Double total, Date deliveryDate,
			ReservationDeliveryType deliveryType)
	{
		this(id, type, new CostumerEntity(costumerId), reservationList, total, deliveryDate, deliveryType, "");

	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * @param total
	 *            The total price of the reservation.
	 * @param deliveryDate
	 *            The delivery date.
	 * @param deliveryType
	 *            The delivery type.
	 * @param blessingCard
	 *            The blessing card.
	 */
	public ReservationEntity(Integer id, ReservationType type, Double total, Date deliveryDate,
			ReservationDeliveryType deliveryType, String blessingCard)
	{
		this(id, type, null, new ArrayList<>(), total, deliveryDate, deliveryType, blessingCard);

	}

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
	 * @param total
	 *            The total price of the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType type, Integer costumerId,
			ArrayList<ItemEntity> reservationList, Double total)
	{
		this(id, type, costumerId, reservationList, total, null, null);
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param reservationType
	 *            the m_reservationType to set The total price of the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType reservationType)
	{
		this(id, reservationType, null, new ArrayList<>(), null);
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param id
	 *            An unique ID of the costumer.
	 * @param reservationType
	 *            the m_reservationType to set The total price of the reservation.
	 * @param total
	 *            The total price of the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType reservationType, Double total)
	{
		this(id, reservationType, null, new ArrayList<>(), total);
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
		this(null, type, costumerId, reservationList, 0.0);
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
		this(null, type, costumerId, new ArrayList<>(), 0.0);
	}

	/**
	 * Create instance of {@link ReservationEntity}. Dedicated for add or update
	 * messages.
	 * 
	 * @param type
	 *            An enumerator which describes the reservation type.
	 * @param costumer
	 *            The costumer that is connected to the reservation.
	 */
	public ReservationEntity(ReservationType type, CostumerEntity costumer)
	{
		this(null, type, costumer.getId(), new ArrayList<>(), 0.0);
		setCostumer(costumer);
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
