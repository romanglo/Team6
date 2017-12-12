package messages;

import entities.IEntity;
import entities.ProductEntity;

public class MessagesFactory
{
	public static Message createGetEntityMessage(IEntity entity) {
		return createEntityMessage(entity,EntityDataOperation.Get);
	}
	
	public static Message createEntityMessage(IEntity entity) {
		return createEntityMessage(entity,EntityDataOperation.None);
	}
	
	public static Message createUpdateEntityMessage(IEntity entity) {

		return createEntityMessage(entity,EntityDataOperation.Update);
	}
	
	private static Message createEntityMessage(IEntity entity, EntityDataOperation entityDataOperation) {
		if(entity == null) {
			return null;
		}
		MessageData data = new EntityData(entityDataOperation, entity);
		return new Message(data);
	}
	
}
