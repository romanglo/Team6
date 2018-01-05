
package entities;

import java.io.Serializable;

/**
 *
 * ShopManagerEntity: Class that generates a shop manager information.
 * 
 */
public class ShopManagerEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -8657979359201142986L;

	private int m_id;

	private UserEntity m_user;

	// end region -> Fields

	// region Getters

	/**
	 * @return A unique number for the shop manager.
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @return The user entity.
	 */
	public UserEntity getUser()
	{
		return m_user;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Update the id of the shop manager.
	 *
	 * @param id
	 *            A unique id for the shop manager.
	 */
	public void setId(int id)
	{
		m_id = id;
	}

	/**
	 * Update the user entity.
	 *
	 * @param user
	 *            The user entity to set.
	 */
	public void setUser(UserEntity user)
	{
		m_user = user;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link ShopManagerEntity}. Dedicated for add or update
	 * messages.
	 *
	 * @param id
	 *            A unique id for the shop manager.
	 * @param user
	 *            The user entity of the shop manager.
	 */
	public ShopManagerEntity(int id, UserEntity user)
	{
		m_id = id;
		m_user = user;
	}

	// end region -> Constructors

	// region Overridden Methods

	@Override
	public String toString()
	{
		return "ShopManagerEntity [ID=" + m_id + ", User=" + m_user + "]";
	}

	// end region -> Overridden Methods
}
