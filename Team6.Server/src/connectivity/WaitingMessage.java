package connectivity;

import newMessages.Message;
import ocsf.server.ConnectionToClient;

class WaitingMessage {

	// region Fields

	private Message m_message;

	private ConnectionToClient m_sender;

	// end region -> Fields

	// region Getters

	public Message getMessage() {
		return m_message;
	}

	public ConnectionToClient getSender() {
		return m_sender;
	}

	// end region -> Getters

	// region Setters

	public void setMessage(Message message) {
		m_message = message;
	}

	// end region -> Setters

	// region Constructors

	public WaitingMessage(ConnectionToClient client, Message message) {
		m_message = message;
		m_sender = client;
	}

	// end region -> Constructors

}
