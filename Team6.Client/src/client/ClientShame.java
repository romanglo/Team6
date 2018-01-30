
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
public class ClientShame implements IMessageSender
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
	public ClientShame(List<Reservation> reservationList, Costumer costumer)
	{
		m_reservationList = reservationList;
		m_costumer = costumer;
	}

	@Override
	public boolean sendMessageToServer(Message data)
	{
		if (data == null) {
			return false;
		}

		boolean isSuccess = false;
		RespondMessageData respondMessageData;
		EntityData newEntityData;
		IMessageData messageData = data.getMessageData();
		if (!(messageData instanceof EntityData)) {
			return false;
		}

		EntityData entityData = (EntityData) messageData;
		if (entityData.getOperation() != EntityDataOperation.Update) {
			return false;
		}

		IEntity entity = entityData.getEntity();
		if (entity instanceof Costumer) {
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
		} else {
			newEntityData = new EntityData(EntityDataOperation.None, entity);
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

	@Override
	public void setMessagesHandler(IMessageReceiveHandler messagesHandler)
	{
		m_messagesHandler = messagesHandler;
	}
}
