
package controllers;

import java.util.ArrayList;
import java.util.Date;

import entities.Costumer;
import entities.IEntity;
import entities.Item;
import entities.ItemInReservation;
import entities.Reservation;
import entities.ShopCostumer;
import entities.EntitiesEnums.CostumerSubscription;
import entities.EntitiesEnums.ReservationDeliveryType;
import entities.EntitiesEnums.ReservationType;

/**
 *
 * Costumer_SavedData: Class that manages the data of the costumer when creating
 * reservation.
 *
 */
public class Costumer_SavedData
{
	// region Fields

	private static Reservation s_reservationEntity;

	private static Costumer s_costumer;

	private static ArrayList<IEntity> s_itemsInReservation;
	
	private static ShopCostumer s_shopCostumer;

	// end region -> Fields

	// region Getters

	/**
	 * @return Reservation entity.
	 */
	public static Reservation getReservationEntity()
	{
		int quantity = 0;
		for (IEntity entity : s_itemsInReservation) {
			ItemInReservation item = (ItemInReservation) entity;
			quantity += item.getQuantity();
		}
		s_reservationEntity.setNumberOfItems(quantity);

		return s_reservationEntity;
	}

	/**
	 * @return Costumer.
	 */
	public static Costumer getCostumer()
	{
		return s_costumer;
	}
	
	/**
	 * @return ShopCostumer.
	 */
	public static ShopCostumer getShopCostumer()
	{
		return s_shopCostumer;
	}

	/**
	 * @return Costumer id.
	 */
	public static Integer getCostumerId()
	{
		return s_costumer.getId();
	}
	
	/**
	 * @return the cumulative price
	 */
	public static float getCumulativePrice()
	{
		return s_shopCostumer.getCumulativePrice();
	}
	
	/**
	 * @return the shopManagerId
	 */
	public static int getShopManagerId()
	{
		return s_reservationEntity.getShopManagerId();
	}

	/**
	 * @return Costumer credit card number.
	 */
	public static String getCreditCard()
	{
		return s_reservationEntity.getCreditCard();
	}

	/**
	 * @return Costumer reservation list.
	 */
	public static ArrayList<IEntity> getCostumerReservationList()
	{
		return s_itemsInReservation;
	}

	/**
	 * @return Costumer subscription.
	 */
	public static CostumerSubscription getSubscription()
	{
		return s_shopCostumer.getCostumerSubscription();
	}

	/**
	 * @return The refunds that the costumer has.
	 */
	public static float getCostumerBalance()
	{
		return s_costumer.getBalance();
	}

	/**
	 * @return The total price of the reservation.
	 */
	public static float getTotalPrice()
	{
		return s_reservationEntity.getPrice();
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

	/**
	 * @return the deliveryAddress
	 */
	public static String getDeliveryAddress()
	{
		return s_reservationEntity.getDeliveryAddress();
	}

	/**
	 * @return the deliveryPhone
	 */
	public static String getDeliveryPhone()
	{
		return s_reservationEntity.getDeliveryPhone();
	}

	/**
	 * @return the deliveryName
	 */
	public static String getDeliveryName()
	{
		return s_reservationEntity.getDeliveryName();
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
		s_costumer.setId(id);
	}
	
	/**
	 * @param cumulativePrice
	 *            the cumulative price to set
	 */
	public static void setCumulativePrice(float cumulativePrice)
	{
		s_shopCostumer.setCumulativePrice(cumulativePrice);
	}
	
	/**
	 * Upgrade the shop of the costumer.
	 *
	 * @param shopCostumer
	 *            the shopCostumer to set
	 */
	public static void setShopCostumer(ShopCostumer shopCostumer)
	{
		s_shopCostumer = shopCostumer;
	}
	
	/**
	 * @param shopManagerId
	 *            the shopManagerId to set
	 */
	public static void setShopManagerId(int shopManagerId)
	{
		s_reservationEntity.setShopManagerId(shopManagerId);
		s_shopCostumer.setShopManagerId(shopManagerId);
	}


	/**
	 * Upgrade the credit card of the costumer.
	 *
	 * @param creditCard
	 *            the creditCard to set
	 */
	public static void setCreditCard(String creditCard)
	{
		s_reservationEntity.setCreditCard(creditCard);
	}

	/**
	 * Upgrade the {@link CostumerSubscription} of the costumer.
	 *
	 * @param subscription
	 *            the subscription to set
	 */
	public static void setSubscription(CostumerSubscription subscription)
	{
		s_shopCostumer.setCostumerSubscription(subscription);
	}

	/**
	 * Upgrade the {@link Item} of the costumer.
	 *
	 * @param reservationList
	 *            the reservationList to set
	 */
	public static void setReservationList(ArrayList<IEntity> reservationList)
	{
		s_itemsInReservation = reservationList;
	}

	/**
	 * Add the {@link IEntity} to the reservation list of the costumer.
	 *
	 * @param item
	 *            the item to add.
	 */
	public static void addItemToReservation(IEntity item)
	{
		ItemInReservation recievedItem = (ItemInReservation) item;
		if (s_itemsInReservation != null) {
			for (IEntity entity : s_itemsInReservation) {
				ItemInReservation itemInReservation = (ItemInReservation) entity;
				if (recievedItem.equals(itemInReservation)) {
					float price = itemInReservation.getPrice();
					int quantity = itemInReservation.getQuantity();
					price = price / quantity;
					quantity++;
					price = price * quantity;
					itemInReservation.setPrice(price);
					itemInReservation.setQuantity(quantity);
					return;
				}
			}
			s_itemsInReservation.add(item);
		}
	}
	
	/**
	 * Remove the {@link IEntity} from the reservation list of the costumer.
	 *
	 * @param item
	 *            the item to remove.
	 */
	public static void removeItemFromReservation(IEntity item)
	{
		ItemInReservation recievedItem = (ItemInReservation) item;
		if (s_itemsInReservation != null) {
			for (IEntity entity : s_itemsInReservation) {
				ItemInReservation itemInReservation = (ItemInReservation) entity;
				if (recievedItem.getItemId() == itemInReservation.getItemId()) {
					recievedItem = itemInReservation;
					break;
				}
			}
			s_itemsInReservation.remove(recievedItem);
		}
	}

	/**
	 * Update the refunds of the costumer.
	 *
	 * @param refund
	 *            The refunds of the costumer.
	 */
	public static void setBalance(float refund)
	{
		s_costumer.setBalance(refund);
	}

	/**
	 * Update the price of the reservation.
	 *
	 * @param price
	 *            The total price of the reservation.
	 */
	public static void setTotalPrice(float price)
	{
		s_reservationEntity.setPrice(price);
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

	/**
	 * @param deliveryAddress
	 *            the deliveryAddress to set
	 */
	public static void setDeliveryAddress(String deliveryAddress)
	{
		s_reservationEntity.setDeliveryAddress(deliveryAddress);
	}

	/**
	 * @param deliveryPhone
	 *            the deliveryPhone to set
	 */
	public static void setDeliveryPhone(String deliveryPhone)
	{
		s_reservationEntity.setDeliveryPhone(deliveryPhone);
	}

	/**
	 * @param deliveryName
	 *            the deliveryName to set
	 */
	public static void setDeliveryName(String deliveryName)
	{
		s_reservationEntity.setDeliveryName(deliveryName);
	}

	// end region -> Setters

	// region Initializing methods

	/**
	 * Initialize the fields.
	 * 
	 * @param costumer
	 *            The costumer connected to the system.
	 */
	public static void initializeSavedData(Costumer costumer)
	{
		s_costumer = costumer;
		s_reservationEntity = new Reservation();
		s_reservationEntity.setCostumerId(s_costumer.getId());
		s_reservationEntity.setType(ReservationType.Open);
		
		s_shopCostumer = new ShopCostumer();
		s_shopCostumer.setCostumerId(s_costumer.getId());

		s_itemsInReservation = new ArrayList<>();
	}

	// end region -> Initializing methods
}
