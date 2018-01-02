
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.CatalogItemRow;
import client.ApplicationEntryPoint;
import client.Client;
import entities.CostumerSubscription;
import entities.ItemEntity;
import entities.ProductType;
import entities.ReservationDeliveryType;
import entities.ReservationEntity;
import entities.ReservationType;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;
import messages.MessagesFactory;

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

	@FXML private Button changing_button;

	@FXML private Label total_price_label;

	// end region -> UI Binding Fields

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	ObservableList<CatalogItemRow> paymentBill = FXCollections.observableArrayList();

	private Double m_discount;

	/**
	 * When displaying the canceling reservation screen, manage the reservation.
	 */
	public static ReservationEntity s_reservationEntity = null;

	/**
	 * Define whether the window is for the reservation creation or for canceling
	 * reservation.
	 */
	public static boolean s_isCreateReservation;

	/* End of --> Fields region */

	// region Private Methods

	@FXML
	private void backButtonClick(ActionEvent backEvent)
	{
		openSelectedWindow(backEvent, "/boundaries/Costumer_CreateReservation.fxml");
	}

	@FXML
	private void changingButtonClick(ActionEvent changingEvent)
	{
		if (s_isCreateReservation) {
			paymentButtonClick(changingEvent);
		} else {
			cancelButtonClick(changingEvent);
		}
	}

	private void paymentButtonClick(ActionEvent paymentEvent)
	{
		Costumer_SavedData.setReservationList();
		Costumer_SavedData.setTotalPrice(Double.parseDouble(total_price_label.getText()));
		/* Check if the user want a blessing card and request a delivery date. */
		confirmReservation();
		
		if (Costumer_SavedData.getSubscription() == CostumerSubscription.None) {
			Costumer_PaymentCreditCardController.m_discount = m_discount;
			openSelectedWindow(paymentEvent, "/boundaries/Costumer_PaymentCreditCard.fxml");
		} else {
			Costumer_SavedData.setRefund(Costumer_SavedData.getCostumerRefund() - m_discount);

			Message entityMessage = MessagesFactory.createAddEntityMessage(Costumer_SavedData.getReservationEntity());
			m_client.sendMessageToServer(entityMessage);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Reservation Completed");
			alert.setHeaderText(null);
			alert.setContentText("Reservation completed. Thanks for using our application.");
			alert.showAndWait();
			openSelectedWindow(paymentEvent, "/boundaries/Costumer_CreateReservation.fxml");
		}
	}

	private void confirmReservation()
	{
		confirmBlessingCard();
		confirmDelivery();
	}

	private void confirmDelivery()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add blessing card to reservation");
		alert.setHeaderText("Please choose the delivery method");
		alert.setContentText(null);

		CheckBox deliveryImmidiate = new CheckBox("Immidiate delivery");
		DatePicker deliveryDate = new DatePicker(LocalDate.now());

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(deliveryImmidiate, 0, 0);
		expContent.add(deliveryDate, 1, 0);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();

		if (deliveryImmidiate.isSelected()) {
			Costumer_SavedData.setDeliveryDate(Date.valueOf(LocalDate.now()));
			Costumer_SavedData.setDeliveryType(ReservationDeliveryType.Immidiate);
		} else {
			Costumer_SavedData.setDeliveryDate(Date.valueOf(deliveryDate.getValue()));
			Costumer_SavedData.setDeliveryType(ReservationDeliveryType.Future);
		}
	}

	private void confirmBlessingCard()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add blessing card to reservation");
		alert.setHeaderText(null);
		alert.setContentText(null);

		CheckBox blessingCheckBox = new CheckBox("Add blessing card");
		TextArea blessingTextArea = new TextArea();
		blessingTextArea.setWrapText(true);

		blessingTextArea.setMaxWidth(Double.MAX_VALUE);
		blessingTextArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(blessingTextArea, Priority.ALWAYS);
		GridPane.setHgrow(blessingTextArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(blessingCheckBox, 0, 0);
		expContent.add(blessingTextArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();

		if (blessingCheckBox.isSelected()) {
			Costumer_SavedData.setBlessingCard(blessingTextArea.getText());
		}
	}

	private void cancelButtonClick(ActionEvent cancelEvent)
	{
		s_reservationEntity.setType(ReservationType.Canceled);
		Message entityMessage = MessagesFactory.createUpdateEntityMessage(s_reservationEntity);
		m_client.sendMessageToServer(entityMessage);
		openSelectedWindow(cancelEvent, "/boundaries/Costumer_CancelReservation.fxml");
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
		if (s_isCreateReservation) {
			changing_button.setText("Payment");
			gatherReservationList();
		} else {
			changing_button.setText("Cancel");
			addReservationList();
		}

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Double>("price"));

		catalog_table.setItems(paymentBill);
		catalog_table.refresh();
	}

	/**
	 * Gather the reservation items and count the amounts of each item.
	 */
	private void gatherReservationList()
	{
		ArrayList<ItemEntity> itemsList = new ArrayList<>();
		double totalPrice = 0;
		for (ItemEntity entity : Costumer_SavedData.getCostumerReservationList()) {
			CatalogItemRow itemReserve = new CatalogItemRow(entity.getId(), entity.getName(), entity.getItemPrice(),
					entity.getItemImage(), entity.getColor(), entity.getItemType().toString());
			itemsList.add(entity);
			paymentBill.add(itemReserve);
		}

		m_discount = Costumer_SavedData.getCostumerRefund();
		if (m_discount > totalPrice) {
			m_discount = totalPrice;
		}

		if (m_discount > 0.0) {
			totalPrice -= m_discount;
			CatalogItemRow item = new CatalogItemRow(99, "Discount", m_discount * -1.0, null, null, null);
			paymentBill.add(item);
		}

		Costumer_SavedData.setReservationList(itemsList);
		total_price_label.setText(String.format("%.2f", totalPrice));
	}

	private void addReservationList()
	{
		for (ItemEntity entity : s_reservationEntity.getReservationList()) {
			CatalogItemRow item = new CatalogItemRow(entity.getId(), entity.getName(), entity.getItemPrice(),
					entity.getItemImage(), entity.getColor(), entity.getItemType().toString());
			paymentBill.add(item);
		}

		total_price_label.setText(String.format("%.2f", s_reservationEntity.getTotalPrice()));
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// Not needed on this screen.
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
