
package messages;

import java.io.Serializable;

import com.sun.istack.internal.NotNull;

/**
 *
 * AddEntityData: Is a class that contains a respond to request, the class
 * contains the requested {@link IMessageData} and a boolean respond if request
 * execution succeed or failed.
 * 
 * @see Message This class suitable to {@link Message}
 */
public class RespondMessageData implements IMessageData
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4732439024965484856L;

	private IMessageData m_messageData;

	private boolean m_succeed;

	// end region -> Fields

	// region Getters

	/**
	 * @return The request {@link IMessageData}.
	 */
	public IMessageData getMessageData()
	{
		return m_messageData;
	}

	/**
	 * @return <code>true</code> if execution was successful and <code>false</code>
	 *         if execution failed.
	 */
	public boolean isSucceed()
	{
		return m_succeed;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param succeed
	 *            <code>true</code> if execution was successful and
	 *            <code>false</code> if execution failed
	 *
	 */
	public void setSucceed(boolean succeed)
	{
		m_succeed = succeed;
	}

	/**
	 * @param messageData
	 *            The request {@link IMessageData}.
	 */
	public void setMessageData(IMessageData messageData)
	{
		m_messageData = messageData;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link RespondMessageData}.
	 * 
	 * @param request
	 *            The request {@link IMessageData}.
	 * @param succeed
	 *            <code>true</code> if execution was successful and
	 *            <code>false</code> if execution failed
	 *
	 * @throws NullPointerException
	 *             If the constructor received null.
	 */
	public RespondMessageData(@NotNull IMessageData request, boolean succeed) throws NullPointerException
	{
		if (request == null) {
			throw new NullPointerException("The constructor of 'RespondMessageData' can not get null parameters.");
		}
		m_messageData = request;
		m_succeed = succeed;
	}

	/**
	 * Create instance of {@link RespondMessageData}.
	 */
	public RespondMessageData()
	{
		m_messageData = null;
		m_succeed = false;
	}

	// end region -> Constructors

	// region Object Methods Override

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "RespondMessageData [request message data=" + m_messageData + ", execution succeed=" + m_succeed + "]";
	}

	// end region -> Object Methods Override

}
