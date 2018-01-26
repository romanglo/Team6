
package boundaries;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * ReservationStatusRow: Represent the reservation status table row data.
 * 
 */
public class ReservationStatusRow
{

	// region Fields
	private Integer m_reservationId;

	private Integer m_costumerId;

	private Float m_reservationPrice;

	private String m_reservationStatus;

	private Date m_deliveryDate;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	// end region -> Fields

	// region Getters

	/**
	 * Get the reservation id.
	 *
	 * @return The reservation id.
	 */
	public Integer getReservationId()
	{
		return m_reservationId;
	}

	/**
	 * Get the reservation owner id (costumer id).
	 *
	 * @return The costumer id.
	 */
	public Integer getCostumerId()
	{
		return m_costumerId;
	}

	/**
	 * Get the reservation price.
	 *
	 * @return The reservation price.
	 */
	public Float getReservationPrice()
	{
		return m_reservationPrice;
	}

	/**
	 * Get the reservation status.
	 *
	 * @return The reservation status.
	 */
	public String getReservationStatus()
	{
		return m_reservationStatus;
	}

	/**
	 * Get the reservation delivery date.
	 *
	 * @return The reservation delivery date.
	 */
	public Date getDeliveryDate()
	{
		return m_deliveryDate;
	}
	
	/**
	 * Get the reservation delivery date in format dd.MM.yyyy HH:mm:ss .
	 *
	 * @return The reservation delivery date.
	 */
	public String getDeliveryDateInFormat()
	{
		return format.format(m_deliveryDate);
	}

	// end region -> Getters

	// region Setters

	/**
	 * Set reservation status.
	 *
	 * @param reservationStatus
	 *            The new reservation status.
	 */
	public void setReservationStatus(String reservationStatus)
	{
		m_reservationStatus = reservationStatus;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Constructor for reservation status table.
	 *
	 * @param reservationId
	 *            The reservation id.
	 * @param costumerId
	 *            The reservation owner id (costumer id).
	 * @param reservationPrice
	 *            The reservation price.
	 * @param reservationStatus
	 *            The reservation status.
	 * @param deliveryDate
	 *            The reservation delivery date.
	 */
	public ReservationStatusRow(Integer reservationId, Integer costumerId, Float reservationPrice,
			String reservationStatus, Date deliveryDate)
	{
		super();
		m_reservationId = reservationId;
		m_costumerId = costumerId;
		m_reservationPrice = reservationPrice;
		m_reservationStatus = reservationStatus;
		m_deliveryDate = deliveryDate;
	}

	// end region -> Constructors

	// region Public Methods
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
