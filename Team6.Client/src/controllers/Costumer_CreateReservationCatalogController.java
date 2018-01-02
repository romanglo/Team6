
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.CatalogItemRow;
import client.ApplicationEntryPoint;
import client.Client;
import entities.IEntity;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
 * Costumer_CreateReservationCatalogController: Class generates a reservation
 * creation.
 * 
 */
public class Costumer_CreateReservationCatalogController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	// UI Binding Fields region

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private TableView<CatalogItemRow> catalog_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_name;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_type;

	@FXML private TableColumn<CatalogItemRow, Double> tablecolumn_price;

	@FXML private TableColumn<CatalogItemRow, ImageView> tablecolumn_image;

	// end region -> UI Binding Fields

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	ObservableList<CatalogItemRow> catalog = FXCollections.observableArrayList();

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
	 * Parse string to the product type.
	 * 
	 * @param stringItemType
	 *            Input string.
	 * @return The product type.
	 */
	private ProductType ParseStringToProductType(String stringItemType)
	{
		if (stringItemType.equalsIgnoreCase("Flower")) {
			return ProductType.Flower;
		} else if (stringItemType.equalsIgnoreCase("FlowerPot")) {
			return ProductType.FlowerPot;
		} else if (stringItemType.equalsIgnoreCase("BridalBouquet")) {
			return ProductType.BridalBouquet;
		} else if (stringItemType.equalsIgnoreCase("FlowerArrangement")) {
			return ProductType.FlowerArrangement;
		}
		return null;
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
		getCatalogFromServer();
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

	private void initializeTable()
	{
		catalog_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					ItemEntity newItemEntity = new ItemEntity(rowData.getM_id(), rowData.getName(),
							ParseStringToProductType(rowData.getType()), rowData.getM_price(),
							rowData.getM_domainColor(), rowData.getM_image());
					Costumer_SavedData.addReservation(newItemEntity);
				}
			});
			return tableRow;
		});

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Double>("price"));
		tablecolumn_image.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, ImageView>("image"));

		catalog_table.setItems(catalog);
		catalog_table.refresh();
	}

	private void getCatalogFromServer()
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
		for (IEntity entity : entityList) {
			if (!(entity instanceof ItemEntity)) {
				m_logger.warning("Received entity not of the type requested.");
				return;
			}

			ItemEntity item = (ItemEntity) entity;
			CatalogItemRow itemRow = new CatalogItemRow(item.getId(), item.getName(), item.getItemPrice(),
					item.getItemImage(), item.getColor(), item.getItemType().toString());
			catalog.add(itemRow);
		}

		initializeTable();
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
