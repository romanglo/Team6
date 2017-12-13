
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
	 * Create {@link Message} with a {@link MessageData} of a {@link EntityData}
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
	 * Create {@link Message} with a {@link MessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#None} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createEntityMessage(IEntity entity)
	{
		return createEntityMessage(entity, EntityDataOperation.None);
	}

	/**
	 * Create {@link Message} with a {@link MessageData} of a {@link EntityData}
	 * with {@link EntityDataOperation#Update} operation.
	 *
	 * @param entity
	 *            The entity to add to message.
	 * @return An {@link Message}.
	 */
	public static Message createUpdateEntityMessage(IEntity entity)
	{

		return createEntityMessage(entity, EntityDataOperation.Update);
	}

	private static Message createEntityMessage(IEntity entity, EntityDataOperation entityDataOperation)
	{
		if (entity == null) {
			return null;
		}
		MessageData data = new EntityData(entityDataOperation, entity);
		return new Message(data);
	}

}
