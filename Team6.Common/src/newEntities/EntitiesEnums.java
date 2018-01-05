
package newEntities;

/**
 *
 * EntitiesEnums: Contains all the enumerators that related to {@link IEntity}.
 * 
 */
@SuppressWarnings("javadoc") public final class EntitiesEnums
{

	/**
	 *
	 * CostumerSubscription: Enumeration of the costumer subscription type.
	 * 
	 */
	public enum CostumerSubscription {
		Monthly, Yearly, None
	}

	/**
	 *
	 * ReservationType: Enumeration of the reservation type.
	 * 
	 */
	public enum ProductType {
		/**
		 * Base flower for customized items also.
		 */
		Flower, FlowerPot, BridalBouquet, FlowerArrangement,
	}

	/**
	 *
	 * DeliveryType: Enumeration for delivery type.
	 * 
	 */
	public enum ReservationDeliveryType {

		/**
		 * Delivery as soon as possible.
		 */
		Immidiate,
		/**
		 * Delivery for future date.
		 */
		Future,
		/**
		 * The costumer does not want a delivery.
		 */
		None
	}

	/**
	 *
	 * ReservationType: Enumeration of the reservation type.
	 * 
	 */
	public enum ReservationType {

		/**
		 * Reservation is not delivered yet.
		 */
		Open,
		/**
		 * Reservation was delivered.
		 */
		Closed,
		/**
		 * Reservation canceled.
		 */
		Canceled
	}

	/**
	 *
	 * UserPrivilege: Enumeration of the user privilege type.
	 * 
	 */
	public enum UserPrivilege {

		CompanyEmployee, ShopManager, ChainManager, Administrator, ShopEmployee, CostumerService, Costumer, ServiceSpecialist
	}

	/**
	 *
	 * UserStatus: Enumeration of the user status.
	 * 
	 */
	public enum UserStatus {

		Connected, Disconnected, Blocked
	}
}
