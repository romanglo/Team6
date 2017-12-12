
package messages;

import java.util.Date;
import java.util.UUID;

import com.sun.istack.internal.NotNull;

/**
 *
 * Message: Define messages that are described by ID, creation time and
 * {@link Object} data.
 * This class instance created by {@link MessagesFactory}.
 */
public class Message
{

	// region Fields

	private String m_id;

	private Date m_creationTime;

	private MessageData m_messageData;

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
	public MessageData getMessageData()
	{
		return m_messageData;
	}

	// end region -> Getters

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
	Message(@NotNull MessageData messageData) throws NullPointerException
	{
		if (messageData == null) {
			throw new NullPointerException("Message constructor received null poitner as MessageData parameter.");
		}
		m_messageData = messageData;
		m_id = UUID.randomUUID().toString();
		m_creationTime = new Date();
	}

	// end region -> Constructors
}
