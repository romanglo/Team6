package client;

import messages.Message;

/**
 *
 * MessageReceiveHandler: Handler interface for message from the server to
 * display on UI.
 * 
 */
public interface IMessageReceiveHandler
{

	/**
	 * Prototype of a method that meant to process the message received from the
	 * server and updating the UI according to the Object details.
	 *
	 * @param msg
	 *            - An message received from the server.
	 * @throws Exception
	 *             The method can throw any kind of exception, this method call
	 *             surround with try/catch.
	 * 
	 * @see Message the received type.
	 */
	void onMessageReceived(Message msg) throws Exception;
}