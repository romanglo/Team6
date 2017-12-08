
package messages;

import com.sun.istack.internal.NotNull;

import entities.IEntity;

/**
 *
 * AddEntityData: Describes the required data to Add entity message, the type
 * used by {@link IMessage}.
 * 
 */
public class EntityData implements MessageData
{
	// region Fields

	private EntityDataOperation m_operation;

	private IEntity m_entity;

	private Class<? extends IEntity> m_entityType;

	// end region -> Fields

	// region Getters

	/**
	 * @return The required {@link IEntity} to manipulate.
	 */
	public IEntity getEntity()
	{
		return m_entity;
	}

	/**
	 * @return The required {@link IEntity} to add.
	 */
	public Class<? extends IEntity> getEntityType()
	{
		return m_entityType;
	}

	/**
	 * @return The operation that required to do on the {@link IEntity}.
	 */
	public EntityDataOperation getOperation()
	{
		return m_operation;
	}

	// end region -> Getters

	// region Constructors

	/**
	 *
	 * @param entityDataOperation
	 *            The required operation to do on the {@link IEntity}.
	 * @param entity
	 *            The {@link IEntity} which manipulated.
	 * @param entityType
	 *            The type of the entity.
	 * @throws NullPointerException
	 *             If the constructor received null.
	 */
	public EntityData(@NotNull EntityDataOperation entityDataOperation, @NotNull IEntity entity,
			@NotNull Class<? extends IEntity> entityType) throws NullPointerException
	{
		if (entityDataOperation == null || entity == null || m_entityType == null) {
			throw new NullPointerException("The constructor of 'AddEntityData' can not get null parameters.");
		}
		m_entity = entity;
		m_entityType = entityType;
	}

	// end region -> Constructors
}
