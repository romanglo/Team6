
package newControllers;

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
import controllers.Costumer_SavedData;
import javafx.application.Platform;
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
import newEntities.Costumer;
import newEntities.EntitiesEnums.CostumerSubscription;
import newEntities.EntitiesEnums.ProductType;
import newEntities.EntitiesEnums.ReservationDeliveryType;
import newEntities.EntitiesEnums.ReservationType;
import newEntities.IEntity;
import newEntities.Item;
import newEntities.ItemInReservation;
import newEntities.Reservation;
import newEntities.ShopCatalogData;
import newEntities.ShopCostumer;
import newEntities.ShopManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * CostumerController: Manage the costumer functionalities.
 * 
 * 
 */
public class CostumerController extends BaseController
{

	private enum ScreenType {
		None, Catalog, Costumized, Payment, CreditCard, Reservations, Cancel
	}

	// region FXML Fields

	@FXML private AnchorPane anchorpane_create_reservation;

	@FXML private AnchorPane anchorpane_cancel_reservation;

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

	@FXML private TextField domain_color;

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

	/* End of -> cancel reservation */

	// end region -> FXML Fields

	// region Fields

	private ScreenType m_currScreen;

	private String m_currComboShop;

	private ObservableList<String> shops = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> ItemRow = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> paymentList = FXCollections.observableArrayList();

	private ObservableList<String> hoursList = FXCollections.observableArrayList();

	private ObservableList<String> minutesList = FXCollections.observableArrayList();

	private ObservableList<CatalogItemRow> searchItemsList = FXCollections.observableArrayList();

	private boolean m_useSubscription;

	private ArrayList<ShopManager> shopManagerList;

	private ArrayList<Item> m_catalogList;

	private float m_discount;

	private ObservableList<CatalogItemRow> reservationTableList = FXCollections.observableArrayList();

	private ArrayList<Reservation> m_reservationList;

	private int s_reservationId;

	private ObservableList<CatalogItemRow> cancelItemsList = FXCollections.observableArrayList();

	private Reservation m_reservationEntity;

	// end region -> Fields

	// region Events listeners

	@FXML
	private void costumizedButtonClickOnCatalogType(ActionEvent costumizedEvent)
	{
		anchorpane_catalog.setVisible(false);
		anchorpane_costumized.setVisible(true);
		anchorpane_credit_card.setVisible(false);
		anchorpane_payment.setVisible(false);
		anchorpane_search.setVisible(false);
	}

	@FXML
	private void catalogButtonClickOnCustomizedType(ActionEvent costumizedEvent)
	{
		anchorpane_catalog.setVisible(true);
		anchorpane_costumized.setVisible(false);
		anchorpane_credit_card.setVisible(false);
		anchorpane_payment.setVisible(false);
		anchorpane_search.setVisible(false);
		initializeCatalog();
	}

	@FXML
	private void paymentButtonClick(ActionEvent paymentEvent)
	{
		anchorpane_catalog.setVisible(false);
		anchorpane_costumized.setVisible(false);
		anchorpane_payment.setVisible(true);
		anchorpane_credit_card.setVisible(false);
		anchorpane_search.setVisible(false);
		initializePayment();
	}

	@FXML
	private void paymentButtonClickPaymentType(ActionEvent paymentEvent)
	{
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
		if (!m_useSubscription && credit_card_number.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("You left empty fields.");
			alert.show();
			return;
		} else if (delivery_radio.isSelected()) {
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
		if (!immidiate_delivery.isSelected()) {
			if (date_pick.getValue().isBefore(LocalDate.now())
					|| (date_pick.getValue().isEqual(LocalDate.now()) && Integer
							.parseInt(combo_hour.getValue()) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 3)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Attention");
				alert.setHeaderText(null);
				alert.setContentText("The date and/or time you picked is invalid, please try again.");
				alert.show();
				return;
			}
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

	private void updateFieldsWithData()
	{
		Costumer_SavedData.setCreditCard(m_useSubscription ? "" : credit_card_number.getText());
		Costumer_SavedData.setBalance(Costumer_SavedData.getCostumerBalance() - m_discount);
		Costumer_SavedData.setTotalPrice(Float.parseFloat(total_price_label.getText()));
		float cumulativePrice = Costumer_SavedData.getCumulativePrice();
		Costumer_SavedData
				.setCumulativePrice(cumulativePrice + (m_useSubscription ? Costumer_SavedData.getTotalPrice() : 0));

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
		delivery_radio.setSelected(true);
		pickup_radio.setSelected(false);
		delivery_address.setDisable(false);
		delivery_phone.setDisable(false);
		delivery_name.setDisable(false);
	}

	@FXML
	private void pickupButtonClick(ActionEvent pickupAction)
	{
		delivery_radio.setSelected(false);
		pickup_radio.setSelected(true);
		delivery_address.setDisable(true);
		delivery_phone.setDisable(true);
		delivery_name.setDisable(true);
	}

	@FXML
	private void blessingButtonClick(ActionEvent blessingAction)
	{
		blessing_text.setDisable(!blessing_card.isSelected());
	}

	@FXML
	private void shopComboClick(ActionEvent shopEvent)
	{
		if (!Costumer_SavedData.getCostumerReservationList().isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("You will lose your reservation, are you sure?");
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

		ShopCatalogData catalogData = new ShopCatalogData(Costumer_SavedData.getShopManagerId());
		Message entityMessage = MessagesFactory.createIMessageDataMessage(catalogData);
		m_Client.sendMessageToServer(entityMessage);
	}

	@FXML
	private void searchButtonClickCustomizedType(ActionEvent searchEvent)
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

		searchItemsList = FXCollections.observableArrayList();
		for (Item entity : m_catalogList) {
			if (entity.getDomainColor().equals(domain_color.getText())
					&& (entity.getPrice() >= Double.parseDouble(min_price.getText())
							&& entity.getPrice() <= Double.parseDouble(max_price.getText()))
					&& entity.getType() == ProductType.Flower) {
				// Check the following:
				// 1. Domain color as requested.
				// 2. Price is in range.
				// 3. The entity is of the type 'Flower'.
				CatalogItemRow catalogItemRow = new CatalogItemRow(entity.getId(), entity.getName(),
						entity.getType().toString(),
						(float) (Integer.parseInt(item_amount.getText()) * entity.getPrice()), domain_color.getText());
				searchItemsList.add(catalogItemRow);
			}
		}

		if (searchItemsList.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("A match wasn't found, please try again.");
			alert.showAndWait();
		} else {
			initializeItemSearchTable();
			anchorpane_search.setVisible(true);
			anchorpane_costumized.setVisible(false);
		}
	}

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

	@FXML
	private void backToReservationsClick(ActionEvent backEvent)
	{
		anchorpane_cancel.setVisible(false);
		anchorpane_reservations.setVisible(true);
		initializeReservations();
	}

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

		ShopManager shopManager = new ShopManager();
		message = MessagesFactory.createGetAllEntityMessage(shopManager);
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
				anchorpane_catalog.setVisible(true);
				anchorpane_costumized.setVisible(false);
				anchorpane_credit_card.setVisible(false);
				anchorpane_payment.setVisible(false);
			break;

			case "Cancel Reservation":
				initializeReservations();
				anchorpane_create_reservation.setVisible(false);
				anchorpane_cancel_reservation.setVisible(true);
				anchorpane_cancel.setVisible(false);
				anchorpane_reservations.setVisible(true);
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
		return new String[] { "Create Reservation", "Cancel Reservation" };
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
			default:
				m_Logger.warning("Screen type is not recognized.");
		}
	}

	private void onMessageReceivedNoneType(Message msg)
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof RespondMessageData) {
			if (!((RespondMessageData) messageData).isSucceed()) {
				m_Logger.warning("Failed when sending a message to the server.");
			} else {
				m_Logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
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
		} else if (messageData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
			shopManagerList = new ArrayList<>();
			shops = FXCollections.observableArrayList();

			for (IEntity entity : entities) {
				if (!(entity instanceof ShopManager)) {
					m_Logger.warning("Received entity not of the type requested.");
					return;
				}
				ShopManager shopManager = (ShopManager) entity;
				shops.add(shopManager.getName());
				shopManagerList.add(shopManager);
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
					initializeCatalog();
				}
			});
		} else {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}
	}

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
						catalogButtonClickOnCustomizedType(new ActionEvent());
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
							backToReservationsClick(new ActionEvent());
						});
						return;
					}
				}
				m_Logger.info("Successfully delivered but received data not of the type requested.");
			}
			return;
		}

		if (entitiesListData instanceof EntityData) {
			IEntity entity = ((EntityData) entitiesListData).getEntity();
			if (!(entity instanceof Reservation)) {
				m_Logger.warning(
						"Received entity not of the type requested, requested: " + Reservation.class.getName());
				return;
			}

			m_reservationEntity = (Reservation) entity;
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

	// end region -> BaseController Implementation

	// region Initializing methods

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

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Item Added");
					alert.setHeaderText(null);
					alert.setContentText("The item was added to the reservation.");
					alert.showAndWait();
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

	private void initializeReservationsTable()
	{
		reservation_table.setRowFactory(param -> {
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					if (!rowData.getM_type().equalsIgnoreCase("open")) {
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

	private void initializeCatalog()
	{
		m_currScreen = ScreenType.Catalog;
		Message entityMessage = MessagesFactory.createGetEntityMessage(Costumer_SavedData.getShopCostumer());
		m_Client.sendMessageToServer(entityMessage);

		ShopCatalogData catalogData = new ShopCatalogData(Costumer_SavedData.getShopManagerId());
		entityMessage = MessagesFactory.createIMessageDataMessage(catalogData);
		m_Client.sendMessageToServer(entityMessage);
	}

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

		tablecolumn_payment_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_payment_name.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("name"));
		tablecolumn_payment_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));

		payment_table.setItems(paymentList);
	}

	private void initializeCreditCard()
	{
		m_currScreen = ScreenType.CreditCard;

		m_useSubscription = false;
		if (Costumer_SavedData.getSubscription() != CostumerSubscription.None) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attention");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to use your subscription?");
			Optional<ButtonType> result = alert.showAndWait();
			m_useSubscription = result.get() == ButtonType.OK;
			if (m_useSubscription) {
				credit_card_label.setVisible(false);
				credit_card_number.setVisible(false);
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

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		combo_hour.setValue("" + (hour < 10 ? "0" + hour : hour));
		combo_minute.setValue("" + (minute < 10 ? "0" + minute : minute));

		delivery_radio.setSelected(true);
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

	private void initializeReservations()
	{
		m_currScreen = ScreenType.Reservations;

		Reservation reservation = new Reservation();
		reservation.setCostumerId(Costumer_SavedData.getCostumerId());
		Message entityMessage = MessagesFactory.createGetAllEntityMessage(reservation);
		m_Client.sendMessageToServer(entityMessage);
	}

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
	// end region -> Initializing methods
}
