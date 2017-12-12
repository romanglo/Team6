
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import entities.IEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import logs.LogFormatter;
import logs.LogManager;
import messages.EntityData;
import messages.Message;
import messages.MessageData;
import messages.MessagesFactory;

public class ServerController implements Initializable, Server.ServerStatusHandler, Server.MessagesHandler {
	// region UI Elements

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

	/* Setting table and columns declaration */
	@FXML
	private TableView<SettingsRow> setting_table;
	@FXML
	private TableColumn<SettingsRow, String> setting_column;
	@FXML
	private TableColumn<SettingsRow, String> value_column;

	// end region -> UI Elements

	// region Fields

	private DbController m_dbContoller;

	private Server m_server;

	private ServerConfiguration m_configuration;

	private Logger m_logger;

	// end region -> Fields

	// region UI Methods

	@FXML
	void startDb(ActionEvent event) {
		try {
			m_dbContoller.Start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		btn_db_start.setDisable(true);
		btn_db_stop.setDisable(false);
		circle_db_on.setFill(Paint.valueOf("green"));
		circle_db_off.setFill(Paint.valueOf("grey"));
	}

	@FXML
	void stopDb(ActionEvent event) {
		try {
			m_dbContoller.Stop();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		btn_db_start.setDisable(false);
		btn_db_stop.setDisable(true);
		circle_db_on.setFill(Paint.valueOf("grey"));
		circle_db_off.setFill(Paint.valueOf("red"));
	}

	@FXML
	void startConnectivity(ActionEvent event) {
		onServerStarted();
	}

	@FXML
	void stopConnectivity(ActionEvent event) {
		onServerStopped();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeView();

		initializeFields();

		initializeLog();

		initializeServerLogic();

	}

	// end region -> UI Methods

	// region Private Methods

	private void initializeServerLogic() {
		m_server.setServerActionHandler(this);
		m_server.setMessagesHandler(this);
	}

	private void initializeLog() {
		Handler logHandler = new LogHandler();
		LogFormatter formatter = new LogFormatter();
		logHandler.setFormatter(formatter);
		m_logger.addHandler(logHandler);
	}

	private void initializeFields() {
		m_dbContoller = ApplicationEntryPoint.DbContoller;
		m_server = ApplicationEntryPoint.Server;
		m_configuration = ServerConfiguration.getInstance();
		m_logger = LogManager.getLogger();
	}

	private void initializeView() {
		setting_column.setCellValueFactory(new PropertyValueFactory<>("settings"));
		value_column.setCellValueFactory(new PropertyValueFactory<>("values"));
		setting_table.setItems(getSettings());
		setting_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	private ObservableList<SettingsRow> getSettings() {
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();
		settings.add(new SettingsRow("IP", "1.1.1.1"));
		settings.add(new SettingsRow("PORT", "5555"));
		return settings;
	}

	private IEntity onEntityDataReceived(MessageData messageData) {
		EntityData entityData = (EntityData) messageData;
		IEntity returningEntity = null;
		switch (entityData.getOperation()) {
		case Update:
//			returningEntity = m_dbContoller.executeUpdateEntity(entityData.getEntity());
		case Get:
			returningEntity = m_dbContoller.executeGetEntity(entityData.getEntity());
		default:
			break;
		}
		return returningEntity;
	}

	// end region -> Private Methods

	// region Server Handlers Implementation

	@Override
	public Message onMessageReceived(Message msg, String clientDetails) {
		m_logger.info(
				"Received message from client. Client details: " + clientDetails + ", Message: " + msg.toString());
		MessageData messageData = msg.getMessageData();
		if (messageData instanceof EntityData) {
			IEntity onEntityDataReceived = onEntityDataReceived(messageData);
			return MessagesFactory.createEntityMessage(onEntityDataReceived);
		}
		return null;
	}

	@Override
	public void onServerStarted() {
		try {
			m_server.listen();
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Listening request faild.", e);
			return;
		}
		btn_connectivity_start.setDisable(true);
		btn_connectivity_stop.setDisable(false);
		circle_connectivity_on.setFill(Paint.valueOf("green"));
		circle_connectivity_off.setFill(Paint.valueOf("grey"));
	}

	@Override
	public void onServerStopped() {
		try {
			m_server.stopListening();
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Listening request faild.", e);
			return;
		}
		btn_connectivity_start.setDisable(false);
		btn_connectivity_stop.setDisable(true);
		circle_connectivity_on.setFill(Paint.valueOf("grey"));
		circle_connectivity_off.setFill(Paint.valueOf("red"));
	}

	// end region -> Server Handlers Implementation

	// region Nested Classes

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
		public void publish(LogRecord record) {
			if (record == null || textarea_log == null) {
				return;
			}
			Formatter logFormmater = getFormatter();
			String msgFormmated = logFormmater.format(record);
			String currentText = textarea_log.getText();
			if (currentText == null || currentText.isEmpty()) {
				currentText = msgFormmated;
			} else {
				currentText = currentText.trim() + '\n' + msgFormmated;
			}
			textarea_log.setText(currentText);
			textarea_log.selectPositionCaret(textarea_log.getLength());
			textarea_log.deselect();
		}
	}
	// end region -> Nested Classes
}
