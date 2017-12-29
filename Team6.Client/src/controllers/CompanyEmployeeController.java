
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
import messages.Message;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;
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

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
		m_client = ApplicationEntryPoint.Client;
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
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

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
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
		catalog.add(new CatalogItemRow(1, "Rose", "Flower", 19.99));
		catalog.add(new CatalogItemRow(2, "Rose2", "Flower2", 29.99));
		catalog.add(new CatalogItemRow(3, "Rose3", "Flower3", 39.99));
		catalog.add(new CatalogItemRow(4, "Rose4", "Flower4", 49.99));
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
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
										|| resultString.equals(ProductType.BridalBouquet.toString()))) {
									ErrorMSG("The type you entered doesn't exist");
									// TODO: log warning and exit method

								} else {
									rowData.setM_type(resultString);
								}
							break;
							case "price":
								Double price = Double.parseDouble(resultString);
								if (price <= 0) {
									ErrorMSG("The price you entered lower then 0");
									return;
									// TODO: log warning and exit method
								} else {
									rowData.setM_price(price);
								}

						}
						ProductType itemType = ParseStringToProductType(rowData.getM_type());
						itemChanged.add(new ItemEntity(rowData.getM_id(), rowData.getM_name(), itemType,
								rowData.getM_price(), rowData.getM_image()));
					} else if (event.getClickCount() == 2 && (!tableRow.isEmpty()) && pickType.equals("image")) {

						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Open Resource File");
						File selectedImage = fileChooser.showOpenDialog(null);

						try {
							BufferedImage imageReader = ImageIO.read(selectedImage);
							Image finalImage = SwingFXUtils.toFXImage(imageReader, null);
							rowData.setM_image(finalImage);
							ProductType itemType = ParseStringToProductType(rowData.getM_type());
							itemChanged.add(new ItemEntity(rowData.getM_id(), rowData.getM_name(), itemType,
									rowData.getM_price(), rowData.getM_image()));
						}
						catch (NullPointerException e) {
							ErrorMSG("The chosen file isn't image file");
							// TODO: log warning : chosen file isn't image file., and exit method
						}
						catch (Exception e) {
							// TODO: log warning and exit method
						}

					}
					drawContantToTable();
					CatalogChanged();
				}
			});
			return tableRow;
		});

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Double>("price"));
		tablecolumn_image.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, ImageView>("image"));

		drawContantToTable();
	}

	/**
	 * 
	 * M
	 *
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
		}
		return null;
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	private void drawContantToTable()
	{

		catalog_table.setItems(catalog);
		catalog_table.refresh();
	}

	private void ErrorMSG(String errorType)
	{
		Alert errorMessage = new Alert(AlertType.ERROR);
		errorMessage.setTitle("Error Message");
		errorMessage.setContentText(errorType);
		errorMessage.show();
	}

	private void CatalogChanged()
	{
		button_save.setDisable(false);
		button_reset.setDisable(false);
	}
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
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
				ProductType itemType = ParseStringToProductType(catalog.get(i).getM_type());
				itemRemoved.add(new ItemEntity(idToRemove, catalog.get(i).getM_name(), itemType,
						catalog.get(i).getM_price(), catalog.get(i).getM_image()));
				catalog.remove(i);
				drawContantToTable();
				return;
			}
		}
		ErrorMSG("Item ID doesn't exist");
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	@FXML
	private void ResetChanges(ActionEvent event)
	{
		// TODO: get item list from server (re-init ObservableList)
		button_reset.setDisable(true);
		button_save.setDisable(true);
		itemAdded.clear();
		itemChanged.clear();
		itemRemoved.clear();
	}

	@FXML
	private void AddItemToCatalog(ActionEvent event)
	{
		Dialog<CatalogItemRow> addDialog = new Dialog<>();
		addDialog.setTitle("Add New Item Window");

		Label labelSubject = new Label("Add New Item Form");
		labelSubject.setFont(new Font(16));
		TextField buttonName = new TextField();
		buttonName.setPromptText("Enter Name");

		ObservableList<String> typeList = FXCollections.observableArrayList();
		typeList.add("Flower");
		typeList.add("FlowerPot");
		typeList.add("BridalBouquet");
		ComboBox<String> comboBoxType = new ComboBox<>(typeList);
		comboBoxType.setValue("Flower");

		TextField buttonPrice = new TextField();
		buttonPrice.setPromptText("Enter Price");

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

		ButtonType buttonTypeOk = new ButtonType("Done", ButtonData.OK_DONE);
		addDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		addDialog.setResultConverter(new Callback<ButtonType, CatalogItemRow>() {

			@Override
			public CatalogItemRow call(ButtonType b)
			{
				if (b == buttonTypeOk) {
					CatalogItemRow newItem;
					Image newItemImage = null;

					try {
						File imageFile = new File(imagePath.getText());
						BufferedImage imageReader = ImageIO.read(imageFile);
						newItemImage = SwingFXUtils.toFXImage(imageReader, null);
					}
					catch (IllegalArgumentException e) {
						// TODO: log warning and !!continue!!
					}
					catch (Exception e) {
						// TODO: log warning and !!continue!!
					}

					if (newItemImage == null) {
						newItem = new CatalogItemRow(5, buttonName.getText(), comboBoxType.getValue(),
								Double.parseDouble(buttonPrice.getText()));
					} else {
						newItem = new CatalogItemRow(5, buttonName.getText(), comboBoxType.getValue(),
								Double.parseDouble(buttonPrice.getText()), newItemImage);
					}

					itemAdded.add(
							new ItemEntity(5, buttonName.getText(), ParseStringToProductType(comboBoxType.getValue()),
									Double.parseDouble(buttonPrice.getText()), newItem.getM_image()));
					catalog.add(newItem);
					CatalogChanged();
					drawContantToTable();
				}
				return null;
			}
		});

		GridPane dialogGrid = new GridPane();
		dialogGrid.add(labelSubject, 1, 2);
		dialogGrid.add(new Label("Name: "), 1, 6);
		dialogGrid.add(buttonName, 2, 6);
		dialogGrid.add(new Label("Type: "), 1, 8);
		dialogGrid.add(comboBoxType, 2, 8);
		dialogGrid.add(new Label("Price: "), 1, 10);
		dialogGrid.add(buttonPrice, 2, 10);
		dialogGrid.add(new Label("Image (Optional): "), 1, 20);
		dialogGrid.add(buttonImage, 2, 20);
		dialogGrid.add(new Label("Image Path:"), 1, 22);
		dialogGrid.add(imagePath, 2, 22);
		addDialog.getDialogPane().setContent(dialogGrid);

		Optional<CatalogItemRow> result = addDialog.showAndWait();
		if (!(result.isPresent())) return;
	}

	/**
	 * 
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 */
	@FXML
	private void SaveChanges(ActionEvent event)
	{
		// TODO: Send saved changes to server and re-init catalog
		button_reset.setDisable(true);
		button_save.setDisable(true);
		itemAdded.clear();
		itemChanged.clear();
		itemRemoved.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// TODO Shimon : Add event handling
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
