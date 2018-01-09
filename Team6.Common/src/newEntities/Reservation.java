
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import newEntities.EntitiesEnums.ReservationDeliveryType;
import newEntities.EntitiesEnums.ReservationType;

/**
 *
 * Reservation: A POJO to database 'reservations' table.
 * 
 */
public class Reservation implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 7931281117302239308L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	private int m_id;

	private int m_costumerId;

	private int m_shopManagerId;

	private ReservationType m_type;

	private int m_numberOfItems;

	private float m_price;

	private Date m_deliveryDate;

	private String m_blessingCard;

	private ReservationDeliveryType m_deliveryType;

	private String m_deliveryAddress;

	private String m_deliveryPhone;

	private String m_deliveryName;

	private String m_creditCard;

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
	 * @return the type
	 */
	public ReservationType getType()
	{
		return m_type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ReservationType type)
	{
		m_type = type;
	}

	/**
	 * @return the numberOfItems
	 */
	public int getNumberOfItems()
	{
		return m_numberOfItems;
	}

	/**
	 * @param numberOfItems
	 *            the numberOfItems to set
	 */
	public void setNumberOfItems(int numberOfItems)
	{
		m_numberOfItems = numberOfItems;
	}

	/**
	 * @return the price
	 */
	public float getPrice()
	{
		return m_price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(float price)
	{
		m_price = price;
	}

	/**
	 * @return the blessingCard
	 */
	public String getBlessingCard()
	{
		return m_blessingCard;
	}

	/**
	 * @param blessingCard
	 *            the blessingCard to set
	 */
	public void setBlessingCard(String blessingCard)
	{
		m_blessingCard = blessingCard;
	}

	/**
	 * @return the deliveryType
	 */
	public ReservationDeliveryType getDeliveryType()
	{
		return m_deliveryType;
	}

	/**
	 * @param deliveryType
	 *            the deliveryType to set
	 */
	public void setDeliveryType(ReservationDeliveryType deliveryType)
	{
		m_deliveryType = deliveryType;
	}

	/**
	 * @return the deliveryAddress
	 */
	public String getDeliveryAddress()
	{
		return m_deliveryAddress;
	}

	/**
	 * @param deliveryAddress
	 *            the deliveryAddress to set
	 */
	public void setDeliveryAddress(String deliveryAddress)
	{
		m_deliveryAddress = deliveryAddress;
	}

	/**
	 * @return the deliveryPhone
	 */
	public String getDeliveryPhone()
	{
		return m_deliveryPhone;
	}

	/**
	 * @param deliveryPhone
	 *            the deliveryPhone to set
	 */
	public void setDeliveryPhone(String deliveryPhone)
	{
		m_deliveryPhone = deliveryPhone;
	}

	/**
	 * @return the deliveryName
	 */
	public String getDeliveryName()
	{
		return m_deliveryName;
	}

	/**
	 * @param deliveryName
	 *            the deliveryName to set
	 */
	public void setDeliveryName(String deliveryName)
	{
		m_deliveryName = deliveryName;
	}

	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate()
	{
		return m_deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate)
	{
		m_deliveryDate = deliveryDate;
	}

	/**
	 * @return the creditCard
	 */
	public String getCreditCard()
	{
		return m_creditCard;
	}

	/**
	 * @param creditCard
	 *            the creditCard to set
	 */
	public void setCreditCard(String creditCard)
	{
		m_creditCard = creditCard;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Reservation [id=" + m_id + ", costumerId=" + m_costumerId + ", shopManagerId=" + m_shopManagerId
				+ ", creditCard=" + m_creditCard + ", type=" + m_type + ", numberOfItems=" + m_numberOfItems
				+ ", price=" + m_price + ", deliveryDate=" + s_dateForamt.format(m_deliveryDate) + ", blessingCard="
				+ m_blessingCard + ", deliveryType=" + m_deliveryType + ", deliveryAddress=" + m_deliveryAddress
				+ ", deliveryPhone=" + m_deliveryPhone + ", deliveryName=" + m_deliveryName + "]";
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
		if (!(obj instanceof Reservation)) {
			return false;
		}
		Reservation other = (Reservation) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}
}
