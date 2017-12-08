package connectivity;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import configurations.ConnectivityConfiguration;
import messages.IMessage;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 *
 * Server: Implementation of {@link AbstractServer} that can receive
 * {@link IMessage} type message.
 * 
 */
public class Server extends AbstractServer {

	// region Interfaces

	/**
	 *
	 * MessagesHandler: Describes handlers of {@link IMessage} messages.
	 * 
	 */
	public interface MessagesHandler {
		/**
		 * 
		 * Method called when {@link IMessage} type message received, there is option to
		 * answer to all clients.
		 *
		 * @param msg
		 *            The message from the client.
		 * @return Answer to the all clients, null if there is not one.
		 */
		IMessage onMessageReceived(IMessage msg);
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

		/**
		 * method called when the server is closed, this method always called after
		 * {@link ServerStatusHandler#onServerStopped()}.
		 */
		void onServerClosed();

	}

	// end region -> Interfaces

	// region Fields

	private int m_numOfConnectedClients;

	private ServerStatusHandler m_serverStatusHandler;

	private MessagesHandler m_messagesHandler;

	private Logger m_logger;

	private ConnectivityConfiguration m_configuration;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * Create Server instance, the listening on specific port.
	 *
	 * @param logger
	 *            The logger that the server will log to it.
	 * @param configuration
	 *            The server configuration
	 * @throws NullPointerException
	 *             If one of the received parameters is null.
	 */
	public Server(Logger logger, ConnectivityConfiguration configuration) throws NullPointerException {
		super(configuration.getPort());
		if (logger == null) {
			throw new NullPointerException("Logger is null!");
		}
		m_logger = logger;
		m_configuration = configuration;
		m_serverStatusHandler = null;
		m_numOfConnectedClients = 0;
	}

	// end region -> Constructors

	// region Getters

	/**
	 * @return The number of the connected clients right not.
	 */
	public int getNumberOfConnectedClients() {
		return m_numOfConnectedClients;
	}
	
	/**
	 * @return The configuration of the server.
	 */
	public ConnectivityConfiguration getConfiguration() {
		return m_configuration;
	}
	// end region -> Getters

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
		this.m_messagesHandler = messagesHandler;
	}

	// end region -> Setters

	// region AbstractServer Methods Override

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg == null) {
			m_logger.info("A null message has been received from client: " + client.toString());
			return;
		}

		if (!(msg instanceof IMessage)) {
			m_logger.info(
					"A message has been received from client, but the message type is not dirived from IMessage. The Client: "
							+ client.toString());
			return;
		}

		if (m_serverStatusHandler == null) {
			m_logger.info("A message has been received from client, but the handler is null. The Client: "
					+ client.toString() + " , Message: " + msg.toString());
			return;
		}
		IMessage receivedMsg = (IMessage) msg;
		IMessage answerMsg = m_messagesHandler.onMessageReceived(receivedMsg);

		if (answerMsg != null) {
			this.sendToAllClients(answerMsg);
		}

	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStarted() {
		if (m_serverStatusHandler != null) {
			m_logger.info("Server Started");
			m_serverStatusHandler.onServerStarted();
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStopped() {
		if (m_serverStatusHandler != null) {
			m_logger.info("Server Stopped");
			m_serverStatusHandler.onServerStopped();
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverClosed() {
		if (m_serverStatusHandler != null) {
			m_logger.info("Server Closed");
			m_serverStatusHandler.onServerClosed();
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		m_numOfConnectedClients++;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		m_numOfConnectedClients--;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
		m_logger.log(Level.WARNING, "Received exception on ConnectionToClient thread, the client: " + client.toString(),
				exception);
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void listeningException(Throwable exception) {
		if (isListening()) {
			m_logger.log(Level.WARNING, "Received exception in listening thread, but the thread is still running.",
					exception);
			return;
		}

		m_logger.info("Restarting the listening thread..");

		try {
			listen();
			return;
		} catch (IOException e) {
			m_logger.log(Level.SEVERE, "Restarting listening thread failed! closing server..", e);
		}

		try {
			close();
		} catch (IOException e) {
			m_logger.log(Level.SEVERE, "Closing server failed!", e);
		}

	}

	// end region -> AbstractServer Methods Override
}
