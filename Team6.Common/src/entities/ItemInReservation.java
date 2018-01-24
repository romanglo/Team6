
package entities;

import java.io.Serializable;

/**
 *
 * ItemInReservation: A POJO to database 'items_in_reservations' table.
 * 
 */
public class ItemInReservation implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 3088838831735633106L;

	private int m_reservationId;

	private int m_itemId;

	private String m_itemName;

	private int m_quantity;

	private float m_price;

	/**
	 * @return the reservationId
	 */
	public int getReservationId()
	{
		return m_reservationId;
	}

	/**
	 * @param reservationId
	 *            the reservationId to set
	 */
	public void setReservationId(int reservationId)
	{
		m_reservationId = reservationId;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId()
	{
		return m_itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(int itemId)
	{
		m_itemId = itemId;
	}

	/**
	 * @return the item name
	 */
	public String getItemName()
	{
		return m_itemName;
	}

	/**
	 * @param name
	 *            the item name to set
	 */
	public void setItemName(String name)
	{
		m_itemName = name;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity()
	{
		return m_quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity)
	{
		m_quantity = quantity;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ItemInReservation [reservationId=" + m_reservationId + ", itemId=" + m_itemId + ", name=" + m_itemName
				+ ", quantity=" + m_quantity + ", price=" + m_price + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_itemId;
		result = prime * result + m_reservationId;
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
		if (!(obj instanceof ItemInReservation)) {
			return false;
		}
		ItemInReservation other = (ItemInReservation) obj;
		if (m_itemId != other.m_itemId) {
			return false;
		}
		if (m_reservationId != other.m_reservationId) {
			return false;
		}
		return true;
	}

}
