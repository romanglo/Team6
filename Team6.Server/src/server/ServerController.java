package server;

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
import db.NewMessagesResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

	@FXML
	private TabPane tabpane;

	/* Database start-stop button declaration */
	@FXML
	private Button btn_db_start;
	@FXML
	private Button btn_db_stop;

	/* Database status declaration */
	@FXML
	private Circle circle_db_on;
	@FXML
	private Circle circle_db_off;

	/* Connectivity start-stop button declaration */

	@FXML
	private Button btn_connectivity_start;
	@FXML
	private Button btn_connectivity_stop;

	/* Connectivity status declaration */
	@FXML
	private Circle circle_connectivity_on;
	@FXML
	private Circle circle_connectivity_off;

	/* Log text view declaration */
	@FXML
	private TextArea textarea_log;

	/* Setting table declaration */
	@FXML
	private TableView<SettingsRow> setting_table;
	@FXML
	private TableColumn<SettingsRow, String> tablecolumn_setting;
	@FXML
	private TableColumn<SettingsRow, String> tablecolumn_value;
	@FXML
	private TableColumn<SettingsRow, String> tablecolumn_type;
	@FXML
	private Button btn_update_settings;

	/* Title images */
	@FXML
	private ImageView imageview_gif;
	@FXML
	private ImageView imageview_title;

	// end region -> UI Elements

	// region Fields

	private DbController m_dbContoller;

	private Server m_server;

	private ServerConfiguration m_configuration;

	private Logger m_logger;

	private NewMessagesResolver m_messageResolver;

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

	private void initializeImages() {
		InputStream serverGif = getClass().getResourceAsStream("ServerGIF.gif");
		if (serverGif != null) {
			Image image = new Image(serverGif);
			imageview_gif.setImage(image);
		}
		InputStream title = getClass().getResourceAsStream("Zerli_Headline.jpg");
		if (title != null) {
			Image image = new Image(title);
			imageview_title.setImage(image);
		}
	}

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
								showInformationMessage(
										"Attention: You must reopen application connection for the changes to take effect!");
							}
						} catch (NumberFormatException e) {
							return;
						}
					} else {
						m_configuration.getDbConfiguration().updateValueByName(rowData.getSetting(), resultString);
						if (m_dbContoller.isRunning()) {
							showInformationMessage(
									"Attention: You must reopen database connection for the changes to take effect!");
						}
					}
					rowData.setValue(resultString);
					drawContantToTable();
					btn_update_settings.setDisable(false);
					String msg = "The Configuration " + rowData.getType() + '-' + rowData.getSetting()
							+ " value changed to " + rowData.getValue();
					m_logger.info(msg);
					addMessageToLog(msg);

				}
			});
			return tableRow;
		});
		tablecolumn_type.setCellValueFactory(new PropertyValueFactory<>("type"));
		tablecolumn_setting.setCellValueFactory(new PropertyValueFactory<>("setting"));
		tablecolumn_value.setCellValueFactory(new PropertyValueFactory<>("value"));

		drawContantToTable();

		btn_update_settings.setDisable(true);
	}

	private void initializeServerLogic() {
		m_server.setServerActionHandler(this);
		m_server.setClientConnectionHandler(this);
		
		m_messageResolver = new NewMessagesResolver(m_dbContoller);

		m_server.setMessagesHandler(m_messageResolver);
	}

	private void initializeFields() {
		m_dbContoller = ApplicationEntryPoint.DbContoller;
		m_server = ApplicationEntryPoint.Server;
		m_configuration = ServerConfiguration.getInstance();
		m_logger = LogManager.getLogger();
	}

	// end region -> Initializable Implementation

	// region UI Methods

	@FXML
	private void startDb(ActionEvent event) {
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
		addMessageToLog("Connected successfully to the database");
	}

	@FXML
	private void stopDb(ActionEvent event) {
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

	@FXML
	private void startConnectivity(ActionEvent event) {
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

	@FXML
	private void stopConnectivity(ActionEvent event) {
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

	@FXML
	private void updateSettingsFile(ActionEvent event) {
		m_configuration.updateResourceFile();
		btn_update_settings.setDisable(true);
	}

	// end region -> UI Methods

	// region Private Methods

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

	private void showInformationMessage(String message) {
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

	@Override
	public void onClientConnected(String clientDetails, int numberOfConnectedClients) {
		addMessageToLog(
				"A client: " + clientDetails + " connected, number of connected clients: " + numberOfConnectedClients);
	}

	@Override
	public void onClientDisconnected(String clientDetails, int numberOfConnectedClients) {
		addMessageToLog("A client: " + clientDetails + " disconnected, number of connected clients: "
				+ numberOfConnectedClients);
	}

	// end region -> Server.ClientConnectionHandler Implementation

}
