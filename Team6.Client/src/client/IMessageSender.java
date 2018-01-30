
package client;

import com.sun.istack.internal.Nullable;

import messages.Message;

/**
 *
 * IMessageSender: TODO shpun: Auto-generated type stub - Change with type
 * description
 * 
 */
public interface IMessageSender
{

	/**
	 * Method send message to server.
	 *
	 * @param message
	 *            Information sent from the UI.
	 * @param messageReceiveHandler
	 * @return true if the sending succeed and false if does not.
	 */
	boolean sendMessageToServer(Message message);
	
	/**
	 * @param messagesHandler
	 *            Handler of messages, if a handler exist the new one will swap him.
	 *            <code>null</code> will remove the current handler.
	 */
	void setMessagesHandler(@Nullable IMessageReceiveHandler messagesHandler);

}
