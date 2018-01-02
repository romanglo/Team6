
package entities;

import java.io.Serializable;

/**
 *
 * ProductEntity: Describes an entity of product.
 * 
 */
public class UserEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -7735420989107662314L;

	private String m_userName;

	private String m_password;

	private String m_email;

	private UserPrivilege m_userPrivilege;

	private UserStatus m_userStatus;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique UserName of the user.
	 */
	public String getUserName()
	{
		return m_userName;
	}

	/**
	 * @return The password of the user.
	 */
	public String getPassword()
	{
		return m_password;
	}

	/**
	 * @return The email of the user.
	 */
	public String getEmail()
	{
		return m_email;
	}

	/**
	 * @return An enumerator which describes the privilege of the user.
	 */
	public UserPrivilege getUserPrivilege()
	{
		return m_userPrivilege;
	}

	/**
	 * @return An enumerator which describes the connection status of the user.
	 */
	public UserStatus getUserStatus()
	{
		return m_userStatus;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param userStatus
	 *            The new user status.
	 */
	public void setUserStatus(UserStatus userStatus)
	{
		m_userStatus = userStatus;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link UserEntity}. Dedicated for add or update messages.
	 * 
	 * @param userName
	 *            An unique UserName of the user.
	 * @param password
	 *            The password of the user.
	 * @param email
	 *            The email of the user.
	 * @param userPrivilege
	 *            A enumerator which describes the user privilege.
	 * @param userStatus
	 *            A enumerator which describes the user connection status.
	 */
	public UserEntity(String userName, String password, String email, UserPrivilege userPrivilege,
			UserStatus userStatus)
	{
		m_userName = userName;
		m_password = password;
		m_email = email;
		m_userPrivilege = userPrivilege;
		m_userStatus = userStatus;
	}

	/**
	 * Create instance of {@link UserEntity}. The message contains only 'UserName'
	 * field all the other fields are <code>null</code>.
	 * 
	 * @param userName
	 *            An unique UserName of the user.
	 */
	public UserEntity(String userName)
	{
		this(userName, null, null, null, null);
	}

	// end region -> Constructors

	// region Override Object Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_userName == null) ? 0 : m_userName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof UserEntity)) return false;
		UserEntity other = (UserEntity) obj;
		if (m_userName == null) {
			if (other.m_userName != null) return false;
		} else if (!m_userName.equals(other.m_userName)) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "UserEntity [UserName=" + m_userName + ", Password=" + m_password + ", Email=" + m_email
				+ ", UserPrivilege=" + m_userPrivilege + ", UserStatus=" + m_userStatus + "]";
	}

	// end region -> Override Object Methods
}
