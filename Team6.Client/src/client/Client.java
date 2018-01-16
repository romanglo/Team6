
package client;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import logger.LogManager;
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

	// region Constants

	private static final int QUEUES_TIMEOUT_IN_MILLIS = 1000;

	// end region -> Constants

	// region Fields

	private Logger m_logger;

	private ClientStatusHandler m_clientStatusHandler;

	private MessageReceiveHandler m_messagesHandler;

	volatile private BlockingQueue<Message> m_incomingMessages;

	private IncomingMessagesConsumingTask m_incomingMessagesConsumingTask;

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
		m_messagesHandler = messagesHandler;
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
	 */
	public Client(Logger logger, String host, int port)
	{
		super(host, port);

		if (logger == null) {
			throw new NullPointerException("Logger is null!");
		}

		m_logger = logger;
		m_clientStatusHandler = null;
		m_messagesHandler = null;

		m_incomingMessages = new ArrayBlockingQueue<>(50);
		m_incomingMessagesConsumingTask = null;
	}

	/**
	 * A constructor for the connection with the server, take the configuration from
	 * {@link ClientConfiguration#getInstance()} and logger from
	 * {@link LogManager#getLogger()}.
	 * 
	 * @throws NullPointerException
	 *             if any problem with getting the configuration of the logger.
	 */
	public Client()
	{
		this(LogManager.getLogger(), ClientConfiguration.getInstance().getIp(),
				ClientConfiguration.getInstance().getPort());
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
	public void handleMessageFromServer(Object obj)
	{
		if (obj == null) {
			m_logger.info("A null message was received from server.");
			return;
		}

		if (!(obj instanceof Message)) {
			m_logger.info(
					"A message has been received from server, but the message type is not inherit from Message. Message type: "
							+ obj.getClass().getName());
			return;
		}

		try {
			Message msg = (Message) obj;

			if (m_incomingMessages.remainingCapacity() == 0) {
				Message removedMessage = m_incomingMessages.poll();
				m_logger.warning(
						"The message: " + removedMessage + ", from server removed because the incoming queue is full");
			}
			m_incomingMessages.put(msg);
		}
		catch (Exception e) {
			m_logger.warning("Some error occurred in adding a new message from client to incoming queue, exception: "
					+ e.getMessage());
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

	// region Protected Methods
	
	@Override
	protected void connectionEstablished()
	{
		m_logger.info("The connection with the server opened successfully.");

		m_incomingMessagesConsumingTask = new IncomingMessagesConsumingTask();
		m_incomingMessagesConsumingTask.start();

		if (m_clientStatusHandler != null) {
			m_clientStatusHandler.onClientConnected();
		}
		
		
	}
	
	@Override
	protected void connectionClosed()
	{
		m_logger.info("The connection with the server closed");

		if (m_incomingMessagesConsumingTask != null) {
			m_incomingMessagesConsumingTask.stopTask();
		}
		
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

	// end region -> Protected Methods
	
	// region Nested Classes

	/**
	 * IncomingMessagesConsumingTask: This thread takes message from incoming queue
	 * send to the messages handler and put them to the outgoing queue.
	 */
	private class IncomingMessagesConsumingTask extends Thread
	{

		boolean m_running = false;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			m_running = true;
			while (m_running) {
				try {
					Message incomingMessage = m_incomingMessages.poll(QUEUES_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);
					if (incomingMessage == null || m_messagesHandler == null) {
						continue;
					}
					m_messagesHandler.onMessageReceived(incomingMessage);
				}
				catch (InterruptedException e) {
					m_logger.severe("Message consuming thread interrupted! Exception: " + e.getMessage());
				}
				catch (Exception e) {
					m_logger.warning(
							"Some error occurred in handling an incoming message, exception: " + e.getMessage());
				}
			}
		}

		/**
		 * The method stop the consuming loop, the stopping will take less than
		 * {@value Client#QUEUES_TIMEOUT_IN_MILLIS} milliseconds.
		 */
		public void stopTask()
		{
			m_running = false;
		}
	}

	// end region - > Nested Classes
}
