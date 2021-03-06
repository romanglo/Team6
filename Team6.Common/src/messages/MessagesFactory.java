
package messages;

import java.util.ArrayList;
import java.util.List;

import entities.IEntity;

/**
 *
 * MessagesFactory: Factory of {@link Message}.
 * 
 */
public class MessagesFactory implements IMessagesFactory
{

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Get} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createGetEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.Get);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Get} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createGetAllEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.GetALL);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Remove} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createRemoveEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.Remove);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#None} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.None);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Update} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createUpdateEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.Update);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Add} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createAddEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.Add);
	}

	private static Message createEntityMessage(IEntity entity, EntityDataOperation entityDataOperation)
	{
		if (entity == null) {
			return null;
		}
		IMessageData data = new EntityData(entityDataOperation, entity);
		return new Message(data);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a
	 * {@link EntitiesListData} with {@link EntityDataOperation#Remove} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createRemoveEntitiesMessage(ArrayList<IEntity> entity)
	{
		return createEntitiesListMessage(entity, EntityDataOperation.Remove);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a
	 * {@link EntitiesListData} with {@link EntityDataOperation#None} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createEntitiesMessage(ArrayList<IEntity> entity)
	{
		return createEntitiesListMessage(entity, EntityDataOperation.None);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a
	 * {@link EntitiesListData} with {@link EntityDataOperation#Update} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createUpdateEntitiesMessage(ArrayList<IEntity> entity)
	{
		return createEntitiesListMessage(entity, EntityDataOperation.Update);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a
	 * {@link EntitiesListData} with {@link EntityDataOperation#Add} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createAddEntitiesMessage(ArrayList<IEntity> entity)
	{
		return createEntitiesListMessage(entity, EntityDataOperation.Add);
	}

	private static Message createEntitiesListMessage(ArrayList<IEntity> entity, EntityDataOperation entityDataOperation)
	{
		if (entity == null || entity.isEmpty()) {
			return null;
		}
		IMessageData data = new EntitiesListData(entityDataOperation, entity);
		return new Message(data);
	}
	
	/**
	 * Create {@link Message} with a {@link IMessageData}.
	 *
	 * @param messageData
	 *            The message data to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createIMessageDataMessage(IMessageData messageData) {
		return new Message(messageData);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a
	 * {@link EntityDataCollection} with {@link List} of {@link EntityData}.
	 *
	 * @param entitiesdata
	 *            {@link List} of {@link EntityData}.
	 * @return An {@link Message}.
	 */
	public static Message createEntitiesListMessage(List<EntityData> entitiesdata)
	{
		if (entitiesdata == null || entitiesdata.isEmpty()) {
			return null;
		}
		IMessageData data = new EntityDataCollection(entitiesdata);
		return new Message(data);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link LoginData}
	 * with login request.
	 * 
	 * @param username
	 *            the user name of the requested user to login.
	 * @param password
	 *            the password of the requested user to login.
	 *
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createLoginMessage(String username, String password)
	{
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return null;
		}
		IMessageData data = new LoginData(username, password);
		return new Message(data);
	}

	/**
	 * Create {@link Message} with a {@link IMessageData} of a {@link LoginData}
	 * with logout request.
	 * 
	 * @param username
	 *            the user name of the requested user to logout.
	 * @param password
	 *            the password of the requested user to logout.
	 *
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createLogoutMessage(String username, String password)
	{
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return null;
		}
		IMessageData data = new LoginData(username, password, true);
		return new Message(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message createMessage(IEntity entity, EntityDataOperation operation)
	{
		return createEntityMessage(entity, operation);
	}
}
