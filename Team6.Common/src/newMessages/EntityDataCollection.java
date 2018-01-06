/**
 * Package Name: messages Created: 30-12-2017
 */

package newMessages;

import java.io.Serializable;
import java.util.List;

import com.sun.istack.internal.NotNull;

/**
 *
 * EntityDataCollection: Describes a {@link IMessageData} that contains a
 * {@link List} of {@link EntityData}.
 * 
 * @see Message This class suitable to {@link Message}
 */
public class EntityDataCollection implements IMessageData
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 8135881541915140772L;

	private List<EntityData> m_entityDataList;

	// end region -> Fields

	// region Getters

	/**
	 * @return The required {@link List} of {@link EntityData}.
	 */
	public List<EntityData> getEntityDataList()
	{
		return m_entityDataList;
	}

	// end region -> Getters

	// region Constructors

	/**
	 * @param entities
	 *            A {@link List} of {@link EntityData}.
	 * @throws NullPointerException
	 *             If the constructor received null.
	 */
	public EntityDataCollection(@NotNull List<EntityData> entities) throws NullPointerException
	{
		if (entities == null) {
			throw new NullPointerException("The constructor of 'AddEntityData' can not get null parameters.");
		}
		m_entityDataList = entities;
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
		for (EntityData entity : m_entityDataList) {
			stringBuilder.append(entity.toString());
			stringBuilder.append(", ");
		}
		stringBuilder.append(']');
		return "ManyEntityData [EntityDataList=" + stringBuilder.toString() + "]";
	}

	// end region -> Object Methods Override
}
