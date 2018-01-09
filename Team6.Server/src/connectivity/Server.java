package connectivity;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import configurations.ServerConfiguration;
import logger.LogManager;
import newMessages.Message;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 *
 * Server: Implementation of {@link AbstractServer} that can receive
 * {@link Message} type message.
 * 
 */
public class Server extends AbstractServer {

	// region Interfaces

	/**
	 *
	 * MessagesHandler: Describes handlers of {@link Message} messages.
	 * 
	 */
	public interface MessagesHandler {
		/**
		 * 
		 * Method called when {@link Message} type message received, there is option to
		 * answer to the client.
		 *
		 * @param msg
		 *            The message from the client.
		 * @return Answer to the client, null to do nothing.
		 * @throws Exception
		 *             The method can throw any kind of exception, this method call
		 *             surround with try/catch.
		 */
		Message onMessageReceived(Message msg) throws Exception;
	}

	/**
	 * 
	 *
	 * ServerStatusHandler: Describe handlers of {@link Server} connection status
	 * changes.
	 *
	 */
	public interface ServerStatusHandler {

		/**
		 * Method called when the server starts listening for connections.
		 */
		void onServerStarted();

		/**
		 * Method called when the server stops accepting connections.
		 */
		void onServerStopped();

	}

	/**
	 * ClientConnectionHandler: Describe handlers of {@link Server} clients
	 * connection status changes.
	 *
	 */
	public interface ClientConnectionHandler {

		/**
		 * Method called when the server created new connection with client.
		 * 
		 * @param clientDetails
		 *            The details of the connected client.
		 * @param numberOfConnectedClients
		 *            The number of the connected clients after the connection of the
		 *            new client.
		 */
		void onClientConnected(String clientDetails, int numberOfConnectedClients);

		/**
		 * Method called when the server stops accepting connections.
		 * 
		 * @param clientDetails
		 *            The details of the disconnected client.
		 * @param numberOfConnectedClients
		 *            The number of the connected clients after the disconnection of the
		 *            a client.
		 */
		void onClientDisconnected(String clientDetails, int numberOfConnectedClients);

	}
	// end region -> Interfaces

	// region Constants

	private static final int QUEUES_TIMEOUT_IN_MILLIS = 1000;

	// end region -> Constants

	// region Fields

	private ClientConnectionHandler m_clientConnectionHandler;

	private ServerStatusHandler m_serverStatusHandler;

	volatile private MessagesHandler m_messagesHandler;

	private Logger m_logger;

	volatile private BlockingQueue<WaitingMessage> m_incomingMessages;

	volatile private BlockingQueue<WaitingMessage> m_outgoingMessages;

	private IncomingMessagesConsumingTask m_incomingMessagesConsumingTask;

	private OutgoingMessagesConsumingTask m_outgoingMessagesConsumingTask;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * Create Server instance, the listening on specific port.
	 *
	 * @param logger
	 *            The logger that the server will log to it.
	 * @param port
	 *            - A port for the connection.
	 * @throws NullPointerException
	 *             If one of the received parameters is null.
	 */
	public Server(@NotNull Logger logger, int port) throws NullPointerException {
		super(port);
		if (logger == null) {
			throw new NullPointerException("Logger is null!");
		}

		m_logger = logger;
		m_clientConnectionHandler = null;
		m_serverStatusHandler = null;
		m_messagesHandler = null;

		m_incomingMessages = new ArrayBlockingQueue<>(50);
		m_outgoingMessages = new ArrayBlockingQueue<>(50);

	}

	/**
	 * 
	 * Create Server instance, getting the port from
	 * {@link ServerConfiguration#getInstance()} and logger from
	 * {@link LogManager#getLogger()}.
	 *
	 * @throws NullPointerException
	 *             If one of the received parameters is null.
	 */
	public Server() throws NullPointerException {
		this(LogManager.getLogger(), ServerConfiguration.getInstance().getConnectivityConfiguration().getPort());
	}
	// end region -> Constructors

	// region Setters

	/**
	 * @param serverStatusHandler
	 *            Handler of changing in server status, if a handler exist the new
	 *            one will swap him. <code>null</code> will remove the current
	 *            handler.
	 */
	public void setServerActionHandler(@Nullable ServerStatusHandler serverStatusHandler) {
		m_serverStatusHandler = serverStatusHandler;
	}

	/**
	 * @param messagesHandler
	 *            Handler of messages, if a handler exist the new one will swap him.
	 *            <code>null</code> will remove the current handler.
	 */
	public void setMessagesHandler(@Nullable MessagesHandler messagesHandler) {
		m_messagesHandler = messagesHandler;
	}

	/**
	 * @param clientConnectionHandler
	 *            Handler of changes in client connections, if a handler exist the
	 *            new one will swap him. <code>null</code> will remove the current
	 *            handler.
	 */
	public void setClientConnectionHandler(@Nullable ClientConnectionHandler clientConnectionHandler) {
		m_clientConnectionHandler = clientConnectionHandler;
	}

	// end region -> Setters

	// region AbstractServer Methods Override

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void handleMessageFromClient(Object obj, ConnectionToClient client) {

		String clientDetails = client == null ? "null" : client.toString();
		if (obj == null) {
			m_logger.info("A null message has been received from client: " + clientDetails);
			return;
		}
		if (!(obj instanceof Message)) {
			m_logger.info(
					"A message has been received from client, but the message type is not dirived from Message. The Client: "
							+ clientDetails);
			return;
		}

		try {
			Message msg = (Message) obj;
			WaitingMessage receivedMessage = new WaitingMessage(client, msg);

			if (m_incomingMessages.remainingCapacity() == 0) {
				WaitingMessage removedMessage = m_outgoingMessages.poll();
				m_logger.warning("The message: " + removedMessage.getMessage() + ", from client: "
						+ removedMessage.getSender() + " removed because the incoming queue is full");
			}
			m_incomingMessages.put(receivedMessage);
		} catch (Exception e) {
			m_logger.warning("Some error occured in adding a new message from client to incoming queue, exception: "
					+ e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStarted() {
		if (m_serverStatusHandler != null) {
			try {
				m_serverStatusHandler.onServerStarted();
			} catch (RuntimeException e) {
				m_logger.warning("An exception occured in server status handler, exception: " + e.getMessage());
			}
		}

		m_incomingMessagesConsumingTask = new IncomingMessagesConsumingTask();
		m_outgoingMessagesConsumingTask = new OutgoingMessagesConsumingTask();

		m_incomingMessagesConsumingTask.start();
		m_outgoingMessagesConsumingTask.start();

		m_logger.info("The server started listen to port " + getPort());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStopped() {
		if (m_incomingMessagesConsumingTask != null) {
			m_incomingMessagesConsumingTask.stopTask();
		}
		if (m_outgoingMessagesConsumingTask != null) {
			m_outgoingMessagesConsumingTask.stopTask();
		}

		if (m_serverStatusHandler != null) {
			try {
				m_serverStatusHandler.onServerStopped();
			} catch (RuntimeException e) {
				m_logger.warning("An exception occured in server status handler, exception: " + e.getMessage());
			}
		}
		m_logger.info("The server stopped listen");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serverClosed() {
		m_logger.info("The server closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		int numberOfClients = getNumberOfClients();
		m_logger.info(
				"The client connected: " + client.toString() + ", number of connected clients:" + numberOfClients);

		if (m_clientConnectionHandler != null) {
			m_clientConnectionHandler.onClientConnected(client.toString(), numberOfClients);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		int numberOfClients = getNumberOfClients() - 1; // Decreased because it happen only after this method in super
														// class.

		m_logger.info(
				"The client connected: " + client.toString() + ", number of connected clients:" + numberOfClients);

		if (m_clientConnectionHandler != null) {
			String clientDetails = client == null ? "null" : client.toString();
			m_clientConnectionHandler.onClientDisconnected(clientDetails, numberOfClients);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
		String clientDetails = client == null ? "null" : client.toString();
		m_logger.warning("Received exception on ConnectionToClient thread! Failed client: " + clientDetails
				+ ", exception: " + exception.getMessage());
		if (!isListening()) {
			serverStopped();
		}

		clientDisconnected(client);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void listeningException(Throwable exception) {
		if (isListening()) {
			m_logger.warning(
					"Received exception in listening thread, but the thread is still running. Exception: " + exception);
			return;
		} else {
			serverStopped();
		}
	}

	// end region -> AbstractServer Methods Override

	// region Nested Classes

	/**
	 * IncomingMessagesConsumingTask: This thread takes message from incoming queue
	 * send to the messages handler and put them to the outgoing queue.
	 */
	private class IncomingMessagesConsumingTask extends Thread {

		boolean m_running = false;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			m_running = true;
			while (m_running) {
				try {
					WaitingMessage incomingMessage = m_incomingMessages.poll(QUEUES_TIMEOUT_IN_MILLIS,
							TimeUnit.MILLISECONDS);
					if (incomingMessage == null || m_messagesHandler == null) {
						continue;
					}

					Message onMessageReceived = m_messagesHandler.onMessageReceived(incomingMessage.getMessage());
					if (onMessageReceived == null) {
						continue;
					}
					incomingMessage.setMessage(onMessageReceived);
					if (m_outgoingMessages.remainingCapacity() == 0) {
						WaitingMessage removedMessage = m_outgoingMessages.poll();
						m_logger.warning("The message: " + removedMessage.getMessage() + ", from client: "
								+ removedMessage.getSender() + " removed because the outgoing queue is full");
					}
					m_outgoingMessages.put(incomingMessage);

				} catch (InterruptedException e) {
					m_logger.severe("Message consuming thread interrupted! Exception: " + e.getMessage());
				} catch (Exception e) {
					m_logger.warning(
							"Some error occured in handeling an incoming message, exception: " + e.getMessage());
				}
			}
		}

		/**
		 * The method stop the consuming loop, the stopping will take less than
		 * {@value Server#QUEUES_TIMEOUT_IN_MILLIS} milliseconds.
		 */
		public void stopTask() {
			m_running = false;
		}
	}

	/**
	 * IncomingMessagesConsumingTask: This thread takes message from incoming queue
	 * send to the messages handler and put them to the outgoing queue.
	 */
	private class OutgoingMessagesConsumingTask extends Thread {

		boolean m_running = false;

		@Override
		public void run() {
			m_running = true;
			while (m_running) {
				String clientDetails = "";
				try {

					WaitingMessage outgoingMessage = m_outgoingMessages.poll(QUEUES_TIMEOUT_IN_MILLIS,
							TimeUnit.MILLISECONDS);
					if (outgoingMessage == null || m_messagesHandler == null) {
						continue;
					}
					ConnectionToClient sender = outgoingMessage.getSender();
					if (sender == null) {
						continue;
					}
					clientDetails = sender.toString();
					if (!sender.isAlive()) {
						m_logger.warning("The client: " + sender.toString()
								+ " logged off until the his message reached the top of the queue");
						continue;
					}

					Message message = outgoingMessage.getMessage();
					if (message != null) {
						sender.sendToClient(message);
					}
				} catch (InterruptedException e) {
					m_logger.severe("Message consuming thread interrupted! Exception: " + e.getMessage());
				} catch (IOException e) {
					m_logger.warning("Some error occured in sending a message to client: " + clientDetails
							+ ", exception: " + e.getMessage());
				}
			}
		}

		/**
		 * The method stop the consuming loop, the stopping will take less than
		 * {@value Server#QUEUES_TIMEOUT_IN_MILLIS} milliseconds.
		 */
		public void stopTask() {
			m_running = false;
		}
	}
	// end region - > Nested Classes
}
