
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import entities.IEntity;
import entities.ItemEntity;
import entities.ProductType;
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
import messages.EntitiesListData;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * Costumer_CreateReservationCustomizedController: Class generates a reservation
 * customized item creation.
 * 
 */
public class Costumer_CreateReservationCustomizedController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private TextField domain_color;

	@FXML private TextField min_price;

	@FXML private TextField max_price;

	@FXML private TextField item_amount;

	/* End region -> UI Binding Fields */

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

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
	 * @param addEvent
	 *            Button pressed event.
	 */
	@FXML
	private void addButtonClick(ActionEvent addEvent)
	{
		if (domain_color.getText().equals("") || min_price.getText().equals("") || max_price.getText().equals("")
				|| item_amount.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("You left empty fields.");
			alert.show();
			return;
		}

		ItemEntity reservation = null;
		for (ItemEntity entity : m_catalogList) {
			if (entity.getColor().equals(domain_color.getText())
					&& (entity.getItemPrice() >= Double.parseDouble(min_price.getText())
							&& entity.getItemPrice() <= Double.parseDouble(max_price.getText()))
					&& entity.getItemType() == ProductType.Flower) {
				// Check the following:
				// 1. Domain color as requested.
				// 2. Price is in range.
				// 3. The entity is of the type 'Flower'.
				reservation = new ItemEntity(entity.getId(), entity.getName(), ProductType.CustomizedArrangement,
						Integer.parseInt(item_amount.getText()) * entity.getItemPrice(), entity.getColor(),
						entity.getItemImage());
				break;
			}
		} /* End of --> for (ItemEntity entity : m_catalogList) */

		if (reservation != null) {
			Costumer_SavedData.addReservation(reservation);
			backButtonClick(addEvent);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("A match wasn't found, please try again.");
			alert.show();
		}
	} /* End of --> private void addButtonClick(ActionEvent addEvent) */

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
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(new ItemEntity(0));
		m_client.sendMessageToServer(entityMessage);
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
		if (!(entitiesListData instanceof EntitiesListData)) {
			m_logger.warning("Received message data not of the type requested.");
			return;
		}

		List<IEntity> entityList = ((EntitiesListData) entitiesListData).getEntities();
		m_catalogList = new ArrayList<>();
		for (IEntity entity : entityList) {
			if (!(entity instanceof ItemEntity)) {
				m_logger.warning("Received entity not of the type requested.");
				return;
			}

			ItemEntity item = (ItemEntity) entity;
			m_catalogList.add(item);
		}
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
