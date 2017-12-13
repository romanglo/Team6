
package messages;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.sun.istack.internal.NotNull;

/**
 *
 * Message: Define messages that are described by ID, creation time and
 * {@link Object} data. This class instance created by {@link MessagesFactory}.
 */
public class Message implements Serializable
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements {@link Serializable}
	 */
	private static final long serialVersionUID = 4562040062542908564L;

	private String m_id;

	private Date m_creationTime;

	private IMessageData m_messageData;

	// end region -> Fields

	// region Getters

	/**
	 * @return The ID of the message.
	 */
	public String getMessageId()
	{
		return m_id;
	}

	/**
	 * @return The creation time of the message.
	 */
	public Date getCreationTime()
	{
		return m_creationTime;
	}

	/**
	 * @return The actual data of the message
	 */
	public IMessageData getMessageData()
	{
		return m_messageData;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param message
	 *            set actual data of the message
	 */
	public void setMessageData(IMessageData message)
	{
		m_messageData = message;
	}
	// end region -> Setters

	// region Constructors

	/**
	 * 
	 * Create instance of class that implements {@link IMessage}. Auto generation of
	 * message ID based on {@link UUID}, and time in UTC format.
	 * 
	 * @param messageData
	 *            The actual data of the message.
	 * 
	 * @throws NullPointerException
	 *             If received null pointer.
	 * 
	 */
	Message(@NotNull IMessageData messageData) throws NullPointerException
	{
		if (messageData == null) {
			throw new NullPointerException("Message constructor received null poitner as MessageData parameter.");
		}
		m_messageData = messageData;
		m_id = UUID.randomUUID().toString();
		m_creationTime = new Date();
	}

	/**
	 * 
	 * Create instance of class that implements {@link IMessage}. Auto generation of
	 * message ID based on {@link UUID}, and time in UTC format. The message have
	 * null {@link IMessageData}.
	 * 
	 */
	Message()
	{
		m_messageData = null;
		m_id = UUID.randomUUID().toString();
		m_creationTime = new Date();
	}
	// end region -> Constructors
}
