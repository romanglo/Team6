
package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import boundaries.CatalogItemRow;
import common.AlertBuilder;
import entities.Complaint;
import entities.Costumer;
import entities.IEntity;
import entities.Item;
import entities.ItemInReservation;
import entities.Reservation;
import entities.ShopCatalogData;
import entities.ShopCostumer;
import entities.ShopManager;
import entities.EntitiesEnums.CostumerSubscription;
import entities.EntitiesEnums.ProductType;
import entities.EntitiesEnums.ReservationDeliveryType;
import entities.EntitiesEnums.ReservationType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import messages.EntitiesListData;
import messages.EntityData;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 *
 * CostumerController: Manage the costumer UI functionalities.
 * 
 * @see BaseController
 */
public class CostumerController extends BaseController
{

	/**
	 * ScreenType: Screen display enumeration.
	 */
	private enum ScreenType {
		None, Catalog, Costumized, Payment, CreditCard, Reservations, Cancel, Complaints
	}

	// region FXML Fields

	@FXML private AnchorPane anchorpane_create_reservation;

	@FXML private AnchorPane anchorpane_cancel_reservation;

	@FXML private AnchorPane anchorpane_complaints;

	/* Create reservation */

	@FXML private AnchorPane anchorpane_catalog;

	@FXML private AnchorPane anchorpane_costumized;

	@FXML private AnchorPane anchorpane_search;

	@FXML private AnchorPane anchorpane_payment;

	@FXML private AnchorPane anchorpane_credit_card;

	@FXML private Button btn_costumized_item;

	@FXML private Button btn_Payment;

	@FXML private TableView<CatalogItemRow> catalog_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_name;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_type;

	@FXML private TableColumn<CatalogItemRow, Float> tablecolumn_price;

	@FXML private TableColumn<CatalogItemRow, ImageView> tablecolumn_image;

	@FXML private ComboBox<String> combo_shop;

	@FXML private ComboBox<String> domain_color;

	@FXML private TextField min_price;

	@FXML private TextField max_price;

	@FXML private TextField item_amount;

	@FXML private TableView<CatalogItemRow> search_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_search_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_search_name;

	@FXML private TableColumn<CatalogItemRow, Float> tablecolumn_search_price;

	@FXML private TableView<CatalogItemRow> payment_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_payment_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_payment_name;

	@FXML private TableColumn<CatalogItemRow, Float> tablecolumn_payment_price;

	@FXML private Label total_price_label;

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

	/* End of -> create reservation */

	/* cancel reservation */

	@FXML private AnchorPane anchorpane_reservations;

	@FXML private AnchorPane anchorpane_cancel;

	@FXML private TableView<CatalogItemRow> reservation_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_reservation_id;

	@FXML private TableColumn<CatalogItemRow, Float> tablecolumn_reservation_price;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_reservation_type;

	@FXML private TableView<CatalogItemRow> cancel_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_cancel_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_cancel_name;

	@FXML private TableColumn<CatalogItemRow, Float> tablecolumn_cancel_price;

	@FXML private Label cancel_total_price_label;

	@FXML private Label cancel_reservation_shop;

	/* End of -> cancel reservation */

	/* complaints tracker */

	@FXML private AnchorPane anchorpane_complaints_display;

	@FXML private TableView<CatalogItemRow> complaints_table;

	@FXML private TableColumn<CatalogItemRow, Integer> tablecolumn_complaints_id;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_complaints_date;

	@FXML private TableColumn<CatalogItemRow, String> tablecolumn_complaints_type;

	@FXML private TextArea textarea_complaint;

	@FXML private TextArea textarea_summary;

	/* End of -> complaints tracker */

	// end region -> FXML Fields

	// region Fields

	private ScreenType m_currScreen;

	private String m_currComboShop;

	private ObservableList<String> shops = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> ItemRow = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> paymentList = FXCollections.observableArrayList();

	private ObservableList<String> costumizedColors = FXCollections.observableArrayList();

	private ObservableList<String> hoursList = FXCollections.observableArrayList();

	private ObservableList<String> minutesList = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> searchItemsList = FXCollections.observableArrayList();

	private boolean m_useSubscription;

	private ArrayList<ShopManager> shopManagerList;

	private ArrayList<ShopCostumer> shopCostumerList;

	private ArrayList<Item> m_catalogList;

	private float m_discount;

	private ObservableList<CatalogItemRow> reservationTableList = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> complaintsTableList = FXCollections.observableArrayList();

	private ArrayList<Reservation> m_reservationList;

	private ArrayList<Complaint> m_complaintsList;

	private int s_reservationId;

	private ObservableList<CatalogItemRow> cancelItemsList = FXCollections.observableArrayList();

	private Reservation m_reservationEntity;

	// end region -> Fields

	// region Events listeners

	/**
	 * Listener method to change the screen to customized item screen.
	 * 
	 * @param costumizedEvent
	 *            Action event of the button click.
	 */
	@FXML
	private void costumizedItemScreen(ActionEvent costumizedEvent)
	{
		anchorpane_catalog.setVisible(false);
		anchorpane_costumized.setVisible(true);
		anchorpane_credit_card.setVisible(false);
		anchorpane_payment.setVisible(false);
		anchorpane_search.setVisible(false);
		initializeCostumized();
	}

	/**
	 * Listener method to change the screen to catalog screen.
	 *
	 * @param catalogEvent
	 *            Action event of the button click.
	 */
	@FXML
	private void catalogScreen(ActionEvent catalogEvent)
	{
		anchorpane_catalog.setVisible(true);
		anchorpane_costumized.setVisible(false);
		anchorpane_credit_card.setVisible(false);
		anchorpane_payment.setVisible(false);
		anchorpane_search.setVisible(false);
		initializeCatalog();
	}

	/**
	 * Listener method to change the screen to reservation list screen.
	 *
	 * @param paymentEvent
	 *            Action event of the button click.
	 */
	@FXML
	private void paymentScreen(ActionEvent paymentEvent)
	{
		anchorpane_catalog.setVisible(false);
		anchorpane_costumized.setVisible(false);
		anchorpane_payment.setVisible(true);
		anchorpane_credit_card.setVisible(false);
		anchorpane_search.setVisible(false);
		initializePayment();
	}

	/**
	 * Listener method to change the screen to payment screen.
	 *
	 * @param paymentEvent
	 *            Action event of the button click.
	 */
	@FXML
	private void creditCardScreen(ActionEvent paymentEvent)
	{
		if (Costumer_SavedData.getCostumerReservationList().isEmpty()) {
			showAlertMessage("The cart is empty.", AlertType.WARNING);
			return;
		}
		anchorpane_catalog.setVisible(false);
		anchorpane_costumized.setVisible(false);
		anchorpane_payment.setVisible(false);
		anchorpane_credit_card.setVisible(true);
		anchorpane_search.setVisible(false);
		initializeCreditCard();
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
		String creditCard = credit_card_number.getText().trim();
		if (!m_useSubscription && creditCard.equals("")) {
			showAlertMessage("Please fill in all fields to complete the reservation.", AlertType.WARNING);
			return;
		} else if (delivery_radio.isSelected()) {
			if (delivery_address.getText().equals("") || delivery_name.getText().equals("")
					|| delivery_phone.getText().equals("")) {
				showAlertMessage("Please fill in all fields to complete the reservation.", AlertType.INFORMATION);
				return;
			}
		}
		if (!immidiate_delivery.isSelected()) {
			if (date_pick.getValue().isBefore(LocalDate.now())
					|| (date_pick.getValue().isEqual(LocalDate.now()) && Integer
							.parseInt(combo_hour.getValue()) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 3)) {
				showAlertMessage("The date and/or time you picked is invalid, please try again.", AlertType.WARNING);
				return;
			}
		}
		if (!m_useSubscription && !creditCard.matches("[0-9]+")) {
			showAlertMessage("A credit card can only contain numbers.", AlertType.ERROR);
			return;
		}

		updateFieldsWithData();

		Message entityMessage;
		if (m_useSubscription) {
			entityMessage = MessagesFactory.createUpdateEntityMessage(Costumer_SavedData.getShopCostumer());
			m_Client.sendMessageToServer(entityMessage);
		}

		entityMessage = MessagesFactory.createUpdateEntityMessage(Costumer_SavedData.getCostumer());
		m_Client.sendMessageToServer(entityMessage);

		entityMessage = MessagesFactory.createAddEntityMessage(Costumer_SavedData.getReservationEntity());
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Method that updates the saved data of the costumer in order to save the
	 * reservation details.
	 */
	private void updateFieldsWithData()
	{
		Costumer_SavedData.setCreditCard(m_useSubscription ? "" : credit_card_number.getText());
		Costumer_SavedData.setBalance(Costumer_SavedData.getCostumerBalance() - m_discount);

		/* Add delivery payment if needed. */
		float totalPrice = Float.parseFloat(total_price_label.getText());
		if (delivery_radio.isSelected()) {
			totalPrice += 30;
		}
		Costumer_SavedData.setTotalPrice(totalPrice);

		float cumulativePrice = Costumer_SavedData.getCumulativePrice();
		Costumer_SavedData
				.setCumulativePrice(cumulativePrice + (m_useSubscription ? Costumer_SavedData.getTotalPrice() : 0));

		/* Add date of reservation to be ready. */
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

	/**
	 * Listener to change fields states to enable when choosing delivery.
	 *
	 * @param deliveryAction
	 *            Action event for the button click.
	 */
	@FXML
	private void deliveryButtonClick(ActionEvent deliveryAction)
	{
		delivery_radio.setSelected(true);
		pickup_radio.setSelected(false);
		delivery_address.setDisable(false);
		delivery_phone.setDisable(false);
		delivery_name.setDisable(false);
	}

	/**
	 * Listener to change fields states to disable when choosing self pick-up.
	 *
	 * @param pickupAction
	 *            Action event for the button click.
	 */
	@FXML
	private void pickupButtonClick(ActionEvent pickupAction)
	{
		delivery_radio.setSelected(false);
		pickup_radio.setSelected(true);
		delivery_address.setDisable(true);
		delivery_phone.setDisable(true);
		delivery_name.setDisable(true);

		delivery_address.setText("");
		delivery_name.setText("");
		delivery_phone.setText("");
	}

	/**
	 * Listener to change fields states to disable / enable when choosing self
	 * pick-up.
	 *
	 * @param blessingAction
	 *            Action event for the button click.
	 */
	@FXML
	private void blessingButtonClick(ActionEvent blessingAction)
	{
		blessing_text.setDisable(!blessing_card.isSelected());
	}

	/**
	 * Listener to clear the reservation list from items. pick-up.
	 *
	 * @param clearAction
	 *            Action event for the button click.
	 */
	@FXML
	private void clearCartButtonClickPaymentType(ActionEvent clearAction)
	{
		Costumer_SavedData.setReservationList(new ArrayList<>());
		initializePayment();
	}

	/**
	 * Listener to update the catalog with the chosen shop.
	 *
	 * @param shopEvent
	 *            Action event for the button click.
	 */
	@FXML
	private void shopComboClick(ActionEvent shopEvent)
	{
		if (!Costumer_SavedData.getCostumerReservationList().isEmpty()) {
			Alert alert = new AlertBuilder().setAlertType(AlertType.CONFIRMATION)
					.setContentText("You will lose your reservation, are you sure?").build();
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Costumer_SavedData.setReservationList(new ArrayList<>());
				m_currComboShop = combo_shop.getValue();
			} else {
				combo_shop.setValue(m_currComboShop);
			}
		}

		for (ShopManager manager : shopManagerList) {
			if (manager.getName().equals(combo_shop.getValue())) {
				Costumer_SavedData.setShopManagerId(manager.getId());
				break;
			}
		}

		for (ShopCostumer shopCostumer : shopCostumerList) {
			if (Costumer_SavedData.getShopManagerId() == shopCostumer.getShopManagerId()) {
				Costumer_SavedData.setShopCostumer(shopCostumer);
				break;
			}
		}

		ShopCatalogData catalogData = new ShopCatalogData(Costumer_SavedData.getShopManagerId());
		Message entityMessage = MessagesFactory.createIMessageDataMessage(catalogData);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Listener that creates a list of items that match the requested details
	 * entered for customized item.
	 *
	 * @param searchEvent
	 *            Action event for button click.
	 */
	@FXML
	private void searchButtonClickCustomizedType(ActionEvent searchEvent)
	{
		if ((domain_color.getValue() != null && domain_color.getValue().equals("")) || min_price.getText().equals("")
				|| max_price.getText().equals("") || item_amount.getText().equals("")) {
			showAlertMessage("Please fill in all fields to start a search.", AlertType.INFORMATION);
			return;
		}
		double min = Double.parseDouble(min_price.getText());
		double max = Double.parseDouble(max_price.getText());
		int amount = Integer.parseInt(item_amount.getText());

		if (min < 0 || max < 0 || amount < 1 || max < min) {
			showAlertMessage("Illigal values entered.", AlertType.WARNING);
			return;
		}

		searchItemsList = FXCollections.observableArrayList();
		for (Item entity : m_catalogList) {
			if (entity.getDomainColor().equals(domain_color.getValue().toLowerCase())
					&& (entity.getPrice() >= min && entity.getPrice() <= max)
					&& entity.getType() == ProductType.Flower) {
				// Check the following:
				// 1. Domain color as requested.
				// 2. Price is in range.
				// 3. The entity is of the type 'Flower'.
				CatalogItemRow catalogItemRow = new CatalogItemRow(entity.getId(), entity.getName(),
						(float) (amount * entity.getPrice()), null, domain_color.getValue().toLowerCase(),
						entity.getType().toString());
				searchItemsList.add(catalogItemRow);
			}
		}

		if (searchItemsList.isEmpty()) {
			showAlertMessage("A match wasn't found, please try again.", AlertType.WARNING);
		} else {
			initializeItemSearchTable();
			anchorpane_search.setVisible(true);
			anchorpane_costumized.setVisible(false);
		}
	}

	/**
	 * Listener that updates the reservation displayed with canceled type.
	 *
	 * @param cancelEvent
	 *            Action event of button click.
	 */
	@FXML
	private void cancelButtonClick(ActionEvent cancelEvent)
	{
		Costumer costumer = Costumer_SavedData.getCostumer();
		float balance = calculateRefund() + costumer.getBalance();
		costumer.setBalance(balance);
		Message entityMessage = MessagesFactory.createUpdateEntityMessage(costumer);
		m_Client.sendMessageToServer(entityMessage);

		m_reservationEntity.setType(ReservationType.Canceled);
		entityMessage = MessagesFactory.createUpdateEntityMessage(m_reservationEntity);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Listener that changes the screen to the reservations list screen.
	 *
	 * @param backEvent
	 *            Action event of button click.
	 */
	@FXML
	private void backToReservationsClick(ActionEvent backEvent)
	{
		anchorpane_cancel.setVisible(false);
		anchorpane_reservations.setVisible(true);
		initializeReservations();
	}

	/**
	 * Method calculates the refund that the costumer is supposed to get when
	 * canceling a reservation.
	 *
	 * @return The refund for the costumer.
	 */
	private float calculateRefund()
	{
		Date firstDate = m_reservationEntity.getDeliveryDate();
		Date secondDate = new Date();

		long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		float returnValue;
		if (diff >= 3) {
			returnValue = m_reservationEntity.getPrice();
		} else if (diff >= 1) {
			returnValue = (float) (m_reservationEntity.getPrice() * 0.5);
		} else {
			returnValue = 0;
		}

		return returnValue;
	}

	// end region -> Events listeners

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		m_currScreen = ScreenType.None;

		Costumer costumer = new Costumer();
		costumer.setUserName(m_ConnectedUser.getUserName());
		Message message = MessagesFactory.createGetEntityMessage(costumer);
		m_Client.sendMessageToServer(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		switch (title) {
			case "Create Reservation":
				initializeCatalog();
				anchorpane_create_reservation.setVisible(true);
				anchorpane_cancel_reservation.setVisible(false);
				anchorpane_complaints.setVisible(false);
				anchorpane_catalog.setVisible(true);
				anchorpane_costumized.setVisible(false);
				anchorpane_credit_card.setVisible(false);
				anchorpane_payment.setVisible(false);
			break;

			case "Cancel Reservation":
				initializeReservations();
				anchorpane_create_reservation.setVisible(false);
				anchorpane_cancel_reservation.setVisible(true);
				anchorpane_complaints.setVisible(false);
				anchorpane_cancel.setVisible(false);
				anchorpane_reservations.setVisible(true);
			break;

			case "Complaint Tracker":
				initializeComplaints();
				anchorpane_create_reservation.setVisible(false);
				anchorpane_cancel_reservation.setVisible(false);
				anchorpane_complaints_display.setVisible(true);
				anchorpane_complaints.setVisible(true);
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
		return new String[] { "Create Reservation", "Cancel Reservation", "Complaint Tracker" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		switch (m_currScreen) {
			case None:
				onMessageReceivedNoneType(msg);
			break;
			case Catalog:
				onMessageReceivedCatalogType(msg);
			break;
			case CreditCard:
				onMessageReceivedCreditCardType(msg);
			break;
			case Reservations:
				onMessageReceivedReservationType(msg);
			break;
			case Cancel:
				onMessageReceivedCancelType(msg);
			break;
			case Complaints:
				onMessageReceivedComplaintsType(msg);
			break;
			default:
				m_Logger.warning("Screen type is not recognized.");
		}
	}

	/**
	 * Method handles a message that has been received from the server on start up.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedNoneType(Message msg)
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof RespondMessageData) {
			if (!((RespondMessageData) messageData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			}
			return;
		}

		if (messageData instanceof EntityData) {
			IEntity entity = ((EntityData) messageData).getEntity();
			if (!(entity instanceof Costumer)) {
				m_Logger.warning("Received entity not of the type requested.");
				return;
			}

			Costumer costumer = (Costumer) entity;
			costumer.setUser(m_ConnectedUser);
			Costumer_SavedData.initializeSavedData(costumer);
			m_ConnectedUser = costumer;

			ShopCostumer shopCostumer = new ShopCostumer();
			shopCostumer.setCostumerId(Costumer_SavedData.getCostumerId());
			Message entityMessage = MessagesFactory.createGetAllEntityMessage(shopCostumer);
			m_Client.sendMessageToServer(entityMessage);
		} else if (messageData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) messageData).getEntities();

			if (entities.get(0) instanceof ShopCostumer) {
				shopCostumerList = new ArrayList<>();

				for (IEntity entity : entities) {
					if (!(entity instanceof ShopCostumer)) {
						m_Logger.warning("Received entity not of the type requested.");
						return;
					}

					shopCostumerList.add((ShopCostumer) entity);
				}

				ShopManager shopManager = new ShopManager();
				Message message = MessagesFactory.createGetAllEntityMessage(shopManager);
				m_Client.sendMessageToServer(message);
			} else if (entities.get(0) instanceof ShopManager) {
				shopManagerList = new ArrayList<>();
				shops = FXCollections.observableArrayList();

				for (IEntity entity : entities) {
					if (!(entity instanceof ShopManager)) {
						m_Logger.warning("Received entity not of the type requested.");
						return;
					}
					ShopManager shopManager = (ShopManager) entity;
					shopManagerList.add(shopManager);

					for (ShopCostumer shopCostumer : shopCostumerList) {
						if (shopCostumer.getShopManagerId() == shopManager.getId()) {
							shops.add(shopManager.getName());
							break;
						}
					}
				}

				Platform.runLater(() -> {
					combo_shop.getItems().clear();
					if (shops != null && !shops.isEmpty()) {
						combo_shop.setItems(shops);
						combo_shop.getSelectionModel().selectFirst();
						if (combo_shop.getValue() == null) {
							return;
						}
						m_currComboShop = combo_shop.getValue();
						Costumer_SavedData.setShopManagerId(shopManagerList.get(0).getId());
						for (ShopCostumer shopCostumer : shopCostumerList) {
							if (Costumer_SavedData.getShopManagerId() == shopCostumer.getShopManagerId()) {
								Costumer_SavedData.setShopCostumer(shopCostumer);
								break;
							}
						}
						initializeCatalog();
					}
				});
			}

		} else {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}
	}

	/**
	 * Method handles a message that has been received from the server on catalog
	 * screen.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedCatalogType(Message msg)
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof RespondMessageData) {
			if (!((RespondMessageData) messageData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				m_Logger.warning("Received message data not of the type requested, requested: "
						+ EntitiesListData.class.getName());
			}
			return;
		}

		if (messageData instanceof EntitiesListData) {
			List<IEntity> entityList = ((EntitiesListData) messageData).getEntities();
			m_catalogList = new ArrayList<>();
			ItemRow = FXCollections.observableArrayList();

			for (IEntity entity : entityList) {
				if (!(entity instanceof Item)) {
					m_Logger.warning("Received entity not of the type requested. Expected: " + Item.class.getName());
					return;
				}

				Item item = (Item) entity;
				m_catalogList.add(item);
				CatalogItemRow itemRow = new CatalogItemRow(item.getId(), item.getName(), item.getPrice(),
						item.getImage(), item.getDomainColor(), item.getType().toString());
				ItemRow.add(itemRow);
			}

			initializeTableCatalogType();
		} else if (messageData instanceof EntityData) {
			IEntity entity = ((EntityData) messageData).getEntity();
			if (!(entity instanceof ShopCostumer)) {
				m_Logger.warning(
						"Received entity not of the type requested. Expected: " + ShopCostumer.class.getName());
				return;
			}

			Costumer_SavedData.setShopCostumer((ShopCostumer) entity);
		} else {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}
	}

	/**
	 * Method handles a message that has been received from the server on credit
	 * card screen.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedCreditCardType(Message msg)
	{
		IMessageData entitiesListData = msg.getMessageData();
		if (entitiesListData instanceof RespondMessageData) {
			if (!((RespondMessageData) entitiesListData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				IMessageData messageData = ((RespondMessageData) entitiesListData).getMessageData();

				if (messageData instanceof EntitiesListData) {
					List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
					if (entities.get(0) instanceof ItemInReservation) {
						Platform.runLater(() -> {
							showAlertMessage("Reservation completed, thanks for choosing us.", AlertType.INFORMATION);
						});

						Costumer_SavedData.setReservationList(new ArrayList<>());

						catalogScreen(new ActionEvent());
					}
					return;
				}

				if (!(messageData instanceof EntityData)) {
					m_Logger.warning("Received message data not of the type requested, requested: "
							+ EntityData.class.getName());
					return;
				}

				IEntity entity = ((EntityData) messageData).getEntity();

				if (entity instanceof Costumer) {
					return;
				}

				if (entity instanceof Reservation) {
					Reservation reservation = (Reservation) entity;
					for (IEntity item : Costumer_SavedData.getCostumerReservationList()) {
						ItemInReservation itemInReservation = (ItemInReservation) item;
						itemInReservation.setReservationId(reservation.getId());
					}
					Message message = MessagesFactory
							.createAddEntitiesMessage(Costumer_SavedData.getCostumerReservationList());
					m_Client.sendMessageToServer(message);
				} else {
					m_Logger.warning(
							"Received entity not of the type requested, requested: " + Reservation.class.getName());
				}
			}
			return;
		} else {
			m_Logger.warning("Received message data not of the type requested, requested: "
					+ RespondMessageData.class.getName());
		}
	}

	/**
	 * Method handles a message that has been received from the server on
	 * reservations list screen.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedReservationType(Message msg)
	{
		IMessageData entitiesListData = msg.getMessageData();
		if (entitiesListData instanceof RespondMessageData) {
			if (!((RespondMessageData) entitiesListData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				m_Logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
			}
			return;
		}
		if (!(entitiesListData instanceof EntitiesListData)) {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}

		List<IEntity> entityList = ((EntitiesListData) entitiesListData).getEntities();
		m_reservationList = new ArrayList<>();
		reservationTableList = FXCollections.observableArrayList();

		for (IEntity entity : entityList) {
			if (!(entity instanceof Reservation)) {
				m_Logger.warning("Received entity not of the type requested.");
				return;
			}

			Reservation reservation = (Reservation) entity;
			m_reservationList.add(reservation);
			CatalogItemRow itemRow = new CatalogItemRow(reservation.getId(), null, reservation.getType().toString(),
					reservation.getPrice(), null, null);
			reservationTableList.add(itemRow);
		}

		initializeReservationsTable();
	}

	/**
	 * Method handles a message that has been received from the server on cancel
	 * reservation screen.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedCancelType(Message msg)
	{
		IMessageData entitiesListData = msg.getMessageData();
		if (entitiesListData instanceof RespondMessageData) {
			if (!((RespondMessageData) entitiesListData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				IMessageData messageData = ((RespondMessageData) entitiesListData).getMessageData();
				if (messageData instanceof EntityData) {
					IEntity entity = ((EntityData) messageData).getEntity();
					if (entity instanceof Reservation) {
						Platform.runLater(() -> {
							float refund = calculateRefund();
							showAlertMessage("Reservation canceled successfully.\nPayback: " + refund,
									AlertType.INFORMATION);
							backToReservationsClick(new ActionEvent());
						});
						return;
					}
				}
			}
			return;
		}

		if (entitiesListData instanceof EntityData) {
			IEntity entity = ((EntityData) entitiesListData).getEntity();
			if (entity instanceof Reservation) {
				m_reservationEntity = (Reservation) entity;
				ShopManager shopManager = new ShopManager();
				shopManager.setId(m_reservationEntity.getShopManagerId());
				Message message = MessagesFactory.createGetEntityMessage(shopManager);
				m_Client.sendMessageToServer(message);
			} else if (entity instanceof ShopManager) {
				ShopManager manager = (ShopManager) entity;
				Platform.runLater(() -> {
					cancel_reservation_shop.setText("Shop: " + manager.getName());
				});
			} else {
				m_Logger.warning("Received entity not of the type requested.");
			}

		} else if (entitiesListData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) entitiesListData).getEntities();
			cancelItemsList = FXCollections.observableArrayList();
			for (IEntity iEntity : entities) {
				if (!(iEntity instanceof ItemInReservation)) {
					m_Logger.warning("Received entity not of the type requested, requested: "
							+ ItemInReservation.class.getName());
					return;
				}

				ItemInReservation itemInReservation = (ItemInReservation) iEntity;
				CatalogItemRow item = new CatalogItemRow(itemInReservation.getItemId(), itemInReservation.getItemName(),
						itemInReservation.getPrice(), null, null, null);
				cancelItemsList.add(item);
			}
			Platform.runLater(() -> {
				cancel_total_price_label.setText(String.format("%.2f", m_reservationEntity.getPrice()));
				tablecolumn_cancel_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
				tablecolumn_cancel_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
				tablecolumn_cancel_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
				cancel_table.setItems(cancelItemsList);
			});
		} else {
			m_Logger.warning("Received message data not of the type requested.");
		}
	}

	/**
	 * Method handles a message that has been received from the server on cancel
	 * reservation screen.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	private void onMessageReceivedComplaintsType(Message msg)
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof RespondMessageData) {
			if (!((RespondMessageData) messageData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			}
			return;
		}

		if (!(messageData instanceof EntitiesListData)) {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}

		List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
		m_complaintsList = new ArrayList<>();
		complaintsTableList.clear();
		for (IEntity entity : entities) {
			if (!(entity instanceof Complaint)) {
				m_Logger.warning("Received entity not of the type requested, requested: " + Complaint.class.getName());
				return;
			}

			DateFormat dateForamt = new SimpleDateFormat("dd/MM/yyyy");
			Complaint complaint = (Complaint) entity;
			m_complaintsList.add(complaint);
			CatalogItemRow catalogItemRow = new CatalogItemRow(complaint.getId(),
					dateForamt.format(complaint.getCreationDate()), complaint.isOpened() ? "Open" : "Closed", null, "");
			complaintsTableList.add(catalogItemRow);
		}

		initializeComplaintsTable();
	}
	// end region -> BaseController Implementation

	// region Initializing methods

	/**
	 * Method that initializes the catalog table.
	 */
	private void initializeTableCatalogType()
	{
		catalog_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					ItemInReservation itemInReservation = new ItemInReservation();
					itemInReservation.setItemId(rowData.getM_id());
					itemInReservation.setQuantity(1);
					itemInReservation.setPrice(Float.parseFloat(rowData.getPrice()));
					itemInReservation.setItemName(rowData.getM_name());
					Costumer_SavedData.addItemToReservation(itemInReservation);

					showAlertMessage("The item was added to the reservation.", AlertType.INFORMATION);
				}
			});
			return tableRow;
		});

		tablecolumn_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		tablecolumn_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
		tablecolumn_image.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, ImageView>("image"));

		Platform.runLater(() -> {
			catalog_table.setItems(ItemRow);
			catalog_table.refresh();
		});
	}

	/**
	 * Method that initializes the reservations table.
	 */
	private void initializeReservationsTable()
	{
		reservation_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					if (rowData.getM_type().equalsIgnoreCase("closed")) {
						showAlertMessage("The reservation is closed.", AlertType.INFORMATION);
						return;
					}
					if (rowData.getM_type().equalsIgnoreCase("canceled")) {
						showAlertMessage("The reservation is canceled.", AlertType.INFORMATION);
						return;
					}

					s_reservationId = rowData.getM_id();
					anchorpane_cancel.setVisible(true);
					anchorpane_reservations.setVisible(false);
					initializeCancel();
				}
			});
			return tableRow;
		});

		tablecolumn_reservation_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_reservation_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
		tablecolumn_reservation_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));

		Platform.runLater(() -> {
			reservation_table.setItems(reservationTableList);
			reservation_table.refresh();
		});
	}

	/**
	 * Method that initializes the customized item search table.
	 */
	private void initializeItemSearchTable()
	{
		search_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();

					ItemInReservation itemInReservation = new ItemInReservation();
					itemInReservation.setItemId(rowData.getM_id());
					itemInReservation.setQuantity(Integer.parseInt(item_amount.getText()));
					itemInReservation.setPrice(rowData.getM_price());
					itemInReservation.setItemName(rowData.getName());

					Costumer_SavedData.addItemToReservation(itemInReservation);
					showAlertMessage("The item was added to the reservation.", AlertType.INFORMATION);
				}
			});
			return tableRow;
		});

		tablecolumn_search_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_search_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_search_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));

		Platform.runLater(() -> {
			search_table.setItems(searchItemsList);
			search_table.refresh();
		});
	}

	/**
	 * Method that initializes the reservations table.
	 */
	private void initializeComplaintsTable()
	{
		complaints_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					Complaint selectedComplaint = null;

					for (Complaint complaint : m_complaintsList) {
						if (complaint.getId() == rowData.getM_id()) {
							selectedComplaint = complaint;
							break;
						}
					}
					if (selectedComplaint == null) {
						return;
					}
					String complaint = selectedComplaint.getComplaint();
					String summary = selectedComplaint.getSummary();

					textarea_complaint.setText(complaint == null ? "" : complaint);
					textarea_summary.setText(summary == null ? "" : summary);
				}
			});
			return tableRow;
		});

		tablecolumn_complaints_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_complaints_date.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_complaints_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));

		Platform.runLater(() -> {
			complaints_table.setItems(complaintsTableList);
			complaints_table.refresh();
		});
	}

	/**
	 * Method that initializes the catalog screen.
	 */
	private void initializeCatalog()
	{
		m_currScreen = ScreenType.Catalog;

		ShopCatalogData catalogData = new ShopCatalogData(Costumer_SavedData.getShopManagerId());
		Message entityMessage = MessagesFactory.createIMessageDataMessage(catalogData);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Method that initializes the customized screen.
	 */
	private void initializeCostumized()
	{
		costumizedColors = FXCollections.observableArrayList();
		ArrayList<String> strings = new ArrayList<>();

		for (Item item : m_catalogList) {
			if (!strings.contains(item.getDomainColor())) {
				strings.add(item.getDomainColor());
			}
		}

		for (String str : strings) {
			costumizedColors.add(upperCaseFirst(str));
		}

		domain_color.setItems(costumizedColors);
		domain_color.getSelectionModel().selectFirst();
	}

	/**
	 * Method changes the word with capital letter.
	 *
	 * @param value
	 *            The string to change.
	 * @return Changed string.
	 */
	private String upperCaseFirst(String value)
	{
		char[] array = value.toCharArray();
		array[0] = Character.toUpperCase(array[0]);
		return new String(array);
	}

	/**
	 * Method that initializes the reservation list screen.
	 */
	private void initializePayment()
	{
		m_currScreen = ScreenType.Payment;

		paymentList = FXCollections.observableArrayList();
		float totalPrice = 0;
		for (IEntity entity : Costumer_SavedData.getCostumerReservationList()) {
			ItemInReservation itemInReservation = (ItemInReservation) entity;
			CatalogItemRow itemReserve = new CatalogItemRow(itemInReservation.getItemId(),
					itemInReservation.getItemName(), itemInReservation.getPrice(), null, null, null);
			totalPrice += itemInReservation.getPrice();
			paymentList.add(itemReserve);
		}

		/* Check for subscription. */
		float discount = 0;
		if (Costumer_SavedData.getSubscription() == CostumerSubscription.Monthly) {
			discount = (float) (totalPrice * 0.1);
		} else if (Costumer_SavedData.getSubscription() == CostumerSubscription.Yearly) {
			discount = (float) (totalPrice * 0.35);
		}
		if (discount != 0) {
			CatalogItemRow newItem = new CatalogItemRow(98, "Subscription Discount", discount * -1, null, null, null);
			paymentList.add(newItem);
		}
		totalPrice -= discount;

		m_discount = Costumer_SavedData.getCostumerBalance();
		if (m_discount > totalPrice) {
			m_discount = totalPrice;
		}

		if (m_discount > 0.0) {
			totalPrice -= m_discount;
			CatalogItemRow item = new CatalogItemRow(99, "Discount", m_discount * -1, null, null, null);
			paymentList.add(item);
		}

		total_price_label.setText(String.format("%.2f", totalPrice));

		payment_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();

					Alert alert = new AlertBuilder().setAlertType(AlertType.CONFIRMATION)
							.setContentText("Are you sure you want to remove the item?").build();
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() != ButtonType.OK) {
						return;
					}

					ItemInReservation itemInReservation = new ItemInReservation();
					itemInReservation.setItemId(rowData.getM_id());

					Costumer_SavedData.removeItemFromReservation(itemInReservation);
					initializePayment();
				}
			});
			return tableRow;
		});

		tablecolumn_payment_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_payment_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_payment_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));

		payment_table.setItems(paymentList);
	}

	/**
	 * Method that initializes the payment screen.
	 */
	private void initializeCreditCard()
	{
		m_currScreen = ScreenType.CreditCard;

		credit_card_number.setText("");
		delivery_address.setText("");
		delivery_name.setText("");
		delivery_phone.setText("");
		blessing_text.setText("");
		m_useSubscription = false;

		if (Costumer_SavedData.getSubscription() != CostumerSubscription.None) {
			Alert alert = new AlertBuilder().setAlertType(AlertType.CONFIRMATION)
					.setContentText("Do you want to use your subscription?").build();
			Optional<ButtonType> result = alert.showAndWait();
			m_useSubscription = result.get() == ButtonType.OK;
			if (m_useSubscription) {
				credit_card_label.setVisible(false);
				credit_card_number.setVisible(false);
			} else {
				credit_card_label.setVisible(true);
				credit_card_number.setVisible(true);
			}
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

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, 3);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		combo_hour.setValue("" + (hour < 10 ? "0" + hour : hour));
		combo_minute.setValue("" + (minute < 10 ? "0" + minute : minute));

		delivery_radio.setSelected(true);
		pickup_radio.setSelected(false);
		immidiate_delivery.setSelected(false);
		delivery_address.setDisable(false);
		delivery_name.setDisable(false);
		delivery_phone.setDisable(false);
		blessing_card.setSelected(false);
		blessing_text.setDisable(true);
		date_pick.setValue(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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

		credit_card_number.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (credit_card_number.getText().length() > 16) {
					String s = credit_card_number.getText().substring(0, 16);
					credit_card_number.setText(s);
				}
			}
		});

		delivery_address.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (delivery_address.getText().length() > 50) {
					String s = delivery_address.getText().substring(0, 50);
					delivery_address.setText(s);
				}
			}
		});

		delivery_name.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (delivery_name.getText().length() > 20) {
					String s = delivery_name.getText().substring(0, 20);
					delivery_name.setText(s);
				}
			}
		});

		delivery_phone.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (delivery_phone.getText().length() > 10) {
					String s = delivery_phone.getText().substring(0, 10);
					delivery_phone.setText(s);
				}
			}
		});

		blessing_text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (blessing_text.getText().length() > 100) {
					String s = blessing_text.getText().substring(0, 100);
					blessing_text.setText(s);
				}
			}
		});
	}

	/**
	 * Method that initializes the reservations list screen.
	 */
	private void initializeReservations()
	{
		m_currScreen = ScreenType.Reservations;

		Reservation reservation = new Reservation();
		reservation.setCostumerId(Costumer_SavedData.getCostumerId());
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(reservation);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Method that initializes the cancel reservation screen.
	 */
	private void initializeCancel()
	{
		m_currScreen = ScreenType.Cancel;

		Reservation reservation = new Reservation();
		reservation.setCostumerId(Costumer_SavedData.getCostumerId());
		reservation.setId(s_reservationId);
		Message message = MessagesFactory.createGetEntityMessage(reservation);
		m_Client.sendMessageToServer(message);

		ItemInReservation itemInReservation = new ItemInReservation();
		itemInReservation.setReservationId(s_reservationId);
		message = MessagesFactory.createGetAllEntityMessage(itemInReservation);
		m_Client.sendMessageToServer(message);
	}

	/**
	 * Method that initializes the cancel complaint tracker screen.
	 */
	private void initializeComplaints()
	{
		m_currScreen = ScreenType.Complaints;

		Complaint complaint = new Complaint();
		complaint.setCostumerId(Costumer_SavedData.getCostumerId());
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(complaint);
		m_Client.sendMessageToServer(entityMessage);
	}
	// end region -> Initializing methods
}
