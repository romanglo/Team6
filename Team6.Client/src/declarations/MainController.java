
package declarations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import connectivity.Client;
import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import logs.LogManager;
import messages.EntityData;
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * MainController: Controls all the actions made in the UI of the client.
 * 
 */
public class MainController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{

	/* Connection circles. */
	@FXML Circle m_connectedLight;

	@FXML Circle m_disconnectedLight;

	/* Connection buttons. */
	@FXML Button m_connectToServer;

	@FXML Button m_disconnectFromServer;

	/* Get item text field and button. */
	@FXML TextField m_enterItemId;

	@FXML Button m_getItemFromServer;

	/* Table elements. */
	@FXML TableView<SettingsRow> m_settingsDisplayTable;

	@FXML TableColumn<SettingsRow, String> m_settingColumn;

	@FXML TableColumn<SettingsRow, String> m_valueColumn;

	/* Split pane attribute. */
	@FXML SplitPane m_getItemPane;

	/* Item display labels. */
	@FXML TextField m_itemIdUpdate;

	@FXML TextField m_itemNameUpdate;

	@FXML TextField m_itemTypeUpdate;

	private static Logger s_logger = null;

	/**
	 * UI controller constructor.
	 *
	 */
	public MainController()
	{
		s_logger = LogManager.getLogger();
	}

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
			EntryPoint.clientController.createConnectionWithServer();
		} else {
			EntryPoint.disposeConnection();
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
			EntryPoint.clientController.handleMessageFromClientUI(msg);
		}
		catch (Exception ex) {
			s_logger.log(Level.WARNING, "Error when sending get request. Exception: ", ex);
		}
	}

	@FXML
	private void updateItemInServer(ActionEvent updateItem)
	{
		if (m_itemIdUpdate.getText().equals("") || m_itemNameUpdate.getText().equals("")
				|| m_itemTypeUpdate.getText().equals("")) {
			s_logger.info("Empty field(s). Cannot send update request.");
			return;
		}

		try {
			Message msg = MessagesFactory
					.createUpdateEntityMessage(new ProductEntity(Integer.parseInt(m_itemIdUpdate.getText()),
							m_itemNameUpdate.getText(), getItemType(m_itemTypeUpdate.getText())));
			EntryPoint.clientController.handleMessageFromClientUI(msg);
		}
		catch (Exception ex) {
			s_logger.log(Level.WARNING, "Error when sending data for update. Exception: ", ex);
		}
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
		m_itemTypeUpdate.setText("");
	}

	ObservableList<SettingsRow> getSettings()
	{
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();
		settings.add(new SettingsRow("IP", EntryPoint.s_clientConfiguration.getIp()));
		settings.add(new SettingsRow("PORT", EntryPoint.s_clientConfiguration.getPort()));
		return settings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		initializeSettings();
		initializeClientHandler();
	}

	private void initializeSettings()
	{
		m_settingColumn.setCellValueFactory(new PropertyValueFactory<>("setting"));
		m_valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		m_settingsDisplayTable.setItems(getSettings());
		m_settingsDisplayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	private void initializeClientHandler()
	{
		EntryPoint.clientController.setMessagesHandler(this);
		EntryPoint.clientController.setClientStatusHandler(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Object msg)
	{
		Message receivedMessage = (Message) msg;
		IEntity entity = ((EntityData) receivedMessage.getMessageData()).getEntity();

		m_itemIdUpdate.setText(String.valueOf(((ProductEntity) entity).getId()));
		m_itemNameUpdate.setText(((ProductEntity) entity).getName());
		m_itemTypeUpdate.setText(((ProductEntity) entity).getProductType().toString());
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
}
