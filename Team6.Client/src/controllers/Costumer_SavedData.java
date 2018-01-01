
package controllers;

import java.util.ArrayList;

import entities.CostumerEntity;
import entities.CostumerSubscription;
import entities.ItemEntity;
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
	 * Upgrade the approval status of the costumer.
	 *
	 * @param approved
	 *            the approved to set
	 */
	public static void setApproved(Boolean approved)
	{
		s_reservationEntity.getCostumer().setApproved(approved);
	}

	/**
	 * Upgrade the {@link ItemEntity} of the costumer.
	 *
	 * @param reservationList
	 *            the reservationList to set
	 */
	public static void setReservationList(ArrayList<ItemEntity> reservationList)
	{
		s_reservationEntity.setReservationList(reservationList);
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

	// end region -> Setters

	// region Initializing methods
	
	/**
	 * Initialize the fields.
	 */
	public static void initializeSavedData() {
		/* TODO Yoni: initialize with the correct user ID. */
		if (s_reservationEntity == null) {
			s_reservationEntity = new ReservationEntity(ReservationType.Open, 4);
		}
		s_reservationList = new ArrayList<>();
	}
	
	// end region -> Initializing methods

	// region Public Methods
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
