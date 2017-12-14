package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import configurations.ConnectivityConfiguration;
import configurations.DbConfiguration;
import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import db.EntitiesResolver;
import db.QueryFactory;
import entities.IEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import logger.LogFormatter;
import logger.LogManager;
import messages.EntityData;
import messages.EntityDataOperation;
import messages.Message;
import messages.IMessageData;

/**
 *
 * ServerController: Server GUI controller.
 * 
 */
public class ServerController implements Initializable, Server.ServerStatusHandler, Server.MessagesHandler {
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

	private Handler m_logHandler;

	// end region -> Fields

	// region Initializable Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeImages();

		initializeFields();

		initializeLog();

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
										"Attention: You must reopen the application for the changes to take effect!");
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

					m_logger.info("The Configuration " + rowData.getType() + '-' + rowData.getSetting()
							+ " value changed to " + rowData.getValue());
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
		m_server.setMessagesHandler(this);
	}

	private void initializeLog() {
		m_logHandler = new LogHandler();
		LogFormatter formatter = new LogFormatter();
		m_logHandler.setFormatter(formatter);
		m_logger.addHandler(m_logHandler);
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
			m_dbContoller.Start();
			btn_db_start.setDisable(true);
			btn_db_stop.setDisable(false);
			circle_db_on.setFill(Paint.valueOf("green"));
			circle_db_off.setFill(Paint.valueOf("grey"));
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Failed to connect to database", event);
			return;
		}
	}

	@FXML
	private void stopDb(ActionEvent event) {
		try {
			m_dbContoller.Stop();
			btn_db_start.setDisable(false);
			btn_db_stop.setDisable(true);
			circle_db_on.setFill(Paint.valueOf("grey"));
			circle_db_off.setFill(Paint.valueOf("red"));
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Failed to disconnect from database", event);
		}
	}

	@FXML
	private void startConnectivity(ActionEvent event) {
		try {
			m_server.listen();
			m_logger.info("Trying to start listening..");
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Listening request faild.", e);
			return;
		}
		onServerStarted();
	}

	@FXML
	private void stopConnectivity(ActionEvent event) {
		m_logger.info("Trying to stop listening..");
		try {
			m_server.close();
		} catch (IOException e) {
			m_logger.severe("An error occurred on closing connection! exception: " + e.getMessage());
		}
		onServerStopped();
	}

	@FXML
	private void updateSettingsFile(ActionEvent event) {
		m_configuration.updateResourceFile();
		btn_update_settings.setDisable(true);
	}

	// end region -> UI Methods

	// region Package Internal Methods

	/**
	 * The method should be called in in application shutdown sequence.
	 */
	void shutdown() {
		// cleanup code here...
		if (m_logger != null && m_logHandler != null) {
			m_logger.removeHandler(m_logHandler);
		}

		if (m_server != null) {
			m_server.setMessagesHandler(null);
			m_server.setServerActionHandler(null);
		}

	}
	// end region -> Package Internal Methods

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

	private IEntity onEntityDataReceived(IMessageData messageData) throws Exception {
		EntityData entityData = (EntityData) messageData;
		IEntity receivedEntity = entityData.getEntity();

		if (receivedEntity == null) {
			return null;
		}

		IEntity returningEntity = null;

		ResultSet queryResult = null;
		try {
			switch (entityData.getOperation()) {
			case Update:
				String generatedUpdateQuery = QueryFactory.generateUpdateEntityQuery(receivedEntity);
				if (generatedUpdateQuery == null || generatedUpdateQuery.isEmpty()) {
					break;
				}
				m_dbContoller.executeUpdateQuery(generatedUpdateQuery);
				break;

			case Get:
				String generatedGetQuery2 = QueryFactory.generateGetEntityQuery(receivedEntity);
				if (generatedGetQuery2 == null || generatedGetQuery2.isEmpty()) {
					break;
				}
				queryResult = m_dbContoller.executeSelectQuery(generatedGetQuery2);
				if (queryResult == null) {
					break;
				}
				returningEntity = EntitiesResolver.resolveResultSet(queryResult, receivedEntity);
				break;

			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close query result. Exception: " + e.getMessage());
				}
			}
		}
		return returningEntity;
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
	}
	// end region -> Private Methods

	// region Server Handlers Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public /* synchronized */ Message onMessageReceived(Message msg) throws Exception {
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof EntityData) {
			IEntity returnEntity = onEntityDataReceived(messageData);
			if (returnEntity != null) {
				IMessageData returnedMessageData = new EntityData(EntityDataOperation.None, returnEntity);
				msg.setMessageData(returnedMessageData);
				return msg;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onServerStarted() {

		btn_connectivity_start.setDisable(true);
		btn_connectivity_stop.setDisable(false);
		circle_connectivity_on.setFill(Paint.valueOf("green"));
		circle_connectivity_off.setFill(Paint.valueOf("grey"));
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
	}

	// end region -> Server Handlers Implementation

	// region Nested Classes

	/**
	 * LogHandler: A class that connect application {@link Logger} with the server
	 * log view.
	 */
	private class LogHandler extends Handler {

		@Override
		public void close() throws SecurityException {
			// ignored
		}

		@Override
		public void flush() {
			// ignored
		}

		@Override
		public synchronized void publish(LogRecord record) {
			try {
				if (record == null || textarea_log == null) {
					return;
				}
				Formatter logFormmater = getFormatter();
				String msgFormmated = logFormmater.format(record);
				textarea_log.appendText(msgFormmated);
			} catch (Exception e) {
				m_logger.removeHandler(m_logHandler);
				m_logger.warning("Logging to log UI disabled due to an excpetion: " + e.getMessage());
			}
		}
	}

	// end region -> Nested Classes
}
