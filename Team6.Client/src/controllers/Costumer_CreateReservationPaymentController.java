
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.CatalogItemRow;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import newEntities.EntitiesEnums.ReservationType;
import newEntities.IEntity;
import newEntities.ItemInReservation;
import newEntities.Reservation;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

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

	private float m_discount;

	private Reservation m_reservationEntity;

	/**
	 * When displaying the canceling reservation screen, manage the reservation.
	 */
	public static int s_reservationId;

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
		Costumer_SavedData.setTotalPrice(Float.parseFloat(total_price_label.getText()));
		Costumer_PaymentCreditCardController.m_discount = m_discount;
		openSelectedWindow(paymentEvent, "/boundaries/Costumer_PaymentCreditCard.fxml");
	}

	/**
	 * Gather the reservation items and count the amounts of each item.
	 */
	private void gatherReservationList()
	{
		float totalPrice = 0;
		for (IEntity entity : Costumer_SavedData.getCostumerReservationList()) {
			ItemInReservation itemInReservation = (ItemInReservation) entity;
			CatalogItemRow itemReserve = new CatalogItemRow(itemInReservation.getItemId(),
					itemInReservation.getItemName(), itemInReservation.getPrice(), null, null, null);
			totalPrice += itemInReservation.getPrice();
			paymentBill.add(itemReserve);
		}

		m_discount = Costumer_SavedData.getCostumerBalance();
		if (m_discount > totalPrice) {
			m_discount = totalPrice;
		}

		if (m_discount > 0.0) {
			totalPrice -= m_discount;
			CatalogItemRow item = new CatalogItemRow(99, "Discount", m_discount * -1, null, null, null);
			paymentBill.add(item);
		}

		total_price_label.setText(String.format("%.2f", totalPrice));
	}

	private void addReservationList()
	{
		Reservation reservation = new Reservation();
		reservation.setCostumerId(Costumer_SavedData.getCostumerId());
		reservation.setId(s_reservationId);
		Message message = MessagesFactory.createGetEntityMessage(reservation);
		m_client.sendMessageToServer(message);

		ItemInReservation itemInReservation = new ItemInReservation();
		itemInReservation.setReservationId(s_reservationId);
		message = MessagesFactory.createGetAllEntityMessage(itemInReservation);
		m_client.sendMessageToServer(itemInReservation);
	}

	private void cancelButtonClick(ActionEvent cancelEvent)
	{
		m_reservationEntity.setType(ReservationType.Canceled);
		Message entityMessage = MessagesFactory.createUpdateEntityMessage(m_reservationEntity);
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
				m_logger.warning(
						"Received message data not of the type requested, requested: " + EntityData.class.getName());
			}
			return;
		}

		if (entitiesListData instanceof EntityData) {
			IEntity entity = ((EntityData) entitiesListData).getEntity();
			if (!(entity instanceof Reservation)) {
				m_logger.warning(
						"Received entity not of the type requested, requested: " + Reservation.class.getName());
				return;
			}

			m_reservationEntity = (Reservation) entity;
		} else if (entitiesListData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) entitiesListData).getEntities();
			for (IEntity iEntity : entities) {
				if (!(iEntity instanceof ItemInReservation)) {
					m_logger.warning("Received entity not of the type requested, requested: "
							+ ItemInReservation.class.getName());
					return;
				}

				ItemInReservation itemInReservation = (ItemInReservation) iEntity;
				CatalogItemRow item = new CatalogItemRow(itemInReservation.getItemId(), itemInReservation.getItemName(),
						itemInReservation.getPrice(), null, null, null);
				paymentBill.add(item);
			}

			total_price_label.setText(String.format("%.2f", m_reservationEntity.getPrice()));
		} else {
			m_logger.warning("Received message data not of the type requested.");
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
