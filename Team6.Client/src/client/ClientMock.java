
package client;

import java.util.List;

import entities.Costumer;
import entities.IEntity;
import entities.Reservation;
import messages.EntityData;
import messages.EntityDataOperation;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 *
 * ClientMock: Client communication mock class for testing.
 * 
 */
public class ClientMock implements IMessageSender
{

	private List<Reservation> m_reservationList;

	private Costumer m_costumer;

	private IMessageReceiveHandler m_messagesHandler;

	/**
	 * Constructor.
	 * 
	 * @param reservationList
	 *            The list of reservations.
	 * @param costumer
	 *            The costumer connected to the reservation.
	 */
	public ClientMock(List<Reservation> reservationList, Costumer costumer)
	{
		m_reservationList = reservationList;
		m_costumer = costumer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMessagesHandler(IMessageReceiveHandler messagesHandler)
	{
		m_messagesHandler = messagesHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendMessageToServer(Message data)
	{
		IMessageData messageData = data.getMessageData();
		EntityData entityData = (EntityData) messageData;
		IEntity entity = entityData.getEntity();

		RespondMessageData respondMessageData;
		if (entity instanceof Costumer) {
			/* Received costumer entity. */
			respondMessageData = handleCostumerReceived(entity);
		} else if (entity instanceof Reservation) {
			/* Received reservation entity. */
			respondMessageData = handleReservationReceived(entity);
		} else {
			/* Entity received not of the type expected. */
			EntityData newEntityData = new EntityData(EntityDataOperation.None, entity);
			respondMessageData = new RespondMessageData(newEntityData, false);
		}

		Message message = MessagesFactory.createIMessageDataMessage(respondMessageData);
		try {
			m_messagesHandler.onMessageReceived(message);
		}
		catch (Exception ignored) {
			// Should never happen.
		}

		return true;
	}

	/**
	 * Method that handles an entity when it is an instance of {@link Costumer}.
	 *
	 * @param entity
	 *            The entity received.
	 * @return {@link RespondMessageData} to return to the message response
	 *         receiver.
	 */
	private RespondMessageData handleCostumerReceived(IEntity entity)
	{
		boolean isSuccess;
		EntityData newEntityData;
		Costumer costumer = (Costumer) entity;
		if (costumer.getId() != m_costumer.getId()) {
			newEntityData = new EntityData(EntityDataOperation.None, costumer);
			isSuccess = false;
		} else {
			m_costumer.setBalance(costumer.getBalance());
			newEntityData = new EntityData(EntityDataOperation.None, m_costumer);
			isSuccess = true;
		}

		return new RespondMessageData(newEntityData, isSuccess);
	}

	/**
	 * Method that handles an entity when it is an instance of {@link Reservation}.
	 *
	 * @param entity
	 *            The entity received.
	 * @return {@link RespondMessageData} to return to the message response
	 *         receiver.
	 */
	private RespondMessageData handleReservationReceived(IEntity entity)
	{
		boolean isSuccess;
		EntityData newEntityData;
		Reservation reservation = (Reservation) entity;
		Reservation toUpdate = null;
		boolean isPresent = false;

		for (Reservation reserv : m_reservationList) {
			if (reserv.getId() == reservation.getId() && reserv.getCostumerId() == reservation.getCostumerId()
					&& reserv.getShopManagerId() == reservation.getShopManagerId()) {
				isPresent = true;
				toUpdate = reserv;
				break;
			}
		}

		if (isPresent) {
			toUpdate.setType(reservation.getType());
			isSuccess = true;
			newEntityData = new EntityData(EntityDataOperation.None, toUpdate);
		} else {
			isSuccess = false;
			newEntityData = new EntityData(EntityDataOperation.None, reservation);
		}

		return new RespondMessageData(newEntityData, isSuccess);
	}
}
