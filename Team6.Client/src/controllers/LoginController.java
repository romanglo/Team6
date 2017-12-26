
package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.SettingsRow;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;

/**
 *
 * LoginController: Create first connection with the server and login to the system.
 * 
 */
public class LoginController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Connection circles :
	@FXML private Circle circle_connectedLight;

	@FXML private Circle circle_disconnectedLight;

	// Connection buttons :
	@FXML private Button btn_connectToServer;

	@FXML private Button btn_disconnectFromServer;

	// login fields and buttons :
	@FXML private TextField textField_userName;

	@FXML private PasswordField passwordField_userPassword;

	@FXML private AnchorPane anchorPane_login;

	// Setting table declaration :
	@FXML private TableView<SettingsRow> setting_table;

	@FXML private TableColumn<SettingsRow, String> tablecolumn_setting;

	@FXML private TableColumn<SettingsRow, String> tablecolumn_value;

	@FXML private Button btn_update_settings;

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private ClientConfiguration m_configuration;

	private Client m_client;

	/* End of --> Fields region */

	/* UI events region */

	/**
	 * Method is called after pressing the connect button and creates new connection
	 * 
	 * @param connectionEvent
	 *            Button click event.
	 */
	@FXML
	private void ServerConnection(ActionEvent connectionEvent) throws IOException
	{
		if (connectionEvent.getSource().equals(btn_connectToServer)) {
			m_client.createConnectionWithServer();
		} else {
			m_client.closeConnectionWithServer();
		}

		clearFields();
	}

	@FXML
	private void login(ActionEvent event)
	{
		String userName = textField_userName.getText().trim();
		if (userName == null || userName.isEmpty()) {
			m_logger.info("Field is empty. Cannot send login request.");
			return;
		}

		String pasword = passwordField_userPassword.getText();

		if (userName == null || userName.isEmpty()) {
			m_logger.info("Field is empty. Cannot send login request.");
			return;
		}

		/*
		 * try {
		 * 
		 * Message msg = MessagesFactory.createLoginMessage();
		 * m_client.sendMessageToServer(msg); } catch (Exception ex) {
		 * m_logger.warning("Error when sending get request, excpetion: " +
		 * ex.getMessage()); }
		 */

		URL url = null;

		if (userName.equalsIgnoreCase("companyemployee")) {
			url = getClass().getResource("/boundaries/CompanyEmployee.FXML");
		} else if (userName.equalsIgnoreCase("shopmanager")) {
			url = getClass().getResource("/boundaries/ShopManager.FXML");
		} else if (userName.equalsIgnoreCase("chainmanager")) {
			url = getClass().getResource("/boundaries/ChainManager.FXML");
		} else if (userName.equalsIgnoreCase("administrator")) {
			url = getClass().getResource("/boundaries/Administrator.FXML");
		} else if (userName.equalsIgnoreCase("shopemployee")) {
			url = getClass().getResource("/boundaries/ShopEmployee.FXML");
		} else if (userName.equalsIgnoreCase("costumerservice")) {
			url = getClass().getResource("/boundaries/CostumerServiceEmployee.FXML");
		} else if (userName.equalsIgnoreCase("costumer")) {
			url = getClass().getResource("/boundaries/Costumer.FXML");
		} else if (userName.equalsIgnoreCase("servicespecialist")) {
			url = getClass().getResource("/boundaries/ServiceSpecialist.FXML");
		} else {
			showInformationMessage("Wrong user name!");
			return;
		}

		m_client.setClientStatusHandler(null);
		m_client.setMessagesHandler(null);

		Stage nextStage = null;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			Parent parent = (Parent) fxmlLoader.load();
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
			nextStage = new Stage();
			nextStage.setScene(scene);
			nextStage.setTitle("Zer-Li");
			nextStage.setMinWidth(450);
			nextStage.setMinHeight(450);
			nextStage.show();
		}
		catch (Exception e) {
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}

		Stage stage = (Stage) anchorPane_login.getScene().getWindow();
		if (stage != null) {
			stage.close();
		} else {
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}

	@FXML
	private void updateSettingsFile(ActionEvent event)
	{
		m_configuration.updateResourceFile();
		btn_update_settings.setDisable(true);
	}

	/* End of --> UI events region */

	/* Initializing methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		initializeFields();
		initializeImages();
		initializeConfigurationTable();
		initializeClientHandler();
	}

	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
		m_client = ApplicationEntryPoint.Client;
	}

	private void initializeImages()
	{
		InputStream serverGif = getClass().getResourceAsStream("/boundaries/images/Flower.gif");
		if (serverGif != null) {
			Image image = new Image(serverGif);
			imageview_gif.setImage(image);
		}
		InputStream title = getClass().getResourceAsStream("/boundaries/images/Zerli_Headline.jpg");
		if (title != null) {
			Image image = new Image(title);
			imageview_title.setImage(image);
		}
	}

	private void initializeConfigurationTable()
	{
		setting_table.setRowFactory(param -> {
			TableRow<SettingsRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					SettingsRow rowData = tableRow.getItem();

					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Update Settings Value");
					dialog.setHeaderText("Do you want to update the value of " + rowData.getSetting() + " ?");
					dialog.setContentText("Please enter the new value:");

					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()) {
						return;
					}
					String resultString = result.get();
					if (!(resultString != null && !resultString.equals(rowData.getValue()))) {
						return;
					}

					switch (rowData.getSetting()) {
						case ClientConfiguration.PROPERTY_NAME_IP:

							m_client.setHost(resultString);
							m_configuration.setIp(resultString);
						break;
						case ClientConfiguration.PROPERTY_NAME_PORT:
							try {
								int port = Integer.parseInt(resultString);
								m_client.setPort(port);
								m_configuration.setPort(port);
							}
							catch (Exception e) {
								return;
							}
						break;
						default:
						break;
					}
					rowData.setValue(resultString);
					drawContantToTable();
					btn_update_settings.setDisable(false);
					if (m_client.isConnected()) {
						showInformationMessage("Attention: You must reconnect for the changes to take effect!");
					}
				}
			});
			return tableRow;
		});
		tablecolumn_setting.setCellValueFactory(new PropertyValueFactory<>("setting"));
		tablecolumn_value.setCellValueFactory(new PropertyValueFactory<>("value"));

		drawContantToTable();

		btn_update_settings.setDisable(true);
	}

	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
	}

	/* End of --> Initializing methods region */

	/* Implemented methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// TODO Roman : Add message handling
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{
		btn_connectToServer.setDisable(true);
		btn_disconnectFromServer.setDisable(false);
		circle_connectedLight.setFill(Color.GREEN);
		circle_disconnectedLight.setFill(Color.GREY);
		anchorPane_login.setDisable(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		btn_connectToServer.setDisable(false);
		btn_disconnectFromServer.setDisable(true);
		circle_connectedLight.setFill(Color.GREY);
		circle_disconnectedLight.setFill(Color.RED);
		anchorPane_login.setDisable(true);
	}

	/* End of --> Implemented methods region */

	/* Private methods region */

	private void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	private void clearFields()
	{
		textField_userName.clear();
		passwordField_userPassword.clear();
	}

	private void drawContantToTable()
	{
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();

		settings.add(new SettingsRow(ClientConfiguration.PROPERTY_NAME_IP, m_configuration.getIp()));
		settings.add(
				new SettingsRow(ClientConfiguration.PROPERTY_NAME_PORT, Integer.toString(m_configuration.getPort())));
		setting_table.setItems(settings);
	}

	/* End of --> Private methods region */
}
