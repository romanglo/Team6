
package messages;

import java.io.Serializable;
import java.util.List;

import com.sun.istack.internal.NotNull;

import entities.IEntity;

/**
 *
 * ManyEntityData: Describes a {@link IMessageData} that contains a {@link List}
 * of {@link IEntity} and an operation to execute on all of them.
 * 
 * @see Message This class suitable to {@link Message}
 */
public class EntitiesListData implements IMessageData
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -7196038398678029778L;

	private EntityDataOperation m_operation;

	private List<IEntity> m_entities;

	// end region -> Fields

	// region Getters

	/**
	 * @return The required {@link List} of {@link IEntity} to manipulate.
	 */
	public List<IEntity> getEntities()
	{
		return m_entities;
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
	 * @param entities
	 *            A {@link List} of {@link IEntity}.
	 * @throws NullPointerException
	 *             If the constructor received null.
	 */
	public EntitiesListData(@NotNull EntityDataOperation entityDataOperation, @NotNull List<IEntity> entities)
			throws NullPointerException
	{
		if (entityDataOperation == null || entities == null) {
			throw new NullPointerException("The constructor of 'AddEntityData' can not get null parameters.");
		}
		m_entities = entities;
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
		StringBuilder stringBuilder = new StringBuilder('[');
		for (IEntity entity : m_entities) {
			stringBuilder.append(entity.toString());
			stringBuilder.append(", ");
		}
		stringBuilder.append(']');
		return "EntitiesListData [Operation=" + m_operation + ", Entities=" + stringBuilder.toString() + "]";
	}

	// end region -> Object Methods Override
}
