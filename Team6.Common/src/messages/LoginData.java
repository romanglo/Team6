
package messages;

import java.io.Serializable;

import com.sun.istack.internal.NotNull;

/**
 *
 * LoginData: Describes the required data to login message, the type used by
 * {@link Message}.
 * 
 */
public class LoginData implements IMessageData
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 8197840005582687419L;

	private String m_userName;

	private String m_password;

	private boolean m_logout;

	private String m_message;

	// end region -> Fields

	// region Getters

	@SuppressWarnings("javadoc")
	public String getUserName()
	{
		return m_userName;
	}

	@SuppressWarnings("javadoc")
	public String getPassword()
	{
		return m_password;
	}

	/**
	 * 
	 * @return This message filled by the server if the user failed to login. The
	 *         message contains the failure reason.
	 */
	public String getMessage()
	{
		return m_message;
	}

	/**
	 * @param message
	 *            A message which contains the login failure reason.
	 */
	public void setMessage(String message)
	{
		m_message = message;
	}

	/**
	 * @return <code>true</code> if this logout message and <code>false</code> if
	 *         login message.
	 */
	public boolean isLogoutMessage()
	{
		return m_logout;
	}
	// end region -> Getters

	// region Constructors

	/**
	 * 
	 * Create instance of login data that suitable to {@link Message}.
	 *
	 * @param userName
	 *            the user name of the requested user to login.
	 * @param password
	 *            the password of the requested user to login.
	 */
	public LoginData(@NotNull String userName, @NotNull String password)
	{
		this(userName, password, false);
	}

	/**
	 * 
	 * Create instance of login\logout data that suitable to {@link Message}.
	 *
	 * @param userName
	 *            the user name of the requested user to login.
	 * @param password
	 *            the password of the requested user to login.
	 * @param logout
	 *            <code>true</code> for logout message and <code>false</code> for
	 *            login message.
	 */
	public LoginData(@NotNull String userName, @NotNull String password, boolean logout)
	{
		m_userName = userName;
		m_password = password;
		m_logout = logout;
		m_message = "";
	}

	// end region -> Constructors

	// region Object Methods Override

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "LoginData [UserName=" + m_userName + ", Password=" + m_password + ", IsLogout=" + m_logout
				+ ", ErrorMessage=" + m_message + "]";
	}

	// end region ->
}
