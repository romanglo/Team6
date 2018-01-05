
package entities;

import java.io.Serializable;

/**
 *
 * ItemInReservationEntity: Class that generates an item in a reservation.
 * 
 */
public class ItemInReservationEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -7914742211769264590L;

	private ReservationEntity m_reservation;

	private ItemEntity m_item;

	private int m_quantity;

	private float m_price;

	// end region -> Fields

	// region Getters

	/**
	 * @return The reservation entity.
	 */
	public ReservationEntity getReservation()
	{
		return m_reservation;
	}

	/**
	 * @return The item entity.
	 */
	public ItemEntity getItem()
	{
		return m_item;
	}

	/**
	 * @return The quantity of the item.
	 */
	public int getQuantity()
	{
		return m_quantity;
	}

	/**
	 * @return The price.
	 */
	public float getPrice()
	{
		return m_price;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Update the reservation of the item.
	 *
	 * @param reservation
	 *            The reservation the item is connected to.
	 */
	public void setResrvation(ReservationEntity reservation)
	{
		m_reservation = reservation;
	}

	/**
	 * Update the item.
	 *
	 * @param item
	 *            The item to set.
	 */
	public void setItem(ItemEntity item)
	{
		m_item = item;
	}

	/**
	 * Update the quantity.
	 *
	 * @param quantity
	 *            The item to set.
	 */
	public void setQuantity(int quantity)
	{
		m_quantity = quantity;
	}

	/**
	 * Update the price.
	 *
	 * @param price
	 *            The item to set.
	 */
	public void setPrice(float price)
	{
		m_price = price;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link ItemInReservationEntity}. Dedicated for add or
	 * update messages.
	 */
	public ItemInReservationEntity()
	{

	}

	// end region -> Constructors

	// region Overridden Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ItemInReservationEntity [Reservation=" + m_reservation + ", Item=" + m_item + ", Quantity=" + m_quantity
				+ ", Price=" + m_price + "]";
	}

	// end region -> Overridden Methods
}
