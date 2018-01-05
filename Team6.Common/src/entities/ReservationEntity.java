
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

	private CostumerEntity m_costumer;

	private ShopManagerEntity m_shopManager;

	private ReservationType m_reservationType;

	private int m_quantityOfItems;

	private Double m_totalPrice;

	private String m_blessingCard;

	private Date m_deliveryDate;

	private ReservationDeliveryType m_deliveryType;

	private String m_deliveryAddress;

	private String m_deliveryPhone;

	private String m_deliveryName;

	// end region -> Fields

	// region Getters

	// end region -> Getters

	// region Setters

	// end region -> Setters

	// region Constructors

	/**
	 * @return The ID.
	 */
	public Integer getId()
	{
		return m_id;
	}

	/**
	 * @return The costumer of the reservation.
	 */
	public CostumerEntity getCostumer()
	{
		return m_costumer;
	}
	
	/**
	 * @return The costumer ID.
	 */
	public int getCostumerId() 
	{
		return m_costumer.getId();
	}

	/**
	 * @return The shop manager connected with the reservation.
	 */
	public ShopManagerEntity getShopManager()
	{
		return m_shopManager;
	}

	/**
	 * @return The reservation type.
	 */
	public ReservationType getType()
	{
		return m_reservationType;
	}

	/**
	 * @return The quantity of items in the reservation.
	 */
	public int getQuantityOfItems()
	{
		return m_quantityOfItems;
	}

	/**
	 * @return The total price of the reservation.
	 */
	public Double getTotalPrice()
	{
		return m_totalPrice;
	}

	/**
	 * @return The blessing card.
	 */
	public String getBlessingCard()
	{
		return m_blessingCard;
	}

	/**
	 * @return The delivery date.
	 */
	public Date getDeliveryDate()
	{
		return m_deliveryDate;
	}

	/**
	 * @return The delivery type.
	 */
	public ReservationDeliveryType getDeliveryType()
	{
		return m_deliveryType;
	}

	/**
	 * @return The delivery address.
	 */
	public String getDeliveryAddress()
	{
		return m_deliveryAddress;
	}

	/**
	 * @return The delivery phone number.
	 */
	public String getDeliveryPhone()
	{
		return m_deliveryPhone;
	}

	/**
	 * @return The delivery name.
	 */
	public String getDeliveryName()
	{
		return m_deliveryName;
	}

	/**
	 * Update the id of the reservation.
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public void setId(Integer m_id)
	{
		this.m_id = m_id;
	}

	/**
	 * Update the costumer of the reservation.
	 *
	 * @param m_costumer
	 *            the m_costumer to set
	 */
	public void setCostumer(CostumerEntity m_costumer)
	{
		this.m_costumer = m_costumer;
	}

	/**
	 * Update the shop manager of the reservation.
	 *
	 * @param m_shopManager
	 *            the m_shopManager to set
	 */
	public void setShopManager(ShopManagerEntity m_shopManager)
	{
		this.m_shopManager = m_shopManager;
	}

	/**
	 * Update the type of the reservation.
	 *
	 * @param m_reservationType
	 *            the m_reservationType to set
	 */
	public void setType(ReservationType m_reservationType)
	{
		this.m_reservationType = m_reservationType;
	}

	/**
	 * Update the quantity of the reservation.
	 *
	 * @param m_quantityOfItems
	 *            the m_quantityOfItems to set
	 */
	public void setQuantityOfItems(int m_quantityOfItems)
	{
		this.m_quantityOfItems = m_quantityOfItems;
	}

	/**
	 * Update the price of the reservation.
	 *
	 * @param m_totalPrice
	 *            the m_totalPrice to set
	 */
	public void setTotalPrice(Double m_totalPrice)
	{
		this.m_totalPrice = m_totalPrice;
	}

	/**
	 * Update the blessing card of the reservation.
	 *
	 * @param m_blessingCard
	 *            the m_blessingCard to set
	 */
	public void setBlessingCard(String m_blessingCard)
	{
		this.m_blessingCard = m_blessingCard;
	}

	/**
	 * Update the delivery date of the reservation.
	 *
	 * @param m_deliveryDate
	 *            the m_deliveryDate to set
	 */
	public void setDeliveryDate(Date m_deliveryDate)
	{
		this.m_deliveryDate = m_deliveryDate;
	}

	/**
	 * Update the delivery type of the reservation.
	 *
	 * @param m_deliveryType
	 *            the m_deliveryType to set
	 */
	public void setDeliveryType(ReservationDeliveryType m_deliveryType)
	{
		this.m_deliveryType = m_deliveryType;
	}

	/**
	 * Update the delivery address of the reservation.
	 *
	 * @param m_deliveryAddress
	 *            the m_deliveryAddress to set
	 */
	public void setDeliveryAddress(String m_deliveryAddress)
	{
		this.m_deliveryAddress = m_deliveryAddress;
	}

	/**
	 * Update the delivery phone of the reservation.
	 *
	 * @param m_deliveryPhone
	 *            the m_deliveryPhone to set
	 */
	public void setDeliveryPhone(String m_deliveryPhone)
	{
		this.m_deliveryPhone = m_deliveryPhone;
	}

	/**
	 * Update the delivery name of the reservation.
	 *
	 * @param m_deliveryName
	 *            the m_deliveryName to set
	 */
	public void setDeliveryName(String m_deliveryName)
	{
		this.m_deliveryName = m_deliveryName;
	}

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
	 * @param total
	 *            The total price of the reservation.
	 * @param deliveryDate
	 *            The delivery date.
	 * @param deliveryType
	 *            The delivery type.
	 * @param blessingCard
	 *            The blessing card in the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType type, CostumerEntity costumerEntity, Double total,
			Date deliveryDate, ReservationDeliveryType deliveryType, String blessingCard)
	{
		m_id = id;
		m_reservationType = type;
		m_costumer = costumerEntity;
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
	public ReservationEntity(Integer id, ReservationType type, int costumerId, Double total, Date deliveryDate,
			ReservationDeliveryType deliveryType, String blessingCard)
	{
		m_id = id;
		m_reservationType = type;
		m_costumer = new CostumerEntity(costumerId);
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
	 * @param total
	 *            The total price of the reservation.
	 * @param deliveryDate
	 *            The delivery date.
	 * @param deliveryType
	 *            The delivery type.
	 */
	public ReservationEntity(Integer id, ReservationType type, Integer costumerId, Double total, Date deliveryDate,
			ReservationDeliveryType deliveryType)
	{
		this(id, type, new CostumerEntity(costumerId), total, deliveryDate, deliveryType, "");

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
		this(id, type, null, total, deliveryDate, deliveryType, blessingCard);

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
	 * @param total
	 *            The total price of the reservation.
	 */
	public ReservationEntity(Integer id, ReservationType type, Integer costumerId, Double total)
	{
		this(id, type, costumerId, total, null, null);
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
		this(id, reservationType, null, null);
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
		this(id, reservationType, null, total);
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
		this(null, type, costumerId, 0.0);
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
		this(null, type, costumer.getId(), 0.0);
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
		return "ReservationEntity [ID=" + m_id + ", Costumer=" + m_costumer + ", Shop Manager=" + m_shopManager
				+ ", Reservation Type=" + m_reservationType + ", Quantity Of Items=" + m_quantityOfItems
				+ ", Total Price=" + m_totalPrice + ", Blessing Card=" + m_blessingCard + ", Delivery Date="
				+ m_deliveryDate + ", Delivery Type=" + m_deliveryType + ", Delivery Address=" + m_deliveryAddress
				+ ", Delivery Phone=" + m_deliveryPhone + ", Delivery Name=" + m_deliveryName + "]";
	}

	// end region -> Override Object Methods
}
