
package messages;

import entities.IEntity;

/**
 *
 * MessagesFactory: Factory of {@link Message}.
 * 
 */
public class MessagesFactory
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
	 * with {@link EntityDataOperation#Update} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message} if the creation succeed or <code>null</code> if
	 *         failed.
	 */
	public static Message createUpdateAllEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.UpdateAll);
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
}
