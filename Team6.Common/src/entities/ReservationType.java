package entities;

/**
 *
 * ReservationType:
 * Enumeration of the reservation type.
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