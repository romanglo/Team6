
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
	public boolean sendMessageToServer(Message data)
	{
		boolean isSuccess = false;
		RespondMessageData respondMessageData;
		EntityData newEntityData = null;
		IMessageData messageData = data.getMessageData();
		EntityData entityData = (EntityData) messageData;

		IEntity entity = entityData.getEntity();
		if (entity instanceof Costumer) {
			/* Received costumer entity. */
			Costumer costumer = (Costumer) entity;
			if (costumer.getId() != m_costumer.getId()) {
				newEntityData = new EntityData(EntityDataOperation.None, costumer);
				isSuccess = false;
			} else {
				m_costumer.setBalance(costumer.getBalance());
				newEntityData = new EntityData(EntityDataOperation.None, m_costumer);
				isSuccess = true;
			}
		} else if (entity instanceof Reservation) {
			/* Received reservation entity. */
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
		}

		respondMessageData = new RespondMessageData(newEntityData, isSuccess);
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
	 * {@inheritDoc}
	 */
	@Override
	public void setMessagesHandler(IMessageReceiveHandler messagesHandler)
	{
		m_messagesHandler = messagesHandler;
	}
}
