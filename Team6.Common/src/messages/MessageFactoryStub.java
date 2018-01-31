
package messages;

import entities.IEntity;

/**
 * MessageFactoryStub: Stub class for message creator.
 */
public class MessageFactoryStub implements IMessagesFactory
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message createMessage(IEntity entity, EntityDataOperation operation)
	{
		EntityData entityData = new EntityData(operation, entity);
		return new Message(entityData);
	}
}
