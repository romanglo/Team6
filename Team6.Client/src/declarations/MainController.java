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
import javafx.scene.control.Label;
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
	@FXML static Label m_itemIdLbl;
	@FXML static Label m_itemNameLbl;
	@FXML static Label m_itemTypeLbl;

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
	@SuppressWarnings("unused")
	@FXML
	private void ServerConnection(ActionEvent connectionEvent)
	{
		try {
			if (connectionEvent.getSource().equals(m_connectToServer)) {
				EntryPoint.initializeConnection();
				m_connectToServer.setDisable(false);
				m_disconnectFromServer.setDisable(true);
				m_connectedLight.setFill(Color.GREEN);
				m_disconnectedLight.setFill(Color.GREY);
				m_getItemPane.setVisible(true);
			} else {
				EntryPoint.disposeConnection();
				m_connectToServer.setDisable(true);
				m_disconnectFromServer.setDisable(false);
				m_connectedLight.setFill(Color.GREY);
				m_disconnectedLight.setFill(Color.GREEN);
				m_getItemPane.setVisible(false);
			}
		}
		catch (IOException ioe) {
			s_logger.warning("Connection fault. Exception: " + ioe.getMessage());
		}
	}

	@SuppressWarnings("unused")
	@FXML
	private void getItemFromServer(ActionEvent getItemEvent)
	{
		Message msg = MessagesFactory.createUpdateEntityMessage(
				new ProductEntity(Integer.parseInt(m_enterItemId.getText()), "Roman", ProductType.Flower));
		EntryPoint.clientController.handleMessageFromClientUI(msg);
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
