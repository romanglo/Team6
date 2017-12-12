package connectivity;

import java.io.IOException;
import java.util.logging.Logger;

import entities.IEntity;
import messages.EntityData;
import messages.Message;
import ocsf.client.AbstractClient;

/**
 *
 * Client:
 * Implement connection to a sever and transfer data.
 * 
 */
public class Client extends AbstractClient {

	// region Fields

	private Logger m_logger;

	// end region -> Fields

	// region Getters
	// end region -> Getters

	// region Setters
	// end region -> Setters

	// region Constructors

	/**
	 * A constructor for the connection with the server.
	 * 
	 * @param logger -  Instance for the logging class.
	 * @param host - IP address.
	 * @param port - A port for the connection.
	 * @throws IOException when there is a problem with the connection.
	 */
	public Client(Logger logger, String host, int port) throws IOException {
		super(host, port);

		if (logger == null) {
			throw new NullPointerException("Logger is null!");
		}

		m_logger = logger;
		openConnection();
	}

	// end region -> Constructors

	// region Public Methods

	/**
	 * The method closes the connection with the server.
	 */
	public void close()
	{
		try
		{
			closeConnection();
		}
		catch(IOException e) {}
	}
	
	@Override
	public void handleMessageFromServer(Object msg) {
		IEntity entity = ((EntityData)((Message)msg).getMessageData()).getEntity();
		
		System.out.println(entity);
	}
	
	/**
	 * Method handles the event sent from the UI.
	 *
	 * @param data - Information sent from the UI.
	 */
	public void handleMessageFromClientUI(Object data) {
		try {
			sendToServer(data);
		}
		catch(IOException e) {
			m_logger.warning("Could not send message to server. Terminating connection.");
			close();
		}
	}

	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
