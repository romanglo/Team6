
package messages;

import java.io.Serializable;

import com.sun.istack.internal.NotNull;

import entities.IEntity;

/**
 *
 * AddEntityData: Describes a {@link IMessageData} that contains an
 * {@link IEntity} and an operation to execute on it.
 * 
 * @see Message This class suitable to {@link Message}
 */
public class EntityData implements IMessageData
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4732439024965484856L;

	private EntityDataOperation m_operation;

	private IEntity m_entity;

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
	 * @throws NullPointerException
	 *             If the constructor received null.
	 */
	public EntityData(@NotNull EntityDataOperation entityDataOperation, @NotNull IEntity entity) throws NullPointerException
	{
		if (entityDataOperation == null || entity == null) {
			throw new NullPointerException("The constructor of 'AddEntityData' can not get null parameters.");
		}
		m_entity = entity;
		m_operation = entityDataOperation;
	}

	

	// end region -> Constructors
	

	// region Object Methods Override
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "EntityData [Operation=" + m_operation + ", Entity=" + m_entity + "]";
	}
	
	
	// end region -> Object Methods Override

}
