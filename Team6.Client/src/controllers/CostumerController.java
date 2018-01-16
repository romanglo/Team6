
package controllers;

import java.io.InputStream;
import java.util.List;

import client.ApplicationEntryPoint;
import client.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import newEntities.Costumer;
import newEntities.IEntity;
import newEntities.ShopManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * CompanyEmployeeController: Class controls the events and action in the
 * costumer menu UI.
 * 
 */
public class CostumerController extends BaseController implements  Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private AnchorPane costumer_anchor;

	@FXML private Button create_reservation_button;

	@FXML private Button cancel_reservation_button;

	@FXML private ComboBox<Integer> combo_shop;

	/* End of --> UI Binding Fields region */

	/* Fields region */

	private ObservableList<Integer> shops = FXCollections.observableArrayList();

	/* End of --> Fields region */

	/* UI events region */

	/**
	 * 
	 * @param createReservation
	 *            press on the create reservation button.
	 */
	@FXML
	public void createReservationClick(ActionEvent createReservation)
	{
		if (combo_shop.getValue() == null) {
			return;
		}
		Costumer_SavedData.setShopManagerId(combo_shop.getValue());
		openSelectedWindow(createReservation, "/boundaries/Costumer_CreateReservation.fxml");
	}

	/**
	 * 
	 * @param cancelReservation
	 *            press on the customize item button.
	 */
	@FXML
	public void secondButtonClick(ActionEvent cancelReservation)
	{
		openSelectedWindow(cancelReservation, "/boundaries/Costumer_CancelReservation.fxml");
	}

	private void openSelectedWindow(ActionEvent event, String fxmlPath)
	{
		try {
			/* Clear client handlers. */
			m_Client.setClientStatusHandler(null);
			m_Client.setMessagesHandler(null);

			/* Hide the current window. */
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource(fxmlPath).openStream());

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			String msg = "Failed to load the next window";
			m_Logger.severe(msg + ", excepion: " + e.getMessage());
		}
	}

	/* End of --> UI events region */

	/* Initializing methods region */

	@Override
	protected void internalInitialize()
	{
		initializeImages();
		initializeClientHandler();
		initializeCostumerData();
		initializeShopData();
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

	private void initializeClientHandler()
	{
		m_Client.setMessagesHandler(this);
		m_Client.setClientStatusHandler(this);
	}

	private void initializeCostumerData()
	{
		Costumer costumer = new Costumer();
		costumer.setUserName(ApplicationEntryPoint.ConnectedUser.getUserName());
		Message entityMessage = MessagesFactory.createGetEntityMessage(costumer);
		m_Client.sendMessageToServer(entityMessage);
	}

	private void initializeShopData()
	{
		ShopManager shopManager = new ShopManager();
		Message message = MessagesFactory.createGetAllEntityMessage(shopManager);
		m_Client.sendMessageToServer(message);
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof RespondMessageData) {
			if (!((RespondMessageData) messageData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				m_Logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
			}
			return;
		}

		if (messageData instanceof EntityData) {
			IEntity entity = ((EntityData) messageData).getEntity();
			if (!(entity instanceof Costumer)) {
				m_Logger.warning("Received entity not of the type requested.");
				return;
			}

			Costumer costumer = (Costumer) entity;
			Costumer_SavedData.initializeSavedData(costumer);
		} else if (messageData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
			for (IEntity entity : entities) {
				if (!(entity instanceof ShopManager)) {
					m_Logger.warning("Received entity not of the type requested.");
					return;
				}
				ShopManager shopManager = (ShopManager) entity;
				shops.add(shopManager.getId());
			}
			
			Platform.runLater(()->{
				if (shops != null && !shops.isEmpty()) {
					combo_shop.setItems(shops); 
					combo_shop.getSelectionModel().selectFirst();
				} else {
					combo_shop.getItems().clear();
				}
			});
		} else {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}
	}

	/* End of --> Client handlers implementation region */
}
