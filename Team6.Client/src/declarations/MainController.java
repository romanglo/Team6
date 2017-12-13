
package declarations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * MainController: Controls all the actions made in the UI of the client.
 * 
 */
public class MainController implements Initializable
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
	@FXML static TextField	m_itemIdLbl;
	@FXML static TextField	m_itemNameLbl;
	@FXML static TextField	m_itemTypeLbl;

	@SuppressWarnings("unused") private static Logger s_logger = null;

	/**
	 * Controller constructor.
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
	 *            - Button event.
	 */
	@FXML
	private void ServerConnection(ActionEvent connectionEvent)
	{
		try {
			if (connectionEvent.getSource().equals(m_connectToServer)) {
				EntryPoint.initializeConnection();
				m_connectToServer.setDisable(true);
				m_disconnectFromServer.setDisable(false);
				m_connectedLight.setFill(Color.GREEN);
				m_disconnectedLight.setFill(Color.GREY);
				m_getItemPane.setDisable(false);
			} else {
				EntryPoint.disposeConnection();
				m_connectToServer.setDisable(false);
				m_disconnectFromServer.setDisable(true);
				m_connectedLight.setFill(Color.GREY);
				m_disconnectedLight.setFill(Color.RED);
				m_getItemPane.setDisable(true);
			}
		}
		catch (IOException ioe) {
			s_logger.warning("Connection fault. Exception: " + ioe.getMessage());
		}
	}

	@FXML
	private void getItemFromServer(ActionEvent getItemEvent)
	{
		Message msg = MessagesFactory.createGetEntityMessage(
				new ProductEntity(Integer.parseInt(m_enterItemId.getText())));
		EntryPoint.clientController.handleMessageFromClientUI(msg);
	}

	@FXML
	private void updateItemInServer(ActionEvent updateItem)
	{
		Message msg = MessagesFactory.createUpdateEntityMessage(
				new ProductEntity(Integer.parseInt(m_enterItemId.getText()),
						m_itemNameLbl.getText(), getItemType(m_itemTypeLbl.getText())));
		EntryPoint.clientController.handleMessageFromClientUI(msg);
	}
	
	private ProductType getItemType(String type) {
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

	/**
	 * After response from the server, insert the details into the UI.
	 *
	 * @param entity
	 *            - details of the entity.
	 */
	public static void setItemDetails(IEntity entity)
	{
		m_itemIdLbl.setText(String.valueOf(((ProductEntity) entity).getId()));
		m_itemNameLbl.setText(((ProductEntity) entity).getName());
		m_itemTypeLbl.setText(((ProductEntity) entity).getProductType().toString());
	}

	ObservableList<SettingsRow> getSettings()
	{
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();
		settings.add(new SettingsRow("IP", EntryPoint.s_clientConfiguration.getIp()));
		settings.add(new SettingsRow("PORT", EntryPoint.s_clientConfiguration.getPort()));
		return settings;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		m_settingColumn.setCellValueFactory(new PropertyValueFactory<>("setting"));
		m_valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		m_settingsDisplayTable.setItems(getSettings());
		m_settingsDisplayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
}
