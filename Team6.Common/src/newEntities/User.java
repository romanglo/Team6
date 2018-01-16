
package newEntities;

import java.io.Serializable;

/**
 *
 * User: A POJO to database 'users' table.
 * 
 */
public class User implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -820282765139693892L;

	private String m_userName;

	private String m_password;

	private String m_email;

	private EntitiesEnums.UserPrivilege m_privilege;

	private EntitiesEnums.UserStatus m_status;

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return m_userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		m_userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return m_password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		m_password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return m_email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email)
	{
		m_email = email;
	}

	/**
	 * @return the privilege
	 */
	public EntitiesEnums.UserPrivilege getPrivilege()
	{
		return m_privilege;
	}

	/**
	 * @param privilege
	 *            the privilege to set
	 */
	public void setPrivilege(EntitiesEnums.UserPrivilege privilege)
	{
		m_privilege = privilege;
	}

	/**
	 * @return the status
	 */
	public EntitiesEnums.UserStatus getStatus()
	{
		return m_status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EntitiesEnums.UserStatus status)
	{
		m_status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "User [userName=" + m_userName + ", password=" + m_password + ", email=" + m_email + ", privilege="
				+ m_privilege + ", status=" + m_status + "]";
	}

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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (m_userName == null) {
			if (other.m_userName != null) {
				return false;
			}
		} else if (!m_userName.equals(other.m_userName)) {
			return false;
		}
		return true;
	}

}
