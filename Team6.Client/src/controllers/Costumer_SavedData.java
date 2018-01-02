
package controllers;

import java.util.ArrayList;
import java.util.Date;

import entities.CostumerEntity;
import entities.CostumerSubscription;
import entities.ItemEntity;
import entities.ReservationDeliveryType;
import entities.ReservationEntity;
import entities.ReservationType;

/**
 *
 * Costumer_SavedData: Class that manages the data of the costumer when creating
 * reservation.
 *
 */
public class Costumer_SavedData
{
	// region Fields

	private static ArrayList<ItemEntity> s_reservationList;

	private static ReservationEntity s_reservationEntity;

	// end region -> Fields

	// region Getters

	/**
	 * @return Reservation entity.
	 */
	public static ReservationEntity getReservationEntity()
	{
		return s_reservationEntity;
	}

	/**
	 * @return costumer entity.
	 */
	public static CostumerEntity getCostumer()
	{
		return s_reservationEntity.getCostumer();
	}

	/**
	 * @return Costumer id.
	 */
	public static Integer getCostumerId()
	{
		return s_reservationEntity.getCostumerId();
	}

	/**
	 * @return Costumer credit card number.
	 */
	public static String getCreditCard()
	{
		return s_reservationEntity.getCostumer().getCreditCard();
	}

	/**
	 * @return Costumer reservation list.
	 */
	public static ArrayList<ItemEntity> getCostumerReservationList()
	{
		return s_reservationList;
	}

	/**
	 * @return Costumer subscription.
	 */
	public static CostumerSubscription getSubscription()
	{
		return s_reservationEntity.getCostumer().getSubscription();
	}

	/**
	 * @return The refunds that the costumer has.
	 */
	public static Double getCostumerRefund()
	{
		return s_reservationEntity.getCostumer().getCostumerRefunds();
	}

	/**
	 * @return The total price of the reservation.
	 */
	public static Double getTotalPrice()
	{
		return s_reservationEntity.getTotalPrice();
	}

	/**
	 * @return A enumerator which describes the reservation type.
	 */
	public static ReservationType getType()
	{
		return s_reservationEntity.getType();
	}
	
	/**
	 * @return Delivery date requested.
	 */
	public static Date getDeliveryDate()
	{
		return s_reservationEntity.getDeliveryDate();
	}

	/**
	 * @return Delivery type.
	 */
	public static ReservationDeliveryType getDeliveryType()
	{
		return s_reservationEntity.getDeliveryType();
	}

	/**
	 * @return Blessing card string.
	 */
	public static String getBlessingCard()
	{
		return s_reservationEntity.getBlessingCard();
	}

	// end region -> Getters

	// region Setters

	/**
	 * Upgrade the ID of the costumer.
	 *
	 * @param id
	 *            the id to set
	 */
	public static void setId(Integer id)
	{
		s_reservationEntity.getCostumer().setId(id);
	}

	/**
	 * Upgrade the credit card of the costumer.
	 *
	 * @param creditCard
	 *            the creditCard to set
	 */
	public static void setCreditCard(String creditCard)
	{
		s_reservationEntity.getCostumer().setCreditCard(creditCard);
	}

	/**
	 * Upgrade the {@link CostumerSubscription} of the costumer.
	 *
	 * @param subscription
	 *            the subscription to set
	 */
	public static void setSubscription(CostumerSubscription subscription)
	{
		s_reservationEntity.getCostumer().setSubscription(subscription);
	}

	/**
	 * Upgrade the {@link ItemEntity} of the costumer.
	 *
	 * @param reservationList
	 *            the reservationList to set
	 */
	public static void setReservationList(ArrayList<ItemEntity> reservationList)
	{
		s_reservationList = reservationList;
	}

	/**
	 * Upgrade the {@link ItemEntity} of the costumer.
	 */
	public static void setReservationList()
	{
		s_reservationEntity.setReservationList(s_reservationList);
	}

	/**
	 * Add the {@link ItemEntity} to the reservation list of the costumer.
	 *
	 * @param reservation
	 *            the reservation to add.
	 */
	public static void addReservation(ItemEntity reservation)
	{
		if (s_reservationList != null) {
			s_reservationList.add(reservation);
			System.out.println(s_reservationList);
		}
	}

	/**
	 * Update the refunds of the costumer.
	 *
	 * @param refund
	 *            The refunds of the costumer.
	 */
	public static void setRefund(Double refund)
	{
		s_reservationEntity.getCostumer().setRefund(refund);
	}

	/**
	 * Update the price of the reservation.
	 *
	 * @param price
	 *            The total price of the reservation.
	 */
	public static void setTotalPrice(Double price)
	{
		s_reservationEntity.setTotalPrice(price);
	}

	/**
	 * Upgrade the {@link ReservationType}.
	 *
	 * @param m_reservationType
	 *            the m_reservationType to set
	 */
	public static void setType(ReservationType m_reservationType)
	{
		s_reservationEntity.setType(m_reservationType);
	}
	
	/**
	 * Upgrade the {@link Date} of the delivery.
	 *
	 * @param deliveryDate
	 *            The date of the delivery.
	 */
	public static void setDeliveryDate(Date deliveryDate)
	{
		s_reservationEntity.setDeliveryDate(deliveryDate);
	}

	/**
	 * Upgrade the {@link ReservationDeliveryType} of the delivery.
	 *
	 * @param deliveryType
	 *            The delivery type.
	 */
	public static void setDeliveryType(ReservationDeliveryType deliveryType)
	{
		s_reservationEntity.setDeliveryType(deliveryType);
	}

	/**
	 * Upgrade the blessing card of the reservation.
	 *
	 * @param blessing
	 *            The blessing string.
	 */
	public static void setBlessingCard(String blessing)
	{
		s_reservationEntity.setBlessingCard(blessing);
	}

	// end region -> Setters

	// region Initializing methods

	/**
	 * Initialize the fields.
	 * 
	 * @param costumer
	 *            The costumer connected to the system.
	 */
	public static void initializeSavedData(CostumerEntity costumer)
	{
		s_reservationEntity = new ReservationEntity(ReservationType.Open, costumer);
		s_reservationList = new ArrayList<>();
	}

	// end region -> Initializing methods
}