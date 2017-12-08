
package messages;

import java.util.Date;
import java.util.UUID;

import com.sun.istack.internal.NotNull;

/**
 *
 * Message: Generic implementation to {@link IMessage} interface.
 * 
 */
public class Message implements IMessage
{

	// region Fields

	private String m_id;

	private Date m_creationTime;

	private MessageData m_messageData;

	// end region -> Fields

	// region Getters

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageId()
	{
		return m_id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCreationTime()
	{
		return m_creationTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	public Message(@NotNull MessageData messageData) throws NullPointerException
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
