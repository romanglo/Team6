
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logger.LogManager;
import newEntities.EntitiesEnums.CostumerSubscription;
import newEntities.EntitiesEnums.ReservationDeliveryType;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

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

	@FXML private Label credit_card_label;

	@FXML private RadioButton delivery_radio;

	@FXML private RadioButton pickup_radio;

	@FXML private DatePicker date_pick;

	@FXML private ComboBox<String> combo_hour;

	@FXML private ComboBox<String> combo_minute;

	@FXML private CheckBox immidiate_delivery;

	@FXML private CheckBox blessing_card;

	@FXML private TextArea blessing_text;

	@FXML private TextField delivery_address;

	@FXML private TextField delivery_phone;

	@FXML private TextField delivery_name;

	/* End region -> UI Binding Fields */

	/* Fields region */

	private Logger m_logger;

	private Client m_client;

	ObservableList<String> hoursList = FXCollections.observableArrayList();

	ObservableList<String> minutesList = FXCollections.observableArrayList();

	/**
	 * Manage discount according to the refund of the costumer.
	 */
	public static float m_discount;

	/* End of --> Fields region */

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
		openSelectedWindow(backEvent, "/boundaries/Costumer_CreateReservationPayment.fxml");
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
		if (!credit_card_number.isDisabled() && credit_card_number.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("You left empty fields.");
			alert.show();
			return;
		}
		if (delivery_radio.isSelected()) {
			if (delivery_address.getText().equals("") || delivery_name.getText().equals("")
					|| delivery_phone.getText().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Attention");
				alert.setHeaderText(null);
				alert.setContentText("You left empty fields.");
				alert.show();
				return;
			}
		}
		if (date_pick.getValue().isBefore(LocalDate.now()) || (date_pick.getValue().isEqual(LocalDate.now())
				&& Integer.parseInt(combo_hour.getValue()) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 3)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("The date and/or hour you picked is invalid, please try again.");
			alert.show();
			return;
		}
		
		updateFieldsWithData();

		Message entityMessage = MessagesFactory.createUpdateEntityMessage(Costumer_SavedData.getCostumer());
		m_client.sendMessageToServer(entityMessage);

		entityMessage = MessagesFactory.createAddEntityMessage(Costumer_SavedData.getReservationEntity());
		m_client.sendMessageToServer(entityMessage);

		entityMessage = MessagesFactory.createAddEntitiesMessage(Costumer_SavedData.getCostumerReservationList());
		m_client.sendMessageToServer(entityMessage);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Reservation Completed");
		alert.setHeaderText(null);
		alert.setContentText("Reservation completed. Thanks for using our application.");
		alert.showAndWait();
		openSelectedWindow(finishEvent, "/boundaries/Costumer.fxml");
	}

	private void updateFieldsWithData()
	{
		Costumer_SavedData.setCreditCard(credit_card_number.getText());
		Costumer_SavedData.setBalance(Costumer_SavedData.getCostumerBalance() - m_discount);
		
		Date date;
		Calendar calendar = Calendar.getInstance();
		if (immidiate_delivery.isSelected()) {
			date = new Date();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, 3);
			date = calendar.getTime();
		} else {
			LocalDate localDate = date_pick.getValue();
			date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		Costumer_SavedData.setDeliveryDate(date);
		
		if (delivery_radio.isSelected()) {
			if (immidiate_delivery.isSelected()) {
				Costumer_SavedData.setDeliveryType(ReservationDeliveryType.Immidiate);
			} else {
				Costumer_SavedData.setDeliveryType(ReservationDeliveryType.Future);
			}
		} else {
			Costumer_SavedData.setDeliveryType(ReservationDeliveryType.None);
		}
		
		Costumer_SavedData.setDeliveryAddress(delivery_address.getText());
		Costumer_SavedData.setDeliveryPhone(delivery_phone.getText());
		Costumer_SavedData.setDeliveryName(delivery_name.getText());
		
		if (blessing_card.isSelected()) {
			Costumer_SavedData.setBlessingCard(blessing_text.getText());
		} else {
			Costumer_SavedData.setBlessingCard("");
		}
	}

	@FXML
	private void deliveryButtonClick(ActionEvent deliveryAction)
	{
		pickup_radio.setSelected(false);
		delivery_address.setDisable(false);
		delivery_phone.setDisable(false);
		delivery_name.setDisable(false);
	}

	@FXML
	private void pickupButtonClick(ActionEvent pickupAction)
	{
		delivery_radio.setSelected(false);
		delivery_address.setDisable(true);
		delivery_phone.setDisable(true);
		delivery_name.setDisable(true);
	}

	@FXML
	private void blessingButtonClick(ActionEvent blessingAction)
	{
		blessing_text.setDisable(!blessing_card.isSelected());
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
		initializeUiFields();
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

	private void initializeUiFields()
	{
		if (Costumer_SavedData.getSubscription() != CostumerSubscription.None) {
			credit_card_number.setVisible(false);
			credit_card_label.setVisible(false);
		}

		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				hoursList.add("0" + i);
			} else {
				hoursList.add("" + i);
			}
		}
		for (int i = 0; i < 60; i++) {
			if (i < 10) {
				minutesList.add("0" + i);
			} else {
				minutesList.add("" + i);
			}
		}

		combo_hour.setItems(hoursList);
		combo_minute.setItems(minutesList);

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		combo_hour.setValue("" + (hour < 10 ? "0" + hour : hour));
		combo_minute.setValue("" + (minute < 10 ? "0" + minute : minute));

		pickup_radio.fire();
		date_pick.setValue(LocalDate.now());
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			@Override
			public String toString(LocalDate date)
			{
				if (date != null) return dateFormatter.format(date);
				else return "";
			}

			@Override
			public LocalDate fromString(String string)
			{
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusYears(1))) {
						return date_pick.getValue();
					} else return date;
				} else return null;
			}
		};

		date_pick.setConverter(converter);
		date_pick.setDayCellFactory(picker -> new DateCell() {

			@Override
			public void updateItem(LocalDate date, boolean empty)
			{
				super.updateItem(date, empty);
				setDisable(empty || date.isBefore(LocalDate.now()));
			}
		});
		blessing_text.setDisable(true);
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
				m_logger.info("Successfully delivered the message.");
			}
			return;
		} else {
			m_logger.warning("Received message data not of the type requested, requested: "
					+ RespondMessageData.class.getName());
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
