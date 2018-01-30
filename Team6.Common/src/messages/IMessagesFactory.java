
package messages;

import entities.IEntity;

/**
 *
 * IMessagesFactory: Interface describes the classes that implements design
 * factories.
 * 
 */
public interface IMessagesFactory
{

	/**
	 * Method creates a {@link Message} with {@link IEntity} and
	 * {@link EntityDataOperation} type.
	 *
	 * @param entity
	 *            The entity to send in the message.
	 * @param operation
	 *            The operation of the message.
	 * @return Built message.
	 */
	Message createMessage(IEntity entity, EntityDataOperation operation);
}
