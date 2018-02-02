
package controllers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import client.IMessageSender;
import common.ITimeProvider;
import common.RealTimeProvider;
import entities.Costumer;
import entities.EntitiesEnums.ReservationType;
import entities.Reservation;
import messages.EntityDataOperation;
import messages.IMessagesFactory;
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * CostumerCancelReservation: Class that contains the logics behind the cancel
 * reservation screen.
 * 
 */
public class CostumerCancelReservation
{

	// region Fields

	private IMessageSender m_messageSender;

	private IMessagesFactory m_messagesFactory;

	private ITimeProvider m_timeProvider;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * @param messageSender
	 *            The connection with the server.
	 */
	public CostumerCancelReservation(IMessageSender messageSender)
	{
		m_messageSender = messageSender;
		m_messagesFactory = new MessagesFactory();
		m_timeProvider = new RealTimeProvider();
	}

	/**
	 * Constructor for testing.
	 *
	 * @param messageSender
	 *            The connection with the server.
	 * @param messagesFactory
	 *            The message factory that creates messages.
	 * @param timeProvider
	 *            The instance that provide the system current time.
	 */
	public CostumerCancelReservation(IMessageSender messageSender, IMessagesFactory messagesFactory,
			ITimeProvider timeProvider)
	{
		m_messageSender = messageSender;
		m_messagesFactory = messagesFactory;
		m_timeProvider = timeProvider;
	}
	// end region -> Constructors

	// region Public Methods

	/**
	 * Method that updates the reservation displayed with canceled type.
	 * 
	 * @param costumer
	 *            The costumer that the reservation belongs to.
	 * @param reservation
	 *            The reservation to cancel.
	 * @return If the send method succeeded.
	 */
	public boolean cancelReservation(Costumer costumer, Reservation reservation)
	{
		if (costumer == null || reservation == null) {
			return false;
		}

		reservation.setType(ReservationType.Canceled);
		Message entityMessage = m_messagesFactory.createMessage(reservation, EntityDataOperation.Update);
		m_messageSender.sendMessageToServer(entityMessage);

		float balance = calculateRefund(reservation) + costumer.getBalance();
		costumer.setBalance(balance);
		entityMessage = m_messagesFactory.createMessage(costumer, EntityDataOperation.Update);
		return m_messageSender.sendMessageToServer(entityMessage);
	}

	/**
	 * Method calculates the refund that the costumer is supposed to get when
	 * canceling a reservation.
	 * 
	 * @param reservation
	 *            The reservation to calculate refund from.
	 *
	 * @return The refund for the costumer.
	 * @throws RuntimeException
	 *             In case of wrong data received.
	 */
	public float calculateRefund(Reservation reservation) throws RuntimeException
	{
		if (reservation == null || reservation.getPrice() < 0) {
			throw new RuntimeException();
		}
		Date firstDate = reservation.getDeliveryDate();
		Date secondDate = m_timeProvider.getCurrentTime();

		long diffInMillies = firstDate.getTime() - secondDate.getTime();
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		float returnValue;
		if (diff >= 3) {
			returnValue = reservation.getPrice();
		} else if (diff >= 1) {
			returnValue = (float) (reservation.getPrice() * 0.5);
		} else {
			returnValue = 0;
		}

		return returnValue;
	}

	// end region -> Public Methods
}
