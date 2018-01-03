
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import logger.LogManager;
import messages.EntitiesListData;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;

import entities.IEntity;
import entities.ItemEntity;
import entities.ProductType;
import boundaries.CatalogItemRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 *
 * CompanyEmployeeController : TODO Shimon: Create class description
 * 
 */
public class CompanyEmployeeController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

	/* Catalog table declaration */
	@FXML private TableView<CatalogItemRow> catalog_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_name;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_type;

	@FXML private TableColumn<CatalogItemRow, Double> tablecolumn_price;

	@FXML private TableColumn<CatalogItemRow, ImageView> tablecolumn_image;
	/* End of --> Catalog table declaration */

	/* Action catalog buttons */
	@FXML private Button button_addNewItem;

	@FXML private Button button_removeItem;

	@FXML private Button button_save;

	@FXML private Button button_reset;
	/* End of --> Action catalog buttons */

	/* View and changes savers lists declaration */
	ObservableList<CatalogItemRow> catalog = FXCollections.observableArrayList();

	ArrayList<ItemEntity> itemAdded = new ArrayList<>();

	ArrayList<ItemEntity> itemRemoved = new ArrayList<>();

	ArrayList<ItemEntity> itemChanged = new ArrayList<>();
	/* End of --> View and changes savers lists declaration */

	/* UI events region */

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
		catalogInit();
		initializeConfigurationTable();
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

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	private void catalogInit()
	{
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(new ItemEntity(0));
		m_client.sendMessageToServer(entityMessage);

		// TODO : get item list from server
		// catalog.add(new CatalogItemRow(1, "Rose", "Flower", 19.99, "White"));
		// catalog.add(new CatalogItemRow(2, "Rose2", "Flower2", 29.99, "Red"));
		// catalog.add(new CatalogItemRow(3, "Rose3", "Flower3", 39.99, "Green"));
		// catalog.add(new CatalogItemRow(4, "Rose4", "Flower4", 49.99, "Blue"));
	}

	private void initializeConfigurationTable()
	{
		catalog_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					Node selectedNode = event.getPickResult().getIntersectedNode();
					String pickType;
					CatalogItemRow rowData = tableRow.getItem();
					if (selectedNode.getId() == null) {
						pickType = selectedNode.toString();
						pickType = pickType.substring(11, pickType.indexOf(',') - 1);

						if (pickType.equals(rowData.getName())) pickType = "name";
						else if (pickType.equals(rowData.getPrice())) pickType = "price";
						else if (pickType.equals(rowData.getType())) pickType = "type";
						else if (pickType.equals(rowData.getId())) pickType = "id";
						else pickType = "image";
					} else {
						pickType = selectedNode.getId().substring(12);
					}
					if (!(pickType.equals("id") || pickType.equals("image"))) {
						TextInputDialog dialog = new TextInputDialog();
						dialog.setTitle("Update Item Value");
						dialog.setHeaderText("Do you want to update the item " + pickType + "  ?\n" + "ID: "
								+ rowData.getM_id() + " , Name: " + rowData.getM_name() + " , Type: "
								+ rowData.getM_type() + " , Price: " + rowData.getM_price());
						dialog.setContentText("Please enter the new value:");
						// Traditional way to get the response value.
						Optional<String> result = dialog.showAndWait();
						if (!result.isPresent()) return;

						String resultString = result.get();
						if (resultString == null || resultString.isEmpty()) return;

						switch (pickType) {

							case "name":
								rowData.setM_name(resultString);
							break;
							case "type":
								if (!(resultString.equals(ProductType.Flower.toString())
										|| resultString.equals(ProductType.FlowerPot.toString())
										|| resultString.equals(ProductType.BridalBouquet.toString())
										|| resultString.equals(ProductType.FlowerArrangement.toString()))) {
									ErrorMSG("The type you entered doesn't exist");
									m_logger.warning("Entered wrorg ProductType");
									return;
								} else {
									rowData.setM_type(resultString);
								}
							break;
							case "price":
								Double price = Double.parseDouble(resultString);
								if (price <= 0) {
									ErrorMSG("The price you entered lower then 0");
									m_logger.warning("Entered zero or negative price");
									return;
								} else {
									rowData.setM_price(price);
								}

						}
						AddEditedItemToArray(rowData);
						CatalogChanged();
					} else if (event.getClickCount() == 2 && (!tableRow.isEmpty()) && pickType.equals("image")) {

						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Open Resource File");
						File selectedImage = fileChooser.showOpenDialog(null);

						try {
							BufferedImage imageReader = ImageIO.read(selectedImage);
							Image finalImage = SwingFXUtils.toFXImage(imageReader, null);
							rowData.setM_image(finalImage);
							AddEditedItemToArray(rowData);
						}
						catch (Exception e) {
						}
						CatalogChanged();

					}
					DrawContantToTable();
				}
			});
			return tableRow;
		});

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Double>("price"));
		tablecolumn_image.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, ImageView>("image"));
		DrawContantToTable();
	}

	/**
	 * Search for edited Item in itemChanged array.
	 * 
	 * @param editedItem
	 *            Edited item.
	 * @return The item index in array, return -1 if item doesn't exist.
	 */
	private int CheckIfItemAlreadyExistInArray(ItemEntity editedItem)
	{
		for (ItemEntity entity : itemChanged) {
			if (entity.getId() == editedItem.getId()) return itemChanged.indexOf(entity);
		}
		return -1;
	}

	/**
	 * Add/Edit the edited ItemEntity to itemChanged array.
	 * 
	 * @param rowData
	 *            Table row with edited item data.
	 */
	private void AddEditedItemToArray(CatalogItemRow rowData)
	{
		ItemEntity editedItem = new ItemEntity(rowData.getM_id(), rowData.getM_name(),
				ParseStringToProductType(rowData.getM_type()), rowData.getM_price(), rowData.getM_domainColor(),
				rowData.getM_image());
		int indexOfExistItemEntityInArray;
		if ((indexOfExistItemEntityInArray = CheckIfItemAlreadyExistInArray(editedItem)) != -1)
			itemChanged.set(indexOfExistItemEntityInArray, editedItem);
		else itemChanged.add(editedItem);
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
		switch (stringItemType) {
			case "Flower":
				return ProductType.Flower;
			case "FlowerPot":
				return ProductType.FlowerPot;
			case "BridalBouquet":
				return ProductType.BridalBouquet;
			case "FlowerArrangement":
				return ProductType.FlowerArrangement;
		}
		return null;
	}

	/**
	 * Insert data into table and show the updated table.
	 * 
	 */
	private void DrawContantToTable()
	{
		catalog_table.setItems(catalog);
		catalog_table.refresh();
	}

	/**
	 * Create error window for user.
	 * 
	 */
	private void ErrorMSG(String errorType)
	{
		Alert errorMessage = new Alert(AlertType.ERROR);
		errorMessage.setTitle("Error Message");
		errorMessage.setContentText(errorType);
		errorMessage.show();
	}

	/**
	 * Check that all fields are filed and valid.
	 * 
	 */
	private boolean CheckFields(TextField name, TextField price, TextField domainColor)
	{
		String inputedName, inputedPrice, inputedDomainColor;
		inputedName = name.getText();
		inputedPrice = price.getText();
		inputedDomainColor = domainColor.getText();

		if (inputedName == null || inputedPrice == null || inputedDomainColor == null) return false;
		if (inputedName.isEmpty() || inputedPrice.isEmpty() || inputedDomainColor.isEmpty()) return false;
		if (Double.parseDouble(inputedPrice) <= 0) {
			ErrorMSG("The price you entered lower then 0");
			m_logger.warning("Entered zero or negative price");
			return false;
		}
		return true;
	}

	/**
	 * Able access to save\reset buttons.
	 * 
	 */
	private void CatalogChanged()
	{
		button_save.setDisable(false);
		button_reset.setDisable(false);
	}

	/**
	 * Clean saved data arrays and disable access to save\reset button.
	 * 
	 */
	private void CleanSavedDataArray()
	{
		button_reset.setDisable(true);
		button_save.setDisable(true);
		itemAdded.clear();
		itemChanged.clear();
		itemRemoved.clear();
	}
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * Execute remove item procedure.
	 * 
	 */
	@FXML
	private void RemoveItemFromCatalog(ActionEvent event)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Remove Item From Catalog");
		dialog.setHeaderText("Please enter Item ID:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent()) return;

		String resultString = result.get();

		if (resultString == null || resultString.isEmpty()) {
			ErrorMSG("Invalid Input");
			return;
		}

		Integer idToRemove = Integer.parseInt(resultString);
		Integer idInTable;
		int i;

		for (i = 0; i < catalog.size(); i++) {
			idInTable = catalog.get(i).getM_id();
			if (idToRemove == idInTable) {
				itemRemoved.add(new ItemEntity(idToRemove, catalog.get(i).getM_name(),
						ParseStringToProductType(catalog.get(i).getM_type()), catalog.get(i).getM_price(),
						catalog.get(i).getM_domainColor(), catalog.get(i).getM_image()));
				catalog.remove(i);
				CatalogChanged();
				DrawContantToTable();
				return;
			}
		}
		ErrorMSG("Item ID doesn't exist");
	}

	/**
	 * Clean saved data arrays and get from server the previous catalog.
	 * 
	 */
	@FXML
	private void ResetChanges(ActionEvent event)
	{
		// TODO: get item list from server (re-init ObservableList)
		catalogInit();
		CleanSavedDataArray();
	}

	/**
	 * Execute add item procedure.
	 * 
	 */
	@FXML
	private void AddItemToCatalog(ActionEvent event)
	{
		Dialog<CatalogItemRow> addDialog = new Dialog<>();
		addDialog.setTitle("Add New Item Window");

		Label labelSubject = new Label("Add New Item Form");
		labelSubject.setFont(new Font(16));
		TextField textFieldName = new TextField();
		textFieldName.setPromptText("Enter Name");

		ObservableList<String> typeList = FXCollections.observableArrayList();
		typeList.add("Flower");
		typeList.add("FlowerPot");
		typeList.add("BridalBouquet");
		typeList.add("FlowerArrangement");
		ComboBox<String> comboBoxType = new ComboBox<>(typeList);
		comboBoxType.setValue("Flower");

		TextField textFieldPrice = new TextField();
		textFieldPrice.setPromptText("Enter Price");

		Button buttonImage = new Button("Add Image");
		TextField imagePath = new TextField();
		FileChooser addItemImage = new FileChooser();
		addItemImage.setTitle("Add Item Image");
		imagePath.setDisable(true);
		buttonImage.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event)
			{
				imagePath.setText(addItemImage.showOpenDialog(null).getAbsolutePath());
			}
		});

		TextField textFieldDomainColor = new TextField();
		textFieldDomainColor.setPromptText("Enter Domain Color");

		ButtonType buttonTypeOk = new ButtonType("Done", ButtonData.OK_DONE);
		addDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		addDialog.setResultConverter(new Callback<ButtonType, CatalogItemRow>() {

			@Override
			public CatalogItemRow call(ButtonType b)
			{
				if (b == buttonTypeOk) {
					if(!(CheckFields(textFieldName, textFieldPrice, textFieldDomainColor)))
					{
						ErrorMSG("One or more of fields is empty");
						m_logger.warning("AddItem - One or more of fields is empty");
					}
					CatalogItemRow newItem;
					Image newItemImage = null;

					try {
						File imageFile = new File(imagePath.getText());
						BufferedImage imageReader = ImageIO.read(imageFile);
						newItemImage = SwingFXUtils.toFXImage(imageReader, null);
					}
					catch (Exception e) {
					}

					if (newItemImage == null) {
						newItem = new CatalogItemRow(textFieldName.getText(), comboBoxType.getValue(),
								Double.parseDouble(textFieldPrice.getText()), textFieldDomainColor.getText());
					} else {
						newItem = new CatalogItemRow(textFieldName.getText(), comboBoxType.getValue(),
								Double.parseDouble(textFieldPrice.getText()), textFieldDomainColor.getText(),
								newItemImage);
					}

					itemAdded.add(
							new ItemEntity(textFieldName.getText(), ParseStringToProductType(comboBoxType.getValue()),
									Double.parseDouble(textFieldPrice.getText()), textFieldDomainColor.getText(),
									newItem.getM_image()));
					catalog.add(newItem);
					CatalogChanged();
					DrawContantToTable();
				}
				return null;
			}
		});

		GridPane dialogGrid = new GridPane();
		dialogGrid.add(labelSubject, 1, 2);
		dialogGrid.add(new Label("Name: "), 1, 6);
		dialogGrid.add(textFieldName, 2, 6);
		dialogGrid.add(new Label("Type: "), 1, 8);
		dialogGrid.add(comboBoxType, 2, 8);
		dialogGrid.add(new Label("Price: "), 1, 10);
		dialogGrid.add(textFieldPrice, 2, 10);
		dialogGrid.add(new Label("Domain Color: "), 1, 20);
		dialogGrid.add(textFieldDomainColor, 2, 20);
		dialogGrid.add(new Label("Image (Optional): "), 1, 22);
		dialogGrid.add(buttonImage, 2, 22);
		dialogGrid.add(new Label("Image Path:"), 1, 24);
		dialogGrid.add(imagePath, 2, 24);
		addDialog.getDialogPane().setContent(dialogGrid);

		Optional<CatalogItemRow> result = addDialog.showAndWait();
		if (!(result.isPresent())) return;
	}

	/**
	 * Send changes (add/remove/change) to server and update catalog.
	 * 
	 */
	@FXML
	private void SaveChanges(ActionEvent event)
	{
		for (ItemEntity entity : itemRemoved) {
			if (itemChanged.contains(entity)) itemChanged.remove(entity);
		}

		Message entityMessage;
//		for (ItemEntity entity : itemRemoved) {
//			entityMessage = MessagesFactory.createRemoveEntityMessage((IEntity) entity);
//			m_client.sendMessageToServer(entityMessage);
//		}
		
		for (ItemEntity entity : itemChanged) {
			entityMessage = MessagesFactory.createUpdateEntityMessage((IEntity) entity);
			m_client.sendMessageToServer(entityMessage);
		}
		for (ItemEntity entity : itemAdded) {
			entityMessage = MessagesFactory.createAddEntityMessage((IEntity) entity);
			m_client.sendMessageToServer(entityMessage);
		}

		// TODO: Send saved changes to server and re-init catalog
		catalogInit();
		CleanSavedDataArray();
	}

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
		catalog.clear();

		List<IEntity> entityList = ((EntitiesListData) entitiesListData).getEntities();
		for (IEntity entity : entityList) {
			if (!(entity instanceof ItemEntity)) {
				m_logger.warning("Received entity not of the type requested.");
				return;
			}

			ItemEntity item = (ItemEntity) entity;
			CatalogItemRow itemRow = new CatalogItemRow(item.getId(), item.getName(), item.getItemType().toString(),
					item.getItemPrice(), item.getColor(), item.getItemImage());
			catalog.add(itemRow);
		}

		DrawContantToTable();
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
		// TODO Shimon : Add event handling
	}

	/* End of --> Client handlers implementation region */
}
