package connectivity;

import messages.Message;
import ocsf.server.ConnectionToClient;

/**
 * WaitingMessage: A package internal class, used by {@link Server} for contains
 * the received {@link Message}s.
 *
 */
class WaitingMessage {

	// region Fields

	private Message m_message;

	private final ConnectionToClient m_sender;

	// end region -> Fields

	// region Getters

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return m_message;
	}

	/**
	 * @return the sender thread.
	 */
	public ConnectionToClient getSender() {
		return m_sender;
	}

	// end region -> Getters

	// region Setters

	/**
	 * @param message
	 *            the message to set.
	 */
	public void setMessage(Message message) {
		m_message = message;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * 
	 * @see WaitingMessage
	 *
	 * @param client
	 *            the sender thread, this is unchangeable data.
	 * @param message
	 *            a message from client.
	 */
	public WaitingMessage(ConnectionToClient client, Message message) {
		m_message = message;
		m_sender = client;
	}

	// end region -> Constructors

}
