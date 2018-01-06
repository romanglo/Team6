
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.RespondMessageData;

/**
 *
 * Costumer_CreateReservationController: Class controls the events and action in
 * the costumer menu UI.
 * 
 */
public class Costumer_CreateReservationController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	/* End of --> UI Binding Fields region */

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	/* End of --> Fields region */

	/* UI events region */

	/**
	 * @param openCatalog
	 *            press on the open catalog button.
	 */
	@FXML
	public void openCatalogClick(ActionEvent openCatalog)
	{
		openSelectedWindow(openCatalog, "/boundaries/Costumer_CreateReservationCatalog.fxml");
	}

	/**
	 * @param customizeItem
	 *            press on the customize item button.
	 */
	@FXML
	public void customizeItemClick(ActionEvent customizeItem)
	{
		openSelectedWindow(customizeItem, "/boundaries/Costumer_CreateReservationCustomized.fxml");
	}

	/**
	 * @param backEvent
	 *            press on the back button.
	 */
	@FXML
	public void backButtonClick(ActionEvent backEvent)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Attention");
		alert.setHeaderText(null);
		alert.setContentText("This action will cause a loss of the reservation, are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			openSelectedWindow(backEvent, "/boundaries/Costumer.fxml");
		}

	}

	/**
	 * Gather all the items the user selected and go to the payment screen.
	 *
	 * @param paymentEvent
	 */
	@FXML
	private void paymentButtonClick(ActionEvent paymentEvent)
	{
		if (Costumer_SavedData.getCostumerReservationList().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("Your items cart is empty.");
			alert.showAndWait();
			return;
		}

		Costumer_CreateReservationPaymentController.s_isCreateReservation = true;
		openSelectedWindow(paymentEvent, "/boundaries/Costumer_CreateReservationPayment.fxml");
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
	}

	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
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
		IMessageData entitiesListData = msg.getMessageData();
		if (entitiesListData instanceof RespondMessageData) {
			if (!((RespondMessageData) entitiesListData).isSucceed()) {
				m_logger.warning("Failed when sending a message to the server.");
			} else {
				m_logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
			}
			return;
		}
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
