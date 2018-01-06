
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;
import newEntities.Costumer;
import newEntities.IEntity;

/**
 *
 * CompanyEmployeeController: Class controls the events and action in the
 * costumer menu UI.
 * 
 */
public class CostumerController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private AnchorPane costumer_anchor;

	@FXML private Button create_reservation_button;

	@FXML private Button cancel_reservation_button;

	/* End of --> UI Binding Fields region */

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

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
			m_client.setClientStatusHandler(null);
			m_client.setMessagesHandler(null);

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
			m_logger.severe(msg + ", excepion: " + e.getMessage());
		}
	}

	private void initializeCostumerData()
	{
		Costumer costumer = new Costumer();
		costumer.setUserName(ApplicationEntryPoint.ConnectedUser.getUserName());
		Message entityMessage = MessagesFactory.createGetEntityMessage(costumer);
		m_client.sendMessageToServer(entityMessage);
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
		initializeClientHandler();
		initializeCostumerData();
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

	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
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
				m_logger.warning("Failed when sending a message to the server.");
			} else {
				m_logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
			}
			return;
		}

		if (!(messageData instanceof EntityData)) {
			m_logger.warning(
					"Received message data not of the type requested, requested: " + EntityData.class.getName());
			return;
		}

		IEntity entity = ((EntityData) messageData).getEntity();
		if (!(entity instanceof Costumer)) {
			m_logger.warning("Received entity not of the type requested.");
			return;
		}

		Costumer costumer = (Costumer) entity;
		Costumer_SavedData.initializeSavedData(costumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{
		// TODO Shimon : Add event handling
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		// TODO Yoni : Add implementation.
	}

	/* End of --> Client handlers implementation region */
}
