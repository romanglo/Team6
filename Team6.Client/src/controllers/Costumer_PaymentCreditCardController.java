
package controllers;

import java.awt.Dialog;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.CatalogItemRow;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.ItemEntity;
import entities.ProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;

/**
 *
 * Costumer_CreateReservationCustomizedController: Class generates a reservation
 * customized item creation.
 * 
 */
public class Costumer_PaymentCreditCardController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private TextField credit_card_number;

	/* End region -> UI Binding Fields */

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

	ObservableList<CatalogItemRow> catalog = FXCollections.observableArrayList();

	private ArrayList<ItemEntity> m_catalogList;

	/* End of --> Fields region */

	// region Getters
	// end region -> Getters

	// region Setters
	// end region -> Setters

	// region Constructors
	// end region -> Constructors

	// region Public Methods
	// end region -> Public Methods

	// region Private Methods

	/**
	 * Return to the create reservation window.
	 *
	 * @param backEvent
	 *            Button pressed event.
	 */
	@FXML
	private void backButtonClick(ActionEvent backEvent)
	{
		try {
			/* Clear client handlers. */
			m_client.setClientStatusHandler(null);
			m_client.setMessagesHandler(null);

			/* Hide the current window. */
			((Node) backEvent.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/boundaries/Costumer_CreateReservation.fxml").openStream());

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

	/**
	 * Find a matching item in the catalog and add it to the reservation list.
	 *
	 * @param finishEvent
	 *            Button pressed event.
	 */
	@FXML
	private void finishButtonClick(ActionEvent finishEvent)
	{
		if (credit_card_number.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("You left empty fields.");
			alert.show();
			return;
		}
		
		/* TODO Yoni: Send the reservation to the server. */
		
		backButtonClick(finishEvent);
	}

	// end region -> Private Methods

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
		initializeCatalogList();
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

	private void initializeCatalogList()
	{
		/* TODO Yoni: Get catalog from the server. */
		m_catalogList = new ArrayList<>();
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// TODO Yoni : Add event handling
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{
		// TODO Yoni : Add event handling
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		// TODO Yoni : Add event handling
	}

	/* End of --> Client handlers implementation region */
}
