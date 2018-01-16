
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import logger.LogManager;

/**
 *
 * BaseController: an abstract base class which handling with not desirable
 * disconnection from server and controlling the GUI initialization process. In
 * addition the class contains useful method.
 * 
 * @see Initializable
 * @see client.Client.ClientStatusHandler
 * 
 */
public abstract class BaseController implements Initializable, Client.ClientStatusHandler
{

	// region Fields

	protected final static int RECONNECT_ATTEMPT_PERIOD_IN_SEC = 10;

	protected Logger m_Logger;

	protected Client m_Client;

	protected ClientConfiguration m_Configuration;

	private Timer m_timer;

	private int m_reconnectAttemptNumber;
	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * An empty constructor. Only exists to ensure reading by an extending class
	 * only.
	 *
	 */
	protected BaseController()
	{

	}

	// end region -> Constructors

	// region initialize Implementation Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void initialize(URL location, ResourceBundle resources)
	{
		m_Logger = LogManager.getLogger();
		m_Client = ApplicationEntryPoint.Client;
		m_Configuration = ApplicationEntryPoint.ClientConfiguration;

		m_Client.setClientStatusHandler(this);

		try {
			internalInitialize();
		}
		catch (Exception ex) {
			m_Logger.log(Level.SEVERE, "Initialization of the deriven class failed!", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * The method called when the controller initialized. The default implementation
	 * does nothing, it may be overridden by extending class.
	 */
	protected abstract void internalInitialize() throws Exception;

	// end region -> initialize Implementation Methods

	// region Client.ClientStatusHandler Implementation Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onClientConnected()
	{
		m_Logger.info("Reconnected to the server successfully");
		if (m_timer != null) {
			m_timer.cancel();
			m_timer = null;
		}

		try {
			onReconnectToServer();
		}
		catch (Exception ex) {
			m_Logger.severe(
					"onReconnectToServer method overrided by the deriven class and failed on execution! Exception: "
							+ ex.getMessage());
		}
	}

	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	protected void onReconnectToServer()
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		if (m_Client == null) {
			m_Logger.info("Disconnected from the server, unable to reconnect due to Client instance is null!");
		}

		m_Logger.info("Disconnected from the server, reconnect attempt will happen every "
				+ RECONNECT_ATTEMPT_PERIOD_IN_SEC + " seconds");
		m_timer = new Timer("ReconnectTimer", true);
		TimerTask timerTask = new ReconnectTimerTask();
		m_timer.scheduleAtFixedRate(timerTask, 0, RECONNECT_ATTEMPT_PERIOD_IN_SEC * 1000);
		try {
			onDisconnectFromServer();
		}
		catch (Exception ex) {
			m_Logger.severe(
					"onDisconnectFromServer method overrided by the deriven class and failed on executuin! Exception: "
							+ ex.getMessage());
		}
	}

	/**
	 * Hook method called after the connection has been closed due to an method
	 * calling or exception. The default implementation does nothing. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	private void onDisconnectFromServer() throws Exception
	{

	}

	// end region -> Client.ClientStatusHandler Implementation Methods

	// region Nested Classes

	class ReconnectTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			if (m_Client == null) {
				return;
			}
			if (!m_Client.createConnectionWithServer()) {
				m_reconnectAttemptNumber++;
				m_Logger.warning("Attempt number " + m_reconnectAttemptNumber
						+ " to connect to server failed! next attempt will be in " + RECONNECT_ATTEMPT_PERIOD_IN_SEC
						+ " seconds.");
			} else {
				m_reconnectAttemptNumber = 0;
			}
		}

	}

	// end region -> Nested Classes

	// region Methods

	/**
	 * 
	 * {@link BaseController} dispose methods, should be called on {@link Stage}
	 * closing sequence.
	 *
	 */
	public final void dispose()
	{

		try {
			if (m_Client != null) {
				m_Client.setClientStatusHandler(null);
			}
			if (m_timer != null) {
				m_timer.cancel();
				m_timer = null;
			}
			internalDispose();
			m_Logger.info("Base controller disposed successfully");
		}
		catch (Exception ex) {
			m_Logger.info("Base controller failed on try to dispose! Exception: " + ex.getMessage());
		}
	}

	/**
	 * Hook method called when the controller has been disposed. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	protected void internalDispose()
	{

	}

	// end region -> Public Methods
}
