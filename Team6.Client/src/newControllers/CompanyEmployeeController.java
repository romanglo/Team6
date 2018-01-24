
package newControllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import newMessages.Message;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.EntityDataOperation;
import newMessages.IMessageData;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.Item;
import newEntities.ItemInShop;
import newEntities.ShopManager;
import javafx.application.Platform;
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
import newBoundaries.CatalogItemRow;

/**
 * CompanyEmployeeController : Managed the Company Employee UI.
 * 
 */
public class CompanyEmployeeController extends BaseController
{

	// TODO : Show message in Edit Catalog about add or remove item
	// success or failed.

	// region Actors Panes
	private @FXML AnchorPane anchorpane_editCatalog;

	private @FXML AnchorPane anchorpane_shopSales;
	// end region -> Actors Panes

	// region Fields Edit Catalog Stage
	/* Catalog table declaration */
	@FXML private TableView<CatalogItemRow> tableView_catalog;

	@FXML private TableColumn<CatalogItemRow, Integer> tableColumn_catalogItemID;

	@FXML private TableColumn<CatalogItemRow, String> tableColumn_catalogItemName;

	@FXML private TableColumn<CatalogItemRow, String> tableColumn_catalogItemType;

	@FXML private TableColumn<CatalogItemRow, Float> tableColumn_catalogItemPrice;

	@FXML private TableColumn<CatalogItemRow, ImageView> tableColumn_catalogItemImage;
	/* End of --> Catalog table declaration */

	/* Action catalog buttons */
	@FXML private Button button_addNewCatalogItem;

	@FXML private Button button_removeCatalogItem;

	@FXML private Button button_saveCatalogChanges;

	@FXML private Button button_resetCatalogChanges;
	/* End of --> Action catalog buttons */

	/* View and changes savers lists declaration */
	ObservableList<CatalogItemRow> catalog = FXCollections.observableArrayList();

	ArrayList<Item> itemsCatalogAdded = new ArrayList<>();

	ArrayList<Item> itemsCatalogRemoved = new ArrayList<>();

	ArrayList<Item> itemsCatalogChanged = new ArrayList<>();
	/* End of --> View and changes savers lists declaration */
	// end region -> Fields Edit Catalog Stage

	// region Fields Shop Sales Stage
	/* Catalog table declaration */
	@FXML private TableView<CatalogItemRow> tableView_shopSales;

	@FXML private TableColumn<CatalogItemRow, Integer> tableColumn_itemInSaleID;

	@FXML private TableColumn<CatalogItemRow, Float> tableColumn_discountedPrice;

	/* End of --> Catalog table declaration */

	/* Action catalog buttons */
	@FXML private Button button_addNewDiscountedItem;

	@FXML private Button button_removeDiscountedItem;

	@FXML private Button button_saveSalesChanges;

	@FXML private Button button_resetSalesChanges;

	@FXML private ComboBox<String> comboBox_storeList;
	/* End of --> Action catalog buttons */

	/* View and changes savers lists declaration */
	ObservableList<CatalogItemRow> shopSales = FXCollections.observableArrayList();

	ObservableList<String> stores = FXCollections.observableArrayList();

	ArrayList<ItemInShop> salesAdded = new ArrayList<>();

	ArrayList<ItemInShop> salesRemoved = new ArrayList<>();

	ArrayList<ItemInShop> salesChanged = new ArrayList<>();
	/* End of --> View and changes savers lists declaration */
	// end region -> Fields Shop Sales Stage

	// region BaseController Implementation

	// ----------------------------------------------------------------------------INIT-------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		getCatalogFromServer();
	}

	/**
	 * Initialize Edit Catalog table columns and define action when double click on
	 * table row.
	 * 
	 */
	private void initializeConfigurationTable()
	{
		tableView_catalog.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					Node selectedNode = event.getPickResult().getIntersectedNode();
					String pickType;
					CatalogItemRow rowData = tableRow.getItem();

					if (rowData.getId() == " ") {
						return;
					}
					if (selectedNode.getId() == null) {
						pickType = selectedNode.toString();
						pickType = pickType.substring(11, pickType.indexOf(',') - 1);

						// Classification by selected node.
						if (pickType.equals(rowData.getName())) pickType = "Name";
						else if (pickType.equals(rowData.getPrice())) pickType = "Price";
						else if (pickType.equals(rowData.getType())) pickType = "Type";
						else if (pickType.equals(rowData.getId())) pickType = "ID";
						else pickType = "Image";
					} else {
						String selected = selectedNode.getId();
						pickType = selected.substring(23, selected.length());
					}
					if (!(pickType.equals("ID") || pickType.equals("Image") || pickType.equals("Type"))) {
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
							case "Name":
								rowData.setM_name(resultString);
							break;
							case "Price":
								Float price = Float.parseFloat(resultString);
								if (price <= 0) {
									errorMSG("The price you entered lower then 0");
									return;
								} else {
									rowData.setM_price(price);
								}
						}
						addEditedItemToArray(rowData);
						catalogChanged();
					} else if (event.getClickCount() == 2 && (!tableRow.isEmpty()) && pickType.equals("Image")) {

						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Open Resource File");
						File selectedImage = fileChooser.showOpenDialog(null);

						try {
							BufferedImage imageReader = ImageIO.read(selectedImage);
							Image finalImage = SwingFXUtils.toFXImage(imageReader, null);
							rowData.setM_image(finalImage);
							addEditedItemToArray(rowData);
						}
						catch (Exception e) {
							return;
						}
						catalogChanged();

					} else if (event.getClickCount() == 2 && (!tableRow.isEmpty()) && pickType.equals("Type")) {
						Dialog<CatalogItemRow> addDialog = new Dialog<>();
						addDialog.setTitle("Edit Catalog Item Type");

						Label labelSubject = new Label("Do you want to update the item " + pickType + "  ?\n" + "ID: "
								+ rowData.getM_id() + " , Name: " + rowData.getM_name() + " , Type: "
								+ rowData.getM_type() + " , Price: " + rowData.getM_price());

						ObservableList<String> typeList = FXCollections.observableArrayList();
						typeList.add("Flower");
						typeList.add("FlowerPot");
						typeList.add("BridalBouquet");
						typeList.add("FlowerArrangement");
						ComboBox<String> comboBoxType = new ComboBox<>(typeList);
						comboBoxType.setValue(rowData.getType());

						ButtonType buttonTypeOk = new ButtonType("Done", ButtonData.OK_DONE);
						addDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
						addDialog.setResultConverter(new Callback<ButtonType, CatalogItemRow>() {

							@Override
							public CatalogItemRow call(ButtonType b)
							{
								if (b == buttonTypeOk) {
									String newType = comboBoxType.getValue();
									if (newType.equals(rowData.getM_type())) return null;
									rowData.setM_type(newType);
									addEditedItemToArray(rowData);
									catalogChanged();
									drawContantToTable();
								}
								return null;
							}
						});
						GridPane dialogGrid = new GridPane();
						dialogGrid.add(labelSubject, 1, 2);
						dialogGrid.add(new Label("\n"), 1, 4);
						dialogGrid.add(new Label("Types: "), 1, 6);
						dialogGrid.add(comboBoxType, 1, 8);
						addDialog.getDialogPane().setContent(dialogGrid);

						Optional<CatalogItemRow> result = addDialog.showAndWait();
						if (!(result.isPresent())) return;

					}
					drawContantToTable();
				}
			});
			return tableRow;
		});

		tableColumn_catalogItemID.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tableColumn_catalogItemName.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tableColumn_catalogItemType.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		tableColumn_catalogItemPrice.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
		tableColumn_catalogItemImage.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, ImageView>("image"));
		drawContantToTable();
	}

	/**
	 * Initialize Shop Sales table columns and define action when double click on
	 * table row.
	 *
	 */
	private void initializeConfigurationShopSalesTable()
	{
		tableView_shopSales.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();

					if (rowData.getId() == " ") {
						return;
					}

					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Update Item Discount");
					dialog.setHeaderText("Do you want to update the discount on " + rowData.getId() + '-'
							+ rowData.getPrice() + " ?");
					dialog.setContentText("Please enter the new value:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()) return;
					String resultString = result.get();
					if (!(resultString != null && !resultString.isEmpty()
							&& !resultString.equals(rowData.getPrice()))) {
						return;
					}

					Float discountedPrice;
					try {
						discountedPrice = Float.parseFloat(resultString);
					}
					catch (Exception e) {
						errorMSG("Invalid discounted price!");
						return;
					}
					if (discountedPrice <= 0) {
						errorMSG("The price you entered lower then 0");
						return;
					} else {
						rowData.setM_price(discountedPrice);
					}
					addNewSaleToArray(rowData);
					shopSalesCatalogChanged();
					drawContantToShopSalesTable();
				}
			});
			return tableRow;
		});

		tableColumn_itemInSaleID.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tableColumn_discountedPrice.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
		drawContantToShopSalesTable();
	}

	// ----------------------------------------------------------------------------INIT-------------------------------------------------------------

	// ----------------------------------------------------------------------------PRIVATE-------------------------------------------------------------

	/**
	 * Search for edited Item in itemChanged array.
	 * 
	 * @param editedItem
	 *            Edited item.
	 * @return The item index in array, return -1 if item doesn't exist.
	 */
	private int checkIfItemAlreadyExistInArray(Item editedItem)
	{
		for (Item entity : itemsCatalogChanged) {
			if (entity.getId() == editedItem.getId()) return itemsCatalogChanged.indexOf(entity);
		}
		return -1;
	}

	/**
	 * Add/Edit the edited ItemEntity to itemChanged array.
	 * 
	 * @param rowData
	 *            Table row with edited item data.
	 */
	private void addEditedItemToArray(CatalogItemRow rowData)
	{
		Item editedItem = new Item();
		editedItem.setId(rowData.getM_id());
		editedItem.setName(rowData.getM_name());
		editedItem.setType(parseStringToProductType(rowData.getM_type()));
		editedItem.setPrice(rowData.getM_price());
		editedItem.setDomainColor(rowData.getM_domainColor());
		if (rowData.getM_image() != null) editedItem.setImage(rowData.getM_image());
		int indexOfExistItemEntityInArray;
		if ((indexOfExistItemEntityInArray = checkIfItemAlreadyExistInArray(editedItem)) != -1)
			itemsCatalogChanged.set(indexOfExistItemEntityInArray, editedItem);
		else itemsCatalogChanged.add(editedItem);
	}

	/**
	 * Parse string to the product type.
	 * 
	 * @param stringItemType
	 *            Input string.
	 * @return The product type.
	 */
	private EntitiesEnums.ProductType parseStringToProductType(String stringItemType)
	{
		switch (stringItemType) {
			case "Flower":
				return EntitiesEnums.ProductType.Flower;
			case "FlowerPot":
				return EntitiesEnums.ProductType.FlowerPot;
			case "BridalBouquet":
				return EntitiesEnums.ProductType.BridalBouquet;
			case "FlowerArrangement":
				return EntitiesEnums.ProductType.FlowerArrangement;
		}
		return null;
	}

	/**
	 * Insert data into table and show the updated table.
	 * 
	 */
	private void drawContantToTable()
	{
		tableView_catalog.setItems(catalog);
		tableView_catalog.refresh();

	}

	/**
	 * Search for edited item sale in itemChanged array.
	 * 
	 * @param editedSale
	 *            Edited item sale.
	 * @return The item index in array, return -1 if item doesn't exist.
	 */
	private int checkIfItemAlreadySaleExistInArray(ItemInShop editedSale)
	{
		for (ItemInShop entity : salesChanged) {
			if (entity.getItemId() == editedSale.getItemId()) return salesChanged.indexOf(entity);
		}
		return -1;
	}

	/**
	 * Add/Edit the edited ItemEntity to itemChanged array.
	 * 
	 * @param rowData
	 *            Table row with edited item data.
	 */
	private void addNewSaleToArray(CatalogItemRow rowData)
	{
		ItemInShop editedItem = new ItemInShop();
		editedItem.setItemId(rowData.getM_id());
		editedItem.setDiscountedPrice(rowData.getM_price());
		editedItem.setShopManagerId(stringShopIdToInt(comboBox_storeList.getValue()));
		int indexOfExistItemEntityInArray;
		if ((indexOfExistItemEntityInArray = checkIfItemAlreadySaleExistInArray(editedItem)) != -1)
			salesChanged.set(indexOfExistItemEntityInArray, editedItem);
		else salesChanged.add(editedItem);
	}

	/**
	 * Insert data into table and show the updated table.
	 * 
	 */
	private void drawContantToShopSalesTable()
	{
		tableView_shopSales.setItems(shopSales);
		tableView_shopSales.refresh();
	}

	/**
	 * Check that all fields are filed and valid.
	 *
	 * @param id
	 *            The discounted item.
	 * @param discountedPrice
	 *            The discounted price.
	 * @return <code>true</code> if all fields are filled and with valid values,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAddNewSaleFields(TextField id, TextField discountedPrice)
	{
		String inputedID, inputedDiscountedPrice;
		inputedID = id.getText();
		inputedDiscountedPrice = discountedPrice.getText();

		if (inputedID == null || inputedDiscountedPrice == null) {
			errorMSG("One or more fields are empty");
			return false;
		}
		if (inputedID.isEmpty() || inputedDiscountedPrice.isEmpty()) {
			errorMSG("One or more fields are empty");
			return false;
		}

		Float newPrice;
		Integer itemID;
		try {
			itemID = Integer.parseInt(inputedID);
			newPrice = Float.parseFloat(inputedDiscountedPrice);
		}
		catch (Exception ex) {
			errorMSG("Invalid input!");
			return false;
		}

		if (itemID <= 0) {
			errorMSG("The ID you entered lower then 0");
			return false;
		}

		for (CatalogItemRow item : shopSales) {
			if (item.getM_id() == itemID) {
				errorMSG("Item discount already exist!");
				return false;
			}
		}

		if (newPrice <= 0) {
			errorMSG("The price you entered lower then 0");
			return false;
		}
		return true;
	}

	/**
	 * Check if the item is exist in comapny catalog.
	 *
	 * @param itemID
	 *            The item ID.
	 * @return <code>true</code> if the item exist in company catalog,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkIfNewItemExistInCatalog(String itemID)
	{
		for (CatalogItemRow item : catalog)
			if (itemID.equals(item.getId())) return true;
		errorMSG("Item ID doesn't exist in catalog!");
		return false;
	}

	/**
	 * Able access to save\reset buttons.
	 * 
	 */
	private void shopSalesCatalogChanged()
	{
		button_saveSalesChanges.setDisable(false);
		button_resetSalesChanges.setDisable(false);
	}

	/**
	 * Clean saved data arrays and disable access to save\reset button.
	 * 
	 */
	private void cleanSavedDataShopSalesArray()
	{
		button_resetSalesChanges.setDisable(true);
		button_saveSalesChanges.setDisable(true);
		salesAdded.clear();
		salesChanged.clear();
		salesRemoved.clear();
	}

	/**
	 * Clean all local changes arrays.
	 *
	 */
	private void cleanEditCatalogVariables()
	{
		itemsCatalogAdded.clear();
		itemsCatalogChanged.clear();
		itemsCatalogRemoved.clear();
	}

	/**
	 * Clean all Shop Sales variables.
	 *
	 */
	private void cleanShopSalesVariables()
	{
		tableView_shopSales.getItems().clear();
		comboBox_storeList.getItems().clear();
		stores.clear();
		shopSales.clear();
		salesAdded.clear();
		salesChanged.clear();
		salesRemoved.clear();
	}

	/**
	 * Create error window for user.
	 *
	 * @param errorType
	 *            The error.
	 */
	private void errorMSG(String errorType)
	{
		Platform.runLater(() -> {
			Alert errorMessage = new Alert(AlertType.ERROR);
			errorMessage.setTitle("Error Message");
			errorMessage.setContentText(errorType);
			errorMessage.show();
		});
	}

	/**
	 * Check that all fields are filed and valid.
	 *
	 * @param name
	 *            The item name.
	 * @param price
	 *            The item price.
	 * @param domainColor
	 *            The item domain color.
	 * @return <code>true</code> if the item exist in company catalog,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAddNewCatalogItemFields(TextField name, TextField price, TextField domainColor)
	{
		String inputedName, inputedPrice, inputedDomainColor;
		inputedName = name.getText();
		inputedPrice = price.getText();
		inputedDomainColor = domainColor.getText();

		if (inputedName == null || inputedPrice == null || inputedDomainColor == null) return false;
		if (inputedName.isEmpty() || inputedPrice.isEmpty() || inputedDomainColor.isEmpty()) return false;
		try {
			Float.parseFloat(inputedPrice);
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * Able access to save\reset buttons.
	 * 
	 */
	private void catalogChanged()
	{
		button_saveCatalogChanges.setDisable(false);
		button_resetCatalogChanges.setDisable(false);
	}

	/**
	 * Clean saved data arrays and disable access to save\reset button.
	 * 
	 */
	private void cleanSavedDataArray()
	{
		button_resetCatalogChanges.setDisable(true);
		button_saveCatalogChanges.setDisable(true);
		itemsCatalogAdded.clear();
		itemsCatalogChanged.clear();
		itemsCatalogRemoved.clear();
	}

	/**
	 * Initialize company catalog values.
	 *
	 * @param catalogItems
	 *            The catalog items.
	 */
	private void companyCatalogInit(List<IEntity> catalogItems)
	{
		for (IEntity entity : catalogItems) {
			if (!(entity instanceof Item)) {
				m_Logger.warning("Received entity not of the type requested.");
				return;
			}
			Item item = (Item) entity;
			CatalogItemRow itemRow = new CatalogItemRow(item.getId(), item.getName(), item.getType().toString(),
					item.getPrice(), item.getDomainColor(), item.getImage());
			catalog.add(itemRow);
		}
	}

	/**
	 * Parse the shop full name to shop ID.
	 *
	 * @param shopID
	 *            The shop full name.
	 * @return The shop ID.
	 */
	private int stringShopIdToInt(String shopID)
	{
		shopID = shopID.substring(0, shopID.indexOf(' '));
		return Integer.parseInt(shopID);
	}

	// ----------------------------------------------------------------------------PRIVATE-------------------------------------------------------------

	// ----------------------------------------------------------------------------FXML-------------------------------------------------------------

	/**
	 * Activated when "Remove catalog item" button pressed. Execute remove item from
	 * catalog procedure.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void removeItemFromCatalog(ActionEvent event)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Remove Item From Catalog");
		dialog.setHeaderText("Please enter Item ID:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent()) return;

		String resultString = result.get();

		if (resultString == null || resultString.isEmpty()) {
			errorMSG("Invalid Input");
			return;
		}

		Integer idToRemove = Integer.parseInt(resultString);
		if (idToRemove <= 0) {
			errorMSG("Invalid ID");
			return;
		}

		Integer idInTable;
		int i;

		for (i = 0; i < catalog.size(); i++) {
			idInTable = catalog.get(i).getM_id();
			if (idToRemove == idInTable) {
				Item itemToRemove = new Item();
				itemToRemove.setId(idToRemove);
				itemToRemove.setName(catalog.get(i).getM_name());
				itemToRemove.setType(parseStringToProductType(catalog.get(i).getM_type()));
				itemToRemove.setPrice(catalog.get(i).getM_price());
				itemToRemove.setDomainColor(catalog.get(i).getM_domainColor());
				if (catalog.get(i).getM_image() != null) itemToRemove.setImage(catalog.get(i).getM_image());
				itemsCatalogRemoved.add(itemToRemove);
				catalog.remove(i);
				catalogChanged();
				drawContantToTable();
				return;
			}
		}
		errorMSG("Item ID doesn't exist");
	}

	/**
	 * Activated when "Reset Changes" button pressed. Clean saved data arrays and
	 * get from server the previous catalog.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void resetChanges(ActionEvent event)
	{
		getCatalogFromServer();
		cleanSavedDataArray();
	}

	/**
	 * Activated when "Add catalog item" button pressed. Execute add item to catalog
	 * procedure.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void addItemToCatalog(ActionEvent event)
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
				File f = addItemImage.showOpenDialog(null);
				if (f != null) imagePath.setText(f.getAbsolutePath());
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
					if (!(checkAddNewCatalogItemFields(textFieldName, textFieldPrice, textFieldDomainColor))) {
						try {
							if (!((textFieldPrice.getText()).isEmpty())) {
								Float floatPrice = Float.parseFloat(textFieldPrice.getText());
								if (floatPrice <= 0) {
									errorMSG("The entered price lower than 0!");
									return null;
								}

							}
						}
						catch (Exception ex) {
							errorMSG("Invalid price!");
							return null;
						}
						errorMSG("One or more of fields is empty!");
						return null;
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

					Item itemToAdd = new Item();
					if (newItemImage == null) {
						newItem = new CatalogItemRow(textFieldName.getText(), comboBoxType.getValue(),
								Float.parseFloat(textFieldPrice.getText()), textFieldDomainColor.getText());
					} else {
						newItem = new CatalogItemRow(textFieldName.getText(), comboBoxType.getValue(),
								Float.parseFloat(textFieldPrice.getText()), textFieldDomainColor.getText(),
								newItemImage);
					}
					itemToAdd.setImage(newItem.getM_image());
					itemToAdd.setName(textFieldName.getText());
					itemToAdd.setType(parseStringToProductType(comboBoxType.getValue()));
					itemToAdd.setPrice(Float.parseFloat(textFieldPrice.getText()));
					itemToAdd.setDomainColor(textFieldDomainColor.getText());
					itemsCatalogAdded.add(itemToAdd);
					catalog.add(newItem);
					catalogChanged();
					drawContantToTable();
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
	 * Activated when "Save Changes" button pressed. Send changes
	 * (add/remove/change) to server and update catalog.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void saveChanges(ActionEvent event)
	{
		for (Item entity : itemsCatalogRemoved) {
			if (itemsCatalogChanged.contains(entity)) itemsCatalogChanged.remove(entity);
		}

		Message entityMessage;
		ArrayList<IEntity> transferedEntities = new ArrayList<>();
		if (itemsCatalogRemoved.size() > 0) {
			for (Item entity : itemsCatalogRemoved) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createRemoveEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		if (itemsCatalogChanged.size() > 0) {
			transferedEntities.clear();
			for (Item entity : itemsCatalogChanged) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createUpdateEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		if (itemsCatalogAdded.size() > 0) {
			transferedEntities.clear();
			for (Item entity : itemsCatalogAdded) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createAddEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		getCatalogFromServer();
		cleanSavedDataArray();
	}

	/**
	 * Activated when shop ComboBox value changed. Send Get message for all selected
	 * shop sales.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void selectShop(ActionEvent event)
	{
		if (comboBox_storeList.getItems().isEmpty()) return;
		button_addNewDiscountedItem.setDisable(false);
		button_removeDiscountedItem.setDisable(false);
		int shop_number = stringShopIdToInt(comboBox_storeList.getValue());
		getShopSalesFromServer(shop_number);
	}

	/**
	 * Activated when "Remove discount" button pressed. Execute remove item
	 * procedure.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void removeItemFromSalesCatalog(ActionEvent event)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Remove Item Discount From Shop Catalog");
		dialog.setHeaderText("Please enter Item ID:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent()) return;

		String resultString = result.get();

		if (resultString == null || resultString.isEmpty()) {
			errorMSG("Invalid Input");
			return;
		}

		Integer idToRemove = Integer.parseInt(resultString);
		if (idToRemove <= 0) {
			errorMSG("Invalid ID");
			return;
		}

		Integer idInTable;
		int i;

		for (i = 0; i < shopSales.size(); i++) {
			idInTable = shopSales.get(i).getM_id();
			if (idToRemove == idInTable) {
				ItemInShop itemToRemove = new ItemInShop();
				itemToRemove.setItemId(idToRemove);
				itemToRemove.setDiscountedPrice(shopSales.get(i).getM_price());
				itemToRemove.setShopManagerId(stringShopIdToInt(comboBox_storeList.getValue()));
				salesRemoved.add(itemToRemove);
				shopSales.remove(i);
				shopSalesCatalogChanged();
				drawContantToShopSalesTable();
				return;
			}
		}
		errorMSG("Item ID doesn't exist");
	}

	/**
	 * Activated when "Reset Changed" button pressed. Clean saved data arrays and
	 * get from server the previous catalog.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void resetSalesCatalogChanges(ActionEvent event)
	{
		int shopID = stringShopIdToInt(comboBox_storeList.getValue());
		getShopSalesFromServer(shopID);
		cleanSavedDataShopSalesArray();
	}

	/**
	 * Activated when "Add new discount" button pressed. Execute add item procedure.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void addSalesToCatalog(ActionEvent event)
	{
		Dialog<CatalogItemRow> addDialog = new Dialog<>();
		addDialog.setTitle("Add New Discount");

		Label labelSubject = new Label("Add New Item Discount");
		labelSubject.setFont(new Font(16));

		TextField textFieldID = new TextField();
		textFieldID.setPromptText("Enter Item ID");

		TextField textFieldDiscountedPrice = new TextField();
		textFieldDiscountedPrice.setPromptText("Enter Discounted Price");

		ButtonType buttonTypeOk = new ButtonType("Done", ButtonData.OK_DONE);
		addDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		addDialog.setResultConverter(new Callback<ButtonType, CatalogItemRow>() {

			@Override
			public CatalogItemRow call(ButtonType b)
			{
				if (b == buttonTypeOk) {
					if (!(checkAddNewSaleFields(textFieldID, textFieldDiscountedPrice))) return null;
					if (!(checkIfNewItemExistInCatalog(textFieldID.getText()))) return null;
					CatalogItemRow newItem;
					ItemInShop itemToAdd = new ItemInShop();
					newItem = new CatalogItemRow(Integer.parseInt(textFieldID.getText()),
							Float.parseFloat(textFieldDiscountedPrice.getText()));

					itemToAdd.setShopManagerId(stringShopIdToInt(comboBox_storeList.getValue()));
					itemToAdd.setItemId(Integer.parseInt(textFieldID.getText()));
					itemToAdd.setDiscountedPrice(Float.parseFloat(textFieldDiscountedPrice.getText()));
					salesAdded.add(itemToAdd);
					shopSales.add(newItem);
					shopSalesCatalogChanged();
					drawContantToShopSalesTable();
				}
				return null;
			}
		});

		GridPane dialogGrid = new GridPane();
		dialogGrid.add(labelSubject, 1, 2);
		dialogGrid.add(new Label("Item ID: "), 1, 4);
		dialogGrid.add(textFieldID, 2, 4);
		dialogGrid.add(new Label("Discounted Price: "), 1, 6);
		dialogGrid.add(textFieldDiscountedPrice, 2, 6);
		addDialog.getDialogPane().setContent(dialogGrid);

		Optional<CatalogItemRow> result = addDialog.showAndWait();
		if (!(result.isPresent())) return;
	}

	/**
	 * Activated when "Add new discount" button pressed. Send changes
	 * (add/remove/change) to server and update catalog.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void saveSalesCatalogChanges(ActionEvent event)
	{
		for (ItemInShop entity : salesRemoved) {
			if (salesChanged.contains(entity)) salesChanged.remove(entity);
		}

		Message entityMessage;
		ArrayList<IEntity> transferedEntities = new ArrayList<>();
		if (salesRemoved.size() > 0) {
			for (ItemInShop entity : salesRemoved) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createRemoveEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		if (salesChanged.size() > 0) {
			transferedEntities.clear();
			for (ItemInShop entity : salesChanged) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createUpdateEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		if (salesAdded.size() > 0) {
			transferedEntities.clear();
			for (ItemInShop entity : salesAdded) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createAddEntitiesMessage(transferedEntities);
			m_Client.sendMessageToServer(entityMessage);
		}

		int shopID = stringShopIdToInt(comboBox_storeList.getValue());
		getShopSalesFromServer(shopID);
		cleanSavedDataShopSalesArray();
	}

	// ----------------------------------------------------------------------------FXML-------------------------------------------------------------

	// ----------------------------------------------------------------------------SERVER-COMMUNICATION---------------------------------------------

	/**
	 * Send get all message to server for all store company.
	 *
	 */
	private void getStoreList()
	{
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(new ShopManager());
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Send get all message to server for all current shop sales.
	 *
	 * @param shopID
	 *            The current shop.
	 */
	private void getShopSalesFromServer(int shopID)
	{
		ItemInShop item = new ItemInShop();
		item.setShopManagerId(shopID);
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(item);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Send get all message to server for all catalog items.
	 *
	 */
	private void getCatalogFromServer()
	{
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(new Item());
		m_Client.sendMessageToServer(entityMessage);
	}

	// ----------------------------------------------------------------------------SERVER-COMMUNICATION---------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		if (title == "Edit Catalog" && anchorpane_editCatalog.isVisible()) return true;
		else if (title == "Shop Sales" && anchorpane_shopSales.isVisible()) return true;
		switch (title) {
			case "Edit Catalog":
				cleanShopSalesVariables();
				getCatalogFromServer();
				initializeConfigurationTable();
				anchorpane_editCatalog.setVisible(true);
				anchorpane_shopSales.setVisible(false);
			break;

			case "Shop Sales":
				cleanEditCatalogVariables();
				getStoreList();
				initializeConfigurationShopSalesTable();
				anchorpane_editCatalog.setVisible(false);
				anchorpane_shopSales.setVisible(true);
			break;
			default:
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getSideButtonsNames()
	{
		return new String[] { "Edit Catalog", "Shop Sales" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (!(messageData instanceof EntitiesListData) && !(messageData instanceof RespondMessageData)) {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}

		if (anchorpane_editCatalog.isVisible()) {
			if (messageData instanceof EntitiesListData) {
				catalog.clear();
				Platform.runLater(() -> {
					companyCatalogInit(((EntitiesListData) messageData).getEntities());
				});
				drawContantToTable();
			} else if (messageData instanceof RespondMessageData) {
				RespondMessageData respondMessageData = (RespondMessageData) messageData;
				boolean succeed = respondMessageData.isSucceed();
				EntityData respondedMessageData = (EntityData) respondMessageData.getMessageData();
				if (!succeed) {
					if (respondedMessageData.getOperation() == EntityDataOperation.GetALL) catalog.clear();
					errorMSG(respondedMessageData.getOperation().toString() + " Failed!");
				} else {
					showInformationMessage("Catalog update successfully!");
				}
			}
		} else if (anchorpane_shopSales.isVisible()) {
			if (messageData instanceof EntitiesListData) {
				List<IEntity> entityList = ((EntitiesListData) messageData).getEntities();
				if (entityList.isEmpty()) return;
				if (entityList.get(0) instanceof ShopManager) {
					String shop;
					ShopManager shopID;
					for (IEntity entity : entityList) {
						if (!(entity instanceof ShopManager)) {
							m_Logger.warning("Failed to get ");
						}
						shopID = (ShopManager) entity;
						shop = shopID.getId() + " - " + shopID.getName();
						stores.add(shop);
					}
					comboBox_storeList.setItems(stores);
				} else {
					shopSales.clear();
					for (IEntity entity : entityList) {
						if (!(entity instanceof ItemInShop)) {
							m_Logger.warning("Received entity not of the type requested.");
							return;
						}
						ItemInShop item = (ItemInShop) entity;
						CatalogItemRow itemRow = new CatalogItemRow(item.getItemId(), item.getDiscountedPrice());
						shopSales.add(itemRow);
					}
					drawContantToShopSalesTable();
				}
			} else if (messageData instanceof RespondMessageData) {
				RespondMessageData respondMessageData = (RespondMessageData) messageData;
				boolean succeed = respondMessageData.isSucceed();
				if (respondMessageData.getMessageData() instanceof EntityData) {
					if (((EntityData) respondMessageData.getMessageData()).getEntity() instanceof ItemInShop) {
						if (!succeed) {
							Platform.runLater(() -> {
								showInformationMessage("There are no discounts for this shop");
							});
							shopSales.clear();
							drawContantToShopSalesTable();
						}
					} else {
						showInformationMessage("Shop sales update successfully!");
					}
				}
			}
		} else {
			if (messageData instanceof EntitiesListData) {
				catalog.clear();
				Platform.runLater(() -> {
					companyCatalogInit(((EntitiesListData) messageData).getEntities());
				});
			}
		}
	}

	// end region -> BaseController Implementation
}
