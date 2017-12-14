
package client;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import configurations.ClientConfiguration;
import connectivity.Client;
import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import logger.LogManager;
import messages.EntityData;
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * MainController: Controls all the actions made in the UI of the client.
 * 
 */
public class ClientController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* Fields region */

	/* Connection circles. */
	@FXML Circle m_connectedLight;

	@FXML Circle m_disconnectedLight;

	/* Connection buttons. */
	@FXML Button m_connectToServer;

	@FXML Button m_disconnectFromServer;

	/* Get item text field and button. */
	@FXML TextField m_enterItemId;

	@FXML Button m_getItemFromServer;

	/* Setting table declaration */
	@FXML private TableView<SettingsRow> setting_table;

	@FXML private TableColumn<SettingsRow, String> tablecolumn_setting;

	@FXML private TableColumn<SettingsRow, String> tablecolumn_value;

	@FXML private Button btn_update_settings;

	/* Split pane attribute. */
	@FXML SplitPane m_getItemPane;

	/* Item display labels. */
	@FXML TextField m_itemIdUpdate;

	@FXML TextField m_itemNameUpdate;

	@FXML ComboBox<String> m_itemTypeUpdate;

	private static Logger s_logger = null;

	/* End of --> Fields region */

	/* Constructors region */

	/**
	 * UI controller constructor.
	 *
	 */
	public ClientController()
	{
		s_logger = LogManager.getLogger();
	}

	/* End of --> Constructors region */

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
		if (connectionEvent.getSource().equals(m_connectToServer)) {
			ApplicationEntryPoint.clientController.createConnectionWithServer();
		} else {
			ApplicationEntryPoint.disposeConnection();
		}
		m_enterItemId.setText("");
		clearFields();
	}

	@FXML
	private void getItemFromServer(ActionEvent getItemEvent)
	{
		clearFields();
		if (m_enterItemId.getText().equals("")) {
			s_logger.info("Field is empty. Cannot send get request.");
			return;
		}

		try {
			Message msg = MessagesFactory
					.createGetEntityMessage(new ProductEntity(Integer.parseInt(m_enterItemId.getText())));
			ApplicationEntryPoint.clientController.handleMessageFromClientUI(msg);
		}
		catch (Exception ex) {
			s_logger.warning("Error when sending get request.");
		}
	}

	@FXML
	private void updateItemInServer(ActionEvent updateItem)
	{
		if (m_itemIdUpdate.getText().equals("") || m_itemNameUpdate.getText().equals("")
				|| m_itemTypeUpdate.getValue().equals("")) {
			s_logger.info("Empty field(s). Cannot send update request.");
			return;
		}

		try {
			Message msg = MessagesFactory
					.createUpdateEntityMessage(new ProductEntity(Integer.parseInt(m_itemIdUpdate.getText()),
							m_itemNameUpdate.getText(), getItemType((String) m_itemTypeUpdate.getValue())));
			ApplicationEntryPoint.clientController.handleMessageFromClientUI(msg);
		}
		catch (Exception ex) {
			s_logger.warning("Error when sending data for update. Exception: "+ ex.getMessage());
		}
	}

	@FXML
	private void updateSettingsFile(ActionEvent event)
	{
		ApplicationEntryPoint.s_clientConfiguration.updateResourceFile();
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
		initializeProductTypeComboBox();
		initializeConfigurationTable();
		initializeClientHandler();
	}

	private void initializeProductTypeComboBox()
	{
		m_itemTypeUpdate.getItems().addAll(ProductType.BridalBouquet.name(), ProductType.Flower.name(), ProductType.FlowerPot.name());
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
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()) {
						return;
					}
					String resultString = result.get();
					if (!(resultString != null && !resultString.equals(rowData.getValue()))) {
						return;
					}

					Client clientController = ApplicationEntryPoint.clientController;
					ClientConfiguration clientConfiguration = ApplicationEntryPoint.s_clientConfiguration;
					switch (rowData.getSetting()) {
						case ClientConfiguration.PROPERTY_NAME_IP:

							clientController.setHost(resultString);
							clientConfiguration.setIp(resultString);
						break;
						case ClientConfiguration.PROPERTY_NAME_PORT:
							try {
								int port = Integer.parseInt(resultString);
								clientController.setPort(port);
								clientConfiguration.setPort(port);
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
					if (clientController.isConnected()) {
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
		ApplicationEntryPoint.clientController.setMessagesHandler(this);
		ApplicationEntryPoint.clientController.setClientStatusHandler(this);
	}

	/* End of --> Initializing methods region */

	/* Implemented methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Object msg)
	{
		Message receivedMessage = (Message) msg;
		IEntity entity = ((EntityData) receivedMessage.getMessageData()).getEntity();

		ProductEntity productEntity = (ProductEntity) entity;
		m_itemIdUpdate.setText(String.valueOf(productEntity.getId()));
		m_itemNameUpdate.setText(productEntity.getName());
		m_itemTypeUpdate.getSelectionModel().select(productEntity.getProductType().name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{
		m_connectToServer.setDisable(true);
		m_disconnectFromServer.setDisable(false);
		m_connectedLight.setFill(Color.GREEN);
		m_disconnectedLight.setFill(Color.GREY);
		m_getItemPane.setDisable(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		m_connectToServer.setDisable(false);
		m_disconnectFromServer.setDisable(true);
		m_connectedLight.setFill(Color.GREY);
		m_disconnectedLight.setFill(Color.RED);
		m_getItemPane.setDisable(true);
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

	private ProductType getItemType(String type)
	{
		switch (type) {
			case "Flower":
				return ProductType.Flower;
			case "FlowerPot":
				return ProductType.FlowerPot;
			case "BridalBouquet":
				return ProductType.BridalBouquet;
			default:
				return null;
		}
	}

	private void clearFields()
	{
		m_itemIdUpdate.setText("");
		m_itemNameUpdate.setText("");		
		m_itemTypeUpdate.setValue("");
	}

	private void drawContantToTable()
	{
		ClientConfiguration dbConfiguration = ApplicationEntryPoint.s_clientConfiguration;
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();

		settings.add(new SettingsRow(ClientConfiguration.PROPERTY_NAME_IP, dbConfiguration.getIp()));
		settings.add(new SettingsRow("PORT", Integer.toString(ApplicationEntryPoint.s_clientConfiguration.getPort())));
		setting_table.setItems(settings);
	}

	/* End of --> Private methods region */
}
