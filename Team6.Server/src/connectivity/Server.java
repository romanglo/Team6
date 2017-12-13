package connectivity;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import messages.Message;
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
		 */
		Message onMessageReceived(Message msg);
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

	// end region -> Interfaces

	// region Fields

	private int m_numOfConnectedClients;

	private ServerStatusHandler m_serverStatusHandler;

	private MessagesHandler m_messagesHandler;

	private Logger m_logger;

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
		m_serverStatusHandler = null;
		m_messagesHandler = null;
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
		if (!(msg instanceof Message)) {
			m_logger.info(
					"A message has been received from client, but the message type is not dirived from Message. The Client: "
							+ client.toString());
			return;
		}

		Message receivedMsg = (Message) msg;

		if (m_serverStatusHandler == null) {
			m_logger.info("A message has been received from client, but the handler is null. The Client: "
					+ client.toString() + " , Message: " + msg.toString());
			return;
		} else {
			m_logger.info("A message has been received from client. The Client: " + client.toString() + " , Message: "
					+ msg.toString());
		}

		// Message entityMessage = MessagesFactory
		// .createGetEntityMessage(new ProductEntity(99, "Hello",
		// ProductType.FlowerPot));
		// try {
		// client.sendToClient((Object) entityMessage);
		// m_logger.info(entityMessage + " message to client: " + client.toString() + "
		// sent.");
		// } catch (IOException e) {
		// m_logger.log(Level.WARNING, entityMessage + " message to client: " +
		// client.toString() + " FAILED!.", e);
		// }

		Message answerMsg = m_messagesHandler.onMessageReceived(receivedMsg);

		if (answerMsg != null) {
			try {
				client.sendToClient(answerMsg);
			} catch (IOException e) {
				m_logger.log(Level.SEVERE, "Answering to the client failed, the client: " + client.toString(), e);
			}
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStarted() {
		if (m_serverStatusHandler != null) {
			m_serverStatusHandler.onServerStarted();
		}
		m_logger.info("The server started listen to port" + getPort());
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverStopped() {
		if (m_serverStatusHandler != null) {
			m_serverStatusHandler.onServerStopped();
		}
		m_logger.info("The server stopped listen");
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void serverClosed() {
		m_logger.info("The server closed");
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		m_numOfConnectedClients++;
		m_logger.info("The client connected: " + client.toString() + ", number of connected clients:"
				+ m_numOfConnectedClients);
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		m_numOfConnectedClients--;
		m_logger.info("The client connected: " + client.toString() + ", number of connected clients:"
				+ m_numOfConnectedClients);
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
		m_logger.log(Level.WARNING, "Received exception on ConnectionToClient thread, the client: " + client.toString()
				+ ", all the connections with clients closed", exception);
		m_numOfConnectedClients = 0;
		if (!isListening()) {
			serverStopped();
		}
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
		} else {
			serverStopped();
		}
	}

	// end region -> AbstractServer Methods Override
}
