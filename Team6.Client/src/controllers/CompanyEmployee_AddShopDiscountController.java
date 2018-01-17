
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import logger.LogManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import newEntities.IEntity;
import newEntities.ItemInShop;
import newEntities.ShopManager;
import boundaries.CatalogItemRow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;

/**
 *
 * CompanyEmployeeController : TODO Shimon: Create class description
 * 
 */
public class CompanyEmployee_AddShopDiscountController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

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

	@FXML private Label label_shopID;

	@FXML private ComboBox<Integer> comboBox_storeList;
	/* End of --> Action catalog buttons */

	/* View and changes savers lists declaration */
	ObservableList<CatalogItemRow> shopSales = FXCollections.observableArrayList();

	ObservableList<Integer> stores = FXCollections.observableArrayList();

	ArrayList<ItemInShop> salesAdded = new ArrayList<>();

	ArrayList<ItemInShop> salesRemoved = new ArrayList<>();

	ArrayList<ItemInShop> salesChanged = new ArrayList<>();
	/* End of --> View and changes savers lists declaration */

	/* UI events region */

	@FXML
	private void selectShop(ActionEvent event)
	{
		int shop_number = comboBox_storeList.getValue();
		getShopSalesFromServer(shop_number);
		ItemInShop entity = new ItemInShop();
		entity.setShopManagerId(shop_number);
		Message msg = MessagesFactory.createGetAllEntityMessage(entity);
		m_client.sendMessageToServer(msg);
	}

	/* Initializing methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		initializeFields();
		initializeClientHandler();
		getStoreList();
		initializeConfigurationShopSalesTable();
	}

	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
		m_client = ApplicationEntryPoint.Client;
	}

	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
	}

	private void getStoreList()
	{
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(new ShopManager());
		m_client.sendMessageToServer(entityMessage);
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	private void getShopSalesFromServer(int shopID)
	{
		ItemInShop item = new ItemInShop();
		item.setShopManagerId(shopID);
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(item);
		m_client.sendMessageToServer(entityMessage);
		label_shopID.setText(Integer.toString(shopID));
	}

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
						m_logger.warning("Failed to read discounted price");
						errorMSG("Invalid discounted price!");
						return;
					}
					if (discountedPrice <= 0) {
						errorMSG("The price you entered lower then 0");
						m_logger.warning("Entered zero or negative price");
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
		editedItem.setShopManagerId(Integer.parseInt(label_shopID.getText()));
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
	 * Create error window for user.
	 * 
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
	 */
	private boolean checkAddNewSaleFields(TextField id, TextField discountedPrice)
	{
		String inputedID, inputedDiscountedPrice;
		inputedID = id.getText();
		inputedDiscountedPrice = discountedPrice.getText();

		if (inputedID == null || inputedDiscountedPrice == null) {
			errorMSG("One or more fields are empty");
			m_logger.warning("AddItem - One or more fields are empty");
			return false;
		}
		if (inputedID.isEmpty() || inputedDiscountedPrice.isEmpty()) {
			errorMSG("One or more fields are empty");
			m_logger.warning("AddItem - One or more fields are empty");
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
			m_logger.warning("Entered invalid values");
			return false;
		}

		if (itemID <= 0) {
			errorMSG("The ID you entered lower then 0");
			m_logger.warning("Entered zero or negative ID");
			return false;
		}

		for (CatalogItemRow item : shopSales) {
			if (item.getM_id() == itemID) {
				errorMSG("Item discount already exist!");
				m_logger.warning("Entered existed item id");
				return false;
			}
		}

		if (newPrice <= 0) {
			errorMSG("The price you entered lower then 0");
			m_logger.warning("Entered zero or negative price");
			return false;
		}
		return true;
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
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * Execute remove item procedure.
	 * 
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
				;
				itemToRemove.setDiscountedPrice(shopSales.get(i).getM_price());
				itemToRemove.setShopManagerId(Integer.parseInt((label_shopID.getText())));
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
	 * Clean saved data arrays and get from server the previous catalog.
	 * 
	 */
	@FXML
	private void resetSalesCatalogChanges(ActionEvent event)
	{
		int shopID = comboBox_storeList.getValue();
		getShopSalesFromServer(shopID);
		cleanSavedDataShopSalesArray();
		label_shopID.setText("shopID");
	}

	/**
	 * Execute add item procedure.
	 * 
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

					CatalogItemRow newItem;
					ItemInShop itemToAdd = new ItemInShop();
					newItem = new CatalogItemRow(Integer.parseInt(textFieldID.getText()),
							Float.parseFloat(textFieldDiscountedPrice.getText()));

					itemToAdd.setShopManagerId(comboBox_storeList.getValue());
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
	 * Send changes (add/remove/change) to server and update catalog.
	 * 
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
			m_client.sendMessageToServer(entityMessage);
		}

		if (salesChanged.size() > 0) {
			transferedEntities.clear();
			for (ItemInShop entity : salesChanged) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createUpdateEntitiesMessage(transferedEntities);
			m_client.sendMessageToServer(entityMessage);
		}

		if (salesAdded.size() > 0) {
			transferedEntities.clear();
			for (ItemInShop entity : salesAdded) {
				transferedEntities.add(entity);
			}
			entityMessage = MessagesFactory.createAddEntitiesMessage(transferedEntities);
			m_client.sendMessageToServer(entityMessage);
		}

		int shopID = comboBox_storeList.getValue();
		getShopSalesFromServer(shopID);
		cleanSavedDataShopSalesArray();
	}

	private void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (!(messageData instanceof EntitiesListData) && !(messageData instanceof RespondMessageData)) {
			m_logger.warning("Received message data not of the type requested.");
			return;
		}

		if (messageData instanceof EntitiesListData) {
			List<IEntity> entityList = ((EntitiesListData) messageData).getEntities();
			if (entityList.isEmpty()) return;
			if (entityList.get(0) instanceof ShopManager) {
				ShopManager shopID;
				for (IEntity entity : entityList) {
					if (!(entity instanceof ShopManager)) {
						m_logger.warning("Failed to get ");
					}
					shopID = (ShopManager) entity;
					stores.add(shopID.getId());
				}
				comboBox_storeList.setItems(stores);
			} else {
				shopSales.clear();
				for (IEntity entity : entityList) {
					if (!(entity instanceof ItemInShop)) {
						m_logger.warning("Received entity not of the type requested.");
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
							showInformationMessage("There are no discaonts for this shop");
						});
						shopSales.clear();
						drawContantToShopSalesTable();
					}
				}
			}
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
		// TODO Shimon : Add event handling
	}

	/* End of --> Client handlers implementation region */
}
