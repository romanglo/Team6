
package messages;

import entities.IEntity;

/**
 *
 * MessageFactoryMock: Mock class for message creator.
 * 
 */
public class MessageFactoryMock implements IMessagesFactory
{

	@Override
	public Message createMessage(IEntity entity, EntityDataOperation operation)
	{
		EntityData entityData = new EntityData(operation, entity);
		return new Message(entityData);
	}
}
