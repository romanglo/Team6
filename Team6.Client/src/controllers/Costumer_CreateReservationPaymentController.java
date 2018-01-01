
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.CatalogItemRow;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.CostumerSubscription;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;

/**
 *
 * Costumer_CreateReservationCatalogController: Class generates a reservation
 * creation.
 * 
 */
public class Costumer_CreateReservationPaymentController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	// UI Binding Fields region

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private TableView<CatalogItemRow> catalog_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_name;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_amount;

	@FXML private TableColumn<CatalogItemRow, Double> tablecolumn_price;

	@FXML private Label total_price_label;

	// end region -> UI Binding Fields

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

	ObservableList<CatalogItemRow> catalog = FXCollections.observableArrayList();

	/* End of --> Fields region */

	// region Private Methods

	@FXML
	private void backButtonClick(ActionEvent backEvent)
	{
		openSelectedWindow(backEvent, "/boundaries/Costumer_CreateReservation.fxml");
	}
	
	@FXML 
	private void paymentButtonClick(ActionEvent paymentEvent) {
		Costumer_SavedData.setReservationList();
		if (Costumer_SavedData.getSubscription() == CostumerSubscription.None) {
			openSelectedWindow(paymentEvent, "/boundaries/Costumer_PaymentCreditCard.fxml");
		}
		else {
			/* TODO Yoni: Send the reservation to the server. */
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("Reservation completed. Thanks for using our application.");
			alert.showAndWait();
			openSelectedWindow(paymentEvent, "/boundaries/Costumer_CreateReservation.fxml");
		}
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
		initializeTable();
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

	private void initializeTable()
	{
		gatherReservationList();

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_amount.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("amount"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Double>("price"));

		catalog_table.setItems(catalog);
		catalog_table.refresh();
	}
	
	/**
	 * Gather the reservation items and count the amounts of each item.
	 */
	private void gatherReservationList()
	{
		HashMap<Integer, CatalogItemRow> gatheredReservation = new HashMap<>();
		ArrayList<CatalogItemRow> gatheredCustomized = new ArrayList<>();
		/*
		 * Separate the customized items with the catalog ones.
		 */
		for (ItemEntity entity : Costumer_SavedData.getCostumerReservationList()) {
			if (gatheredReservation.containsKey(entity.getId())
					&& entity.getItemType() != ProductType.CustomizedArrangement) {
				CatalogItemRow item = gatheredReservation.get(entity.getId());
				item.setM_amount(item.getM_amount() + 1);
				item.setM_price(item.getM_price() + entity.getItemPrice());
				gatheredReservation.put(entity.getId(), item);
				continue;
			}

			CatalogItemRow itemReserve = new CatalogItemRow(entity.getId(), entity.getName(), 1, entity.getItemPrice());
			if (entity.getItemType() == ProductType.CustomizedArrangement) {
				gatheredCustomized.add(itemReserve);
				continue;
			}

			gatheredReservation.put(entity.getId(), itemReserve);
		}

		double totalPrice = 0;
		for (CatalogItemRow item : gatheredReservation.values()) {
			totalPrice += item.getM_price();
			catalog.add(item);
		}

		for (CatalogItemRow item : gatheredCustomized) {
			totalPrice += item.getM_price();
			catalog.add(item);
		}
		
		total_price_label.setText(String.format("%.2f", totalPrice));
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
