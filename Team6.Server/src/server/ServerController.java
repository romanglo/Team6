package server;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ConnectivityConfiguration;
import configurations.DbConfiguration;
import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import db.MessagesResolver;
import db.QueryGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import logger.LogManager;

/**
 *
 * ServerController: Server GUI controller.
 * 
 */
public class ServerController implements Initializable, Server.ServerStatusHandler, Server.ClientConnectionHandler {
	// region UI Elements

	private @FXML TabPane tabpane;

	/* Database start-stop button declaration */
	private @FXML Button btn_db_start;
	private @FXML Button btn_db_stop;

	/* Database status declaration */
	private @FXML Circle circle_db_on;
	private @FXML Circle circle_db_off;

	/* Connectivity start-stop button declaration */

	private @FXML Button btn_connectivity_start;
	private @FXML Button btn_connectivity_stop;

	/* Connectivity status declaration */
	private @FXML Circle circle_connectivity_on;
	private @FXML Circle circle_connectivity_off;
	private @FXML Label label_numberOfConnectedUsers;

	/* Log text view declaration */
	private @FXML TextArea textarea_log;

	/* Setting table declaration */
	private @FXML TableView<SettingsRow> setting_table;
	private @FXML TableColumn<SettingsRow, String> tablecolumn_setting;
	private @FXML TableColumn<SettingsRow, String> tablecolumn_value;
	private @FXML TableColumn<SettingsRow, String> tablecolumn_region;

	/* Title images */
	private @FXML ImageView imageview_gif;
	private @FXML Button btn_run_logger_file;

	// end region -> UI Elements

	// region Fields

	private DbController m_dbContoller;

	private Server m_server;

	private ServerConfiguration m_configuration;

	private Logger m_logger;

	private MessagesResolver m_messageResolver;

	private boolean m_resetUserStatus;

	private final static DateTimeFormatter s_dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	// end region -> Fields

	// region Initializable Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeImages();

		initializeFields();

		initializeServerLogic();

		initializeConfigurationTable();
	}

	/**
	 * This method initialize all the images in {@link ServerController}.
	 */
	private void initializeImages() {
		InputStream serverGif = getClass().getResourceAsStream("ServerGIF.gif");
		if (serverGif != null) {
			Image image = new Image(serverGif);
			imageview_gif.setImage(image);
		}
	}

	/**
	 * The method initialize {@link ServerController#setting_table} and set
	 * necessary triggers.
	 */
	private void initializeConfigurationTable() {
		setting_table.setRowFactory(param -> {
			TableRow<SettingsRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					SettingsRow rowData = tableRow.getItem();

					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Update Settings Value");
					dialog.setHeaderText("Do you want to update the value of " + rowData.getType() + '-'
							+ rowData.getSetting() + " ?");
					dialog.setContentText("Please enter the new value:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()) {
						return;
					}
					String resultString = result.get();
					if (!(resultString != null && !resultString.equals(rowData.getValue()))) {
						return;
					}
					if (rowData.getType().equals(ConnectivityConfiguration.CONFIGURATION_TYPE_NAME)) {
						int port;
						try {
							port = Integer.parseInt(resultString);
							m_configuration.getConnectivityConfiguration().setPort(port);
							m_server.setPort(port);
							if (m_server.isListening()) {
								showAlertMessage(
										"Attention: You must reopen application connection for the changes to take effect!");
							}
						} catch (NumberFormatException e) {
							return;
						}
					} else {
						m_configuration.getDbConfiguration().updateValueByName(rowData.getSetting(), resultString);
						if (m_dbContoller.isRunning()) {
							showAlertMessage(
									"Attention: You must reopen database connection for the changes to take effect!");
						}
					}
					rowData.setValue(resultString);
					drawContantToTable();
					String msg = "The Configuration " + rowData.getType() + '-' + rowData.getSetting()
							+ " value changed to " + rowData.getValue();
					m_logger.info(msg);
					addMessageToLog(msg);

				}
			});
			return tableRow;
		});
		tablecolumn_region.setCellValueFactory(new PropertyValueFactory<>("type"));
		tablecolumn_setting.setCellValueFactory(new PropertyValueFactory<>("setting"));
		tablecolumn_value.setCellValueFactory(new PropertyValueFactory<>("value"));

		drawContantToTable();
	}

	/**
	 * Initialize {@link ServerController} internal logic.
	 */
	private void initializeServerLogic() {
		m_server.setServerActionHandler(this);
		m_server.setClientConnectionHandler(this);

		m_messageResolver = new MessagesResolver(m_dbContoller);

		m_server.setMessagesHandler(m_messageResolver);
	}

	/**
	 * Initialize {@link ServerController} fields.
	 */
	private void initializeFields() {
		m_dbContoller = ApplicationEntryPoint.DbContoller;
		m_server = ApplicationEntryPoint.Server;
		m_configuration = ServerConfiguration.getInstance();
		m_logger = LogManager.getLogger();
		m_resetUserStatus = false;
	}

	// end region -> Initializable Implementation

	// region UI Methods

	/**
	 * 
	 * The method called on click of {@link ServerController#btn_db_start} and try
	 * to open connection with the DB.
	 *
	 * @param event
	 *            the trigger event.
	 */
	private @FXML void startDb(ActionEvent event) {
		try {
			addMessageToLog("Trying to connect to the database with the configuration: "
					+ m_configuration.getDbConfiguration().toString());
			m_dbContoller.Start();
			btn_db_start.setDisable(true);
			btn_db_stop.setDisable(false);
			circle_db_on.setFill(Paint.valueOf("green"));
			circle_db_off.setFill(Paint.valueOf("grey"));
		} catch (Exception e) {
			addMessageToLog("Failed to disconnect from database, exception : " + e.getMessage());
			m_logger.log(Level.SEVERE, "Failed to connect to database", event);
			return;
		}
		if (m_resetUserStatus) {
			resetUsersStatus();
		}
		addMessageToLog("Connected successfully to the database");
	}

	/**
	 * 
	 * The method called on click of {@link ServerController#btn_db_stop} and try to
	 * close connection with the DB.
	 *
	 * @param event
	 *            the trigger event.
	 */
	private @FXML void stopDb(ActionEvent event) {
		try {
			addMessageToLog("Trying to disconnect from the database");
			m_dbContoller.Stop();
			btn_db_start.setDisable(false);
			btn_db_stop.setDisable(true);
			circle_db_on.setFill(Paint.valueOf("grey"));
			circle_db_off.setFill(Paint.valueOf("red"));

		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Failed to disconnect from database", event);
			addMessageToLog("Failed to disconnect from database, exception : " + e.getMessage());
			return;
		}
		addMessageToLog("Disconnected successfully from the database");
	}

	/**
	 * 
	 * The method called on click of {@link ServerController#btn_connectivity_start}
	 * and try to open {@link Server} connection.
	 *
	 * @param event
	 *            the trigger event.
	 */
	private @FXML void startConnectivity(ActionEvent event) {
		String ip;
		try {
			ip = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException ignored) {
			ip = "localhost";
		}
		String msg = "Trying to open TCP\\IP connection, IP : " + ip + ", port: " + m_server.getPort();
		m_logger.info(msg);
		addMessageToLog(msg);
		try {
			m_server.listen();
		} catch (Exception e) {
			msg = "Failed on try to open TCP\\IP connection";
			m_logger.log(Level.SEVERE, msg, e);
			addMessageToLog(msg);
			return;
		}
	}

	/**
	 * 
	 * The method called on click of {@link ServerController#btn_connectivity_stop}
	 * and try to close {@link Server} connection.
	 *
	 * @param event
	 *            the trigger event.
	 */
	private @FXML void stopConnectivity(ActionEvent event) {
		String msg = "Trying to stop TCP\\IP connection";
		m_logger.info(msg);
		addMessageToLog(msg);
		try {
			m_server.close();
		} catch (IOException e) {
			msg = "Failed on try to close TCP\\IP connection";
			m_logger.log(Level.SEVERE, msg, e);
			addMessageToLog(msg);
		}
	}

	/**
	 * 
	 * The method called on click of {@link ServerController#btn_run_logger_file}
	 * and try to load the logger file.
	 *
	 * @param event
	 *            the trigger event.
	 */
	private @FXML void runLoggerFile(ActionEvent event) {
		String loggerPath = LogManager.getLoggerPath();
		if (!(loggerPath != null && !loggerPath.isEmpty())) {
			m_logger.warning("'Show Text Log' button while the log path not initialized.");
			showAlertMessage("Text log file did not initialized.");
			return;
		}

		File file = new File(loggerPath);
		if (!file.exists()) {
			m_logger.warning("There is not a log in the receiving log path: " + loggerPath);
			showAlertMessage("Text logger did not initialized.");
			return;
		}

		// check if Desktop is supported by Platform or not
		if (!Desktop.isDesktopSupported()) {
			m_logger.info(
					"'Desktop Platform' is not supported in this computer, so it is impossible to open text log file.");
			showAlertMessage("Error:\nFiles opening platfron are not supported in this computer!\nLog path: \""
					+ loggerPath + "\"");
			return;

		}

		Desktop desktop = Desktop.getDesktop();

		try {
			desktop.open(file);
		} catch (IOException ex) {
			m_logger.severe("Failed on try to open the text logger file! Exception: " + ex.getMessage());
			showAlertMessage("Some error occured on try to open the text log file!\nLog path: \"" + loggerPath + "\"");
		}
	}

	// end region -> UI Methods

	// region Private Methods

	/**
	 * 
	 * The method draw the {@link ServerConfiguration} to
	 * {@link ServerController#setting_table}.
	 *
	 */
	private void drawContantToTable() {
		DbConfiguration dbConfiguration = m_configuration.getDbConfiguration();
		ConnectivityConfiguration connectivityConfiguration = m_configuration.getConnectivityConfiguration();
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();
		settings.add(new SettingsRow(ConnectivityConfiguration.CONFIGURATION_TYPE_NAME,
				ConnectivityConfiguration.PROPERTY_NAME_PORT, Integer.toString(connectivityConfiguration.getPort())));

		settings.add(new SettingsRow(DbConfiguration.CONFIGURATION_TYPE_NAME, DbConfiguration.PROPERTY_NAME_IP,
				dbConfiguration.getIp()));
		settings.add(new SettingsRow(DbConfiguration.CONFIGURATION_TYPE_NAME, DbConfiguration.PROPERTY_NAME_SCHEMA,
				dbConfiguration.getSchema()));
		settings.add(new SettingsRow(DbConfiguration.CONFIGURATION_TYPE_NAME, DbConfiguration.PROPERTY_NAME_USERNAME,
				dbConfiguration.getUsername()));
		settings.add(new SettingsRow(DbConfiguration.CONFIGURATION_TYPE_NAME, DbConfiguration.PROPERTY_NAME_PASSWORD,
				dbConfiguration.getPassword()));
		setting_table.setItems(settings);
	}

	/**
	 * The method show dialog message from {@link Alert} type.
	 *
	 * @param message
	 *            the message to show.
	 */
	private void showAlertMessage(String message) {
		if (message == null || message.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();

		addMessageToLog(message);
	}

	/**
	 * The methods print a message to visual logger.
	 *
	 * @param msg
	 *            the message to print.
	 */
	private void addMessageToLog(String msg) {
		if (msg != null && !msg.isEmpty()) {
			LocalDateTime now = LocalDateTime.now();
			String time = s_dateTimeFormatter.format(now);

			// updating UI thread from different thread.
			javafx.application.Platform.runLater(() -> {
				textarea_log.appendText(time + " - " + msg + ".\n");
			});
		}
	}

	/**
	 * The methods reset all the 'Connected' {@link User} entities in the DB to be
	 * 'Disconnected'.
	 */
	private void resetUsersStatus() {
		if (!m_dbContoller.isRunning()) {
			m_resetUserStatus = true;
			m_logger.info("The request to reset all user status will happened when the DB connect.");
			return;
		}

		try {
			String updateAllUsersToDisconnectQuery = QueryGenerator.generateUpdateAllUsersToDisconnectQuery();
			boolean executeQuery = m_dbContoller.executeQuery(updateAllUsersToDisconnectQuery);
			String msg;
			if (executeQuery) {
				msg = "All users status updated to 'Disconnected' successfully.";
				m_logger.info(msg);
			} else {
				msg = "Failed on try to update all users to 'Disconnected' status, client connection problems may occur. See the log file for more information.";
				m_logger.warning(msg);
			}
			addMessageToLog(msg);
			m_resetUserStatus = false;
		} catch (Exception ex) {
			m_logger.warning(
					"Failed on try to update all users to 'Disconnected' status, client connection problems may occur. exception: "
							+ ex.getMessage());
		}
	}

	// end region -> Private Methods

	// region Server Handlers Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onServerStarted() {

		btn_connectivity_start.setDisable(true);
		btn_connectivity_stop.setDisable(false);
		circle_connectivity_on.setFill(Paint.valueOf("green"));
		circle_connectivity_off.setFill(Paint.valueOf("grey"));
		addMessageToLog("TCP\\IP connection opened successfully");

		resetUsersStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onServerStopped() {
		btn_connectivity_start.setDisable(false);
		btn_connectivity_stop.setDisable(true);
		circle_connectivity_on.setFill(Paint.valueOf("grey"));
		circle_connectivity_off.setFill(Paint.valueOf("red"));
		addMessageToLog("TCP\\IP connection closed successfully");
	}

	// end region -> Server Handlers Implementation

	// region Server.ClientConnectionHandler Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected(String clientDetails, int numberOfConnectedClients) {
		javafx.application.Platform
				.runLater(() -> label_numberOfConnectedUsers.setText(Integer.toString(numberOfConnectedClients)));
		addMessageToLog(
				"A client: " + clientDetails + " connected, number of connected clients: " + numberOfConnectedClients);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected(String clientDetails, int numberOfConnectedClients) {
		javafx.application.Platform
				.runLater(() -> label_numberOfConnectedUsers.setText(Integer.toString(numberOfConnectedClients)));
		addMessageToLog("A client: " + clientDetails + " disconnected, number of connected clients: "
				+ numberOfConnectedClients);
	}

	// end region -> Server.ClientConnectionHandler Implementation

}
