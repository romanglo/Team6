
package connectivity;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import messages.Message;
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
		 *            - An Object received from the server.
		 */
		void onMessageReceived(Object msg);
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
		 * Update the UI after the connection to the server.
		 *
		 */
		void onClientConnected();

		/**
		 * Update the UI after the disconnection from the server.
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
	 */
	public void createConnectionWithServer()
	{
		try {
			openConnection();
		}
		catch (IOException e) {
			m_logger.log(Level.WARNING, "Failed to open connection.", e);
		}
	}

	/**
	 * The method closes the connection with the server.
	 */
	public void close()
	{
		try {
			closeConnection();
			m_logger.info("The connection with the server closed.");
		}
		catch (IOException e) {
			m_logger.warning("Could not close the connection with the server.");
		}
	}

	@Override
	public void handleMessageFromServer(Object msg)
	{
		if (msg == null) {
			m_logger.info("A null message was received from server.");
			return;
		}

		if (msg instanceof String) {
			System.out.println("Message received is : " + msg);
			return;
		}

		if (!(msg instanceof Message)) {
			m_logger.info("A message was received from the server, but the message type is not dirived from Message.");
			return;
		}

		m_logger.info("A message received from the server.");

		try {
			m_messageReceiveHandler.onMessageReceived(msg);
			m_logger.info("Displayed data from server.");
		}
		catch (Exception ex) {
			m_logger.log(Level.WARNING, "Error! Failed displaying the data received. Exception: ", ex);
		}
	}

	@Override
	protected void connectionClosed()
	{
		m_clientStatusHandler.onClientDisconnected();
	}

	@Override
	protected void connectionException(Exception exception)
	{
		m_logger.log(Level.WARNING, "Connection exception: ", exception);
		if (!isConnected()) {
			connectionClosed();
		}
	}

	@Override
	protected void connectionEstablished()
	{
		m_clientStatusHandler.onClientConnected();
	}

	/**
	 * Method handles the event sent from the UI.
	 *
	 * @param data
	 *            - Information sent from the UI.
	 */
	public void handleMessageFromClientUI(Object data)
	{
		try {
			sendToServer(data);
		}
		catch (IOException e) {
			m_logger.warning("Could not send message to server. Terminating connection.");
			close();
		}
	}

	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
