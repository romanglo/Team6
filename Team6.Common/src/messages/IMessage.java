
package messages;

import java.util.Date;

/**
 *
 * IMessage: Describes messages that are described by ID, creation time and
 * {@link Object} data.
 * 
 */
public interface IMessage
{

	/**
	 * @return The ID of the the message.
	 */
	String getMessageId();

	/**
	 * @return The creation time of the message.
	 */
	Date getCreationTime();

	/**
	 * @return The actual data of the message
	 */
	MessageData getMessageData();
}
