
package newControllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import logger.LogManager;
import newEntities.User;
import newMessages.Message;
import newMessages.MessagesFactory;

/**
 *
 * BaseController: an abstract base class which handling with not desirable
 * disconnection from server and controlling the GUI initialization process. In
 * addition the class contains useful method.
 * 
 * @see Initializable
 * @see client.Client.ClientStatusHandler
 * @see client.Client.MessageReceiveHandler
 */
public abstract class BaseController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{

	// region UI Fields

	private @FXML ImageView imageview_logo;

	private @FXML BorderPane borderpain_main_top;

	private @FXML VBox vbox_sidebar;

	private @FXML AnchorPane anchorpane_welcome;

	private @FXML ImageView imageview_welcome;

	private @FXML ImageView imageview_account_button;

	private @FXML ImageView imageview_logout_button;

	private @FXML Label label_username;

	private @FXML Circle circle_connection_status;

	// end region UI Fields

	// region Fields

	protected final static int RECONNECT_ATTEMPT_PERIOD_IN_SEC = 10;

	protected Logger m_Logger;

	protected Client m_Client;

	protected ClientConfiguration m_Configuration;

	protected User m_ConnectedUser;

	private Timer m_timer;

	private int m_reconnectAttemptNumber;

	private ToggleButton m_currentPressedButton = null;

	private Paint m_redPaint;

	private Paint m_greenPaint;

	private Tooltip m_connectedToolTip;

	private Tooltip m_disconnectedToolTip;

	// end region -> Fields

	// region Getters

	/**
	 * @return the pressed button in side bar text that received in
	 *         {@link BaseController#getSideButtonsNames()}, if never pressed button
	 *         the method will return <code>null</code>.
	 */
	protected String getPressedButton()
	{
		if (m_currentPressedButton == null) {
			return null;
		}
		return m_currentPressedButton.getText();
	}

	// end region -> Getters

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
		m_ConnectedUser = ApplicationEntryPoint.ConnectedUser;

		m_Client.setClientStatusHandler(this);
		m_Client.setMessagesHandler(this);
		if (!m_Client.isConnected()) {
			onClientDisconnected();
		}

		try {
			initalizeBaseWindow();
			internalInitialize();
		}
		catch (Exception ex) {
			m_Logger.log(Level.SEVERE, "Initialization of the deriven class failed!", ex);
			throw new RuntimeException(ex);
		}
	}

	private void initalizeBaseWindow()
	{
		initializeContent();
		initializeTopBar();
		initializeSideBar();
		initializeBottomBar();
	}

	// end region -> initialize Implementation Methods

	// region Base Window Methods

	private void initializeBottomBar()
	{
		m_redPaint = Paint.valueOf("red");
		m_greenPaint = Paint.valueOf("green");
		m_connectedToolTip = new Tooltip();
		m_connectedToolTip.setText("Connected to server.");
		m_disconnectedToolTip = new Tooltip();
		m_disconnectedToolTip.setText("Disonnected from server,\ntrying to reconnect every 10 seconds.");
		setConnectionCircleStatus(m_Client.isConnected());

	}

	private void setConnectionCircleStatus(boolean connected)
	{
		Platform.runLater(() -> {
			if (connected) {
				circle_connection_status.setFill(m_greenPaint);
				Tooltip.install(circle_connection_status, m_connectedToolTip);
			} else {
				circle_connection_status.setFill(m_redPaint);
				Tooltip.install(circle_connection_status, m_disconnectedToolTip);
			}
		});
	}

	private void initializeSideBar()
	{
		ObservableList<Node> children = vbox_sidebar.getChildren();

		String[] buttons = getSideButtonsNames();
		for (String button : buttons) {
			ToggleButton toggleButton = new ToggleButton();
			toggleButton.setText(button);
			toggleButton.setOnAction((action) -> onSideBarButtonPressed(action));
			children.add(toggleButton);
		}
	}

	private void initializeTopBar()
	{
		InputStream account = getClass().getResourceAsStream("/newBoundaries/images/account.png");
		if (account != null) {
			Image accountImage = new Image(account);
			imageview_account_button.setImage(accountImage);
		}

		InputStream logout = getClass().getResourceAsStream("/newBoundaries/images/logout.png");
		if (logout != null) {
			Image logoutImage = new Image(logout);
			imageview_logout_button.setImage(logoutImage);
		}
	}

	private void initializeContent()
	{
		InputStream tulips = getClass().getResourceAsStream("/newBoundaries/images/tulips.jpg");
		if (tulips != null) {
			Image image = new Image(tulips);
			imageview_welcome.setImage(image);
		}

		label_username.setText(m_ConnectedUser.getUserName());
	}

	private void onSideBarButtonPressed(ActionEvent event)
	{
		Object source = event.getSource();
		if (!(source instanceof ToggleButton)) {
			m_Logger.warning("Receiven onAction event of the side bar not from toggle button! Received type: "
					+ source.getClass().getName());
			return;
		}
		ToggleButton pressedButton = (ToggleButton) source;

		String selection = pressedButton.getText();

		if (m_currentPressedButton == null) {

			if (!onSelection(selection)) {
				m_Logger.info("Selection denied by the implemanting class. The Selection: " + selection);
				pressedButton.setSelected(false);
				return;
			}
			anchorpane_welcome.setVisible(false);
			m_currentPressedButton = pressedButton;
			return;
		}

		if (pressedButton == m_currentPressedButton) {
			pressedButton.setSelected(true);
			return;
		}

		if (!onSelection(selection)) {
			m_Logger.info("Selection denied by the implemanting class. The Selection: " + selection);
			pressedButton.setSelected(false);
			return;
		}

		for (Node node : vbox_sidebar.getChildren()) {
			if (!(node instanceof ToggleButton) || node == pressedButton) {
				continue;
			}
			((ToggleButton) node).setSelected(false);
		}

		pressedButton.setSelected(true);
		m_currentPressedButton = pressedButton;
	}

	@FXML
	private void onLogoutButtonPressed(ActionEvent event)
	{
		try {
			if (m_ConnectedUser != null) {
				Message logoutMessage = MessagesFactory.createLogoutMessage(m_ConnectedUser.getUserName(),
						m_ConnectedUser.getPassword());
				m_Client.sendMessageToServer(logoutMessage);
			}
			ApplicationEntryPoint.ConnectedUser = null;
			m_Client.setMessagesHandler(null);
			m_Client.setClientStatusHandler(null);
			m_Client.closeConnection();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/newBoundaries/Login.fxml"));
			Parent root = (Parent) loader.load();
			LoginController controller = (LoginController) loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/newBoundaries/login.css").toExternalForm());
			Stage loginStage = new Stage();
			loginStage.setScene(scene);
			loginStage.setWidth(700);
			loginStage.setHeight(500);
			loginStage.setTitle("Zer-Li");
			loginStage.setResizable(false);
			controller.intializeKeyHandler(scene);
			loginStage.show();
		}
		catch (Exception ex) {
			m_Logger.severe("Failed on try to log out, the application will shut down. exception: " + ex.getMessage());
			showInformationMessage("Error!\nFailed on try to logout, the aplication shutting down.. ");
		}

		Stage stage = (Stage) borderpain_main_top.getScene().getWindow();
		if (stage != null) {
			stage.close();
		} else {
			borderpain_main_top.getScene().getWindow().hide();
		}
	}

	@FXML
	private void onAccountButtonPressed(ActionEvent event)
	{
		try {
			Stage currentStage = (Stage) borderpain_main_top.getScene().getWindow();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/newBoundaries/UserDetails.FXML"));
			Parent parent = (Parent) fxmlLoader.load();
			FadeTransition ft = new FadeTransition(Duration.seconds(1.5), parent);
			ft.setFromValue(0.0);
			ft.setToValue(1.0);
			ft.play();
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/newBoundaries/application.css").toExternalForm());
			Stage nextStage = new Stage();
			nextStage.setScene(scene);
			nextStage.setResizable(false);
			nextStage.initModality(Modality.WINDOW_MODAL);
			nextStage.initStyle(StageStyle.DECORATED);
			nextStage.initOwner(currentStage);
			nextStage.showAndWait();
		}
		catch (Exception e) {
			m_Logger.severe("Failed on try to load the user details window, excepion: " + e.getMessage());
			showInformationMessage("Your account details could not be loaded at this time..");
		}
	}

	// end region -> Base Controller Methods

	// region Client.ClientStatusHandler Implementation Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onClientConnected()
	{
		m_Logger.info("Reconnected to the server successfully");
		setConnectionCircleStatus(true);
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
	 * {@inheritDoc}
	 */
	@Override
	public final void onClientDisconnected()
	{
		setConnectionCircleStatus(false);

		if (m_Client == null) {
			m_Logger.info("Disconnected from the server, unable to reconnect due to Client instance is null!");
			return;
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
	// end region -> Client.ClientStatusHandler Implementation Methods

	// region Public & Protected Methods

	/**
	 * 
	 * {@link BaseController} dispose methods, should be called on {@link Stage}
	 * closing sequence.
	 *
	 */
	public final void dispose()
	{

		try {
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

	protected void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	// end region -> Public Methods

	// region Abstract Methods

	/**
	 * The method called when the controller initialized. The default implementation
	 * does nothing, it may be overridden by extending class.
	 */
	protected abstract void internalInitialize() throws Exception;

	/**
	 * 
	 * This method invoked when any button pressed in the side bar.
	 *
	 * @param title
	 *            the name of the selected option, as received in
	 *            {@link BaseController#getSideButtonsNames}.
	 * @return <code>true</code> if the selection approved and <code>false</code> if
	 *         does not.
	 */
	protected abstract boolean onSelection(String title);

	/**
	 * 
	 * This method implemented by inherit class, and get the names of the options at
	 * the side bar.
	 *
	 * @return An array of the name of the option at the side bar.
	 */
	protected abstract String[] getSideButtonsNames();

	// end region -> Abstract Methods

	// region Optional Event Methods

	/**
	 * Event method called after a connection has been established. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	protected void onReconnectToServer()
	{

	}

	/**
	 * Event method called after the connection has been closed due to an method
	 * calling or exception. The default implementation does nothing. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	protected void onDisconnectFromServer() throws Exception
	{

	}

	/**
	 * Event method called when the controller has been disposed. The default
	 * implementation does nothing, it may be overridden by extending class.
	 */
	protected void internalDispose()
	{

	}

	// end region -> Optional Event Methods

	// region Nested Classes

	/**
	 * 
	 * ReconnectTimerTask: An extend of {@link TimerTask} that related to the
	 * reconnection attempts.
	 *
	 */
	private class ReconnectTimerTask extends TimerTask
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
}
