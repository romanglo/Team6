
package client;

import java.io.IOException;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import newMessages.Message;
import ocsf.client.AbstractClient;

/**
 *
 * Client: Implement connection to a sever and transfer data.
 * 
 */
public class Client extends AbstractClient
{

	/**
	 *
	 * MessageReceiveHandler: Handler interface for message from the server to
	 * display on UI.
	 * 
	 */
	public interface MessageReceiveHandler
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
		 * @see {@link Message} the received type.
		 */
		void onMessageReceived(Message msg) throws Exception;
	}

	/**
	 *
	 * ClientStatusHandler: handler interface for updating the UI according to the
	 * connection status of the client.
	 * 
	 */
	public interface ClientStatusHandler
	{

		/**
		 * Update the UI after the connection to the server, this method called from
		 * exception unsafe scope.
		 *
		 */
		void onClientConnected();

		/**
		 * Update the UI after the disconnection from the server, this method called
		 * from exception unsafe scope.
		 *
		 */
		void onClientDisconnected();
	}

	// region Fields

	private Logger m_logger;

	private ClientStatusHandler m_clientStatusHandler;

	private MessageReceiveHandler m_messageReceiveHandler;

	// end region -> Fields

	// region Setters

	/**
	 * @param clientStatusHandler
	 *            Handler of changing in client status, if a handler exist the new
	 *            one will swap him. <code>null</code> will remove the current
	 *            handler.
	 */
	public void setClientStatusHandler(@Nullable ClientStatusHandler clientStatusHandler)
	{
		m_clientStatusHandler = clientStatusHandler;
	}

	/**
	 * @param messagesHandler
	 *            Handler of messages, if a handler exist the new one will swap him.
	 *            <code>null</code> will remove the current handler.
	 */
	public void setMessagesHandler(@Nullable MessageReceiveHandler messagesHandler)
	{
		this.m_messageReceiveHandler = messagesHandler;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * A constructor for the connection with the server.
	 * 
	 * @param logger
	 *            - Instance for the logging class.
	 * @param host
	 *            - IP address.
	 * @param port
	 *            - A port for the connection.
	 * @throws IOException
	 *             when there is a problem with the connection.
	 */
	public Client(Logger logger, String host, int port) throws IOException
	{
		super(host, port);

		if (logger == null) {
			throw new NullPointerException("Logger is null!");
		}

		m_logger = logger;
		m_clientStatusHandler = null;
		m_messageReceiveHandler = null;
	}

	// end region -> Constructors

	// region Public Methods

	/**
	 * Open connection with the server.
	 * 
	 * @return <code>true</code> if the connection opened successfully and
	 *         <code>false</code> otherwise.
	 *
	 */
	public boolean createConnectionWithServer()
	{
		try {
			openConnection();
		}
		catch (IOException e) {
			m_logger.warning("Failed to open connection! host = " + getHost() + ", port = " + getPort() + ", exception:"
					+ e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The method closes the connection with the server.
	 */
	public void closeConnectionWithServer()
	{
		try {
			closeConnection();
			m_logger.info("The connection with the server closed.");
		}
		catch (IOException e) {
			m_logger.warning("Could not close the connection with the server. Exception:" + e.getMessage());
		}
	}

	@Override
	public void handleMessageFromServer(Object msg)
	{
		if (msg == null) {
			m_logger.info("A null message was received from server.");
			return;
		}

		if (!(msg instanceof Message)) {
			m_logger.info("A message was received from the server, but the message type is not dirived from Message");
			return;
		}

		if (m_messageReceiveHandler == null) {
			m_logger.info(
					"A message received from the server, but does not registered a handler so the message is thrown out");
		} else {
			m_logger.info("A message received from the server.");
		}
		Message message = (Message) msg;
		try {
			m_messageReceiveHandler.onMessageReceived(message);
		}
		catch (Exception ex) {
			m_logger.warning("Message handler failed!. Exception: " + ex.getMessage());
		}
	}

	@Override
	protected void connectionClosed()
	{
		m_logger.info("The connection with the server closed");

		if (m_clientStatusHandler != null) {
			m_clientStatusHandler.onClientDisconnected();
		}
	}

	@Override
	protected void connectionException(Exception exception)
	{
		m_logger.severe("Connection exception: " + exception.getMessage());

		// if (!isConnected()) {
		// connectionClosed();
		// }

		/*
		 * If the exception happens because a disconnection from the server, the method
		 * isConnected() still returns true due to an unknown reason. So on condition
		 * that an error is occurred, always happened a logout from the server.
		 */
		connectionClosed();
	}

	@Override
	protected void connectionEstablished()
	{
		m_logger.info("The connection with the server opened successfully.");

		if (m_clientStatusHandler != null) {
			m_clientStatusHandler.onClientConnected();
		}
	}

	/**
	 * Method send message to server.
	 *
	 * @param data
	 *            - Information sent from the UI.
	 * @return true if the sending succeed and false if does not.
	 */
	public boolean sendMessageToServer(Object data)
	{
		boolean returningValue = true;
		try {
			sendToServer(data);
		}
		catch (IOException e) {
			m_logger.warning("Could not send message to server, exception: " + e.getMessage());
			returningValue = false;
		}
		return returningValue;
	}

	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
