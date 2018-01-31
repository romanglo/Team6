
package controllers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import client.Client;
import client.IMessageSender;
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

	// end region -> Fields

	// region Constructors

	/**
	 * Constructor for testing.
	 *
	 * @param messageSender
	 *            The connection with the server.
	 * @param messagesFactory
	 *            The message factory that creates messages.
	 */
	public CostumerCancelReservation(IMessageSender messageSender, IMessagesFactory messagesFactory)
	{
		m_messageSender = messageSender;
		m_messagesFactory = messagesFactory;
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
		Date secondDate = new Date();

		long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
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
