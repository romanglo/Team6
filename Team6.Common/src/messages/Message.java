
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
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
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
	 * Create instance of message. Auto generation of message ID based on
	 * {@link UUID}, and time in UTC format.
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
	 * Create instance of message. Auto generation of message ID based on
	 * {@link UUID}, and time in UTC format. The message have null
	 * {@link IMessageData}.
	 * 
	 */
	Message()
	{
		m_messageData = null;
		m_id = UUID.randomUUID().toString();
		m_creationTime = new Date();
	}
	// end region -> Constructors

	// region Object Methods Override

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Message [ID=" + m_id + ", Creation Time=" + m_creationTime + ", Message Data=" + m_messageData + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
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
		if (!(obj instanceof Message)) {
			return false;
		}
		Message other = (Message) obj;
		if (m_id == null) {
			if (other.m_id != null) {
				return false;
			}
		} else if (!m_id.equals(other.m_id)) {
			return false;
		}
		return true;
	}

	// end region -> Object Methods Override
}
