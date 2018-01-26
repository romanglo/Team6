
package controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import boundaries.CatalogItemRow;
import boundaries.ReservationStatusRow;
import common.AlertBuilder;
import entities.EntitiesEnums;
import entities.IEntity;
import entities.Reservation;
import entities.ShopEmployee;
import entities.Survey;
import entities.SurveyResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import messages.EntitiesListData;
import messages.EntityData;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 *
 * ShopEmployeeController: Manages shop employee UI.
 * 
 * @see BaseController
 * 
 */
public class ShopEmployeeController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	private @FXML AnchorPane anchorpane_option2;

	private @FXML AnchorPane anchorpane_dino;

	private @FXML ImageView imageview_dino;

	private String correct_title;

	@FXML private TextField textfiled_id;

	@FXML private TextField textfiled_question1;

	@FXML private TextField textfiled_question2;

	@FXML private TextField textfiled_question3;

	@FXML private TextField textfiled_question4;

	@FXML private TextField textfiled_question5;

	@FXML private TextField textfiled_question6;

	@FXML private Spinner<Integer> combobox_answer1;

	@FXML private Spinner<Integer> combobox_answer2;

	@FXML private Spinner<Integer> combobox_answer3;

	@FXML private Spinner<Integer> combobox_answer4;

	@FXML private Spinner<Integer> combobox_answer5;

	@FXML private Spinner<Integer> combobox_answer6;

	private int manager_id;

	private Survey correct_survey;

	private String[] questions = { "How likely is it that you would recommended our company?",
			"How well do our porducts meet your needs?", "How would you rate the quality of our products?",
			"How would you rate the value for money of the product?",
			"How likely are you to purchase any of our products again?",
			"How would you rate the shopping experience in our shop?" };

	private SpinnerValueFactory<Integer> svf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private List<IEntity> reservations;

	private ArrayList<ReservationStatusRow> open_reservations = new ArrayList<>();

	private ObservableList<ReservationStatusRow> reservation_list = FXCollections.observableArrayList();

	private @FXML Label enter_date;

	private @FXML TableView<ReservationStatusRow> reservation_table;

	private @FXML TableColumn<ReservationStatusRow, Integer> tablecolumn_reservation_id;

	private @FXML TableColumn<ReservationStatusRow, Integer> tablecolumn_costumer_id;

	private @FXML TableColumn<ReservationStatusRow, Float> tablecolumn_reservation_price;

	private @FXML TableColumn<ReservationStatusRow, String> tablecolumn_reservation_type;

	private @FXML TableColumn<ReservationStatusRow, String> tablecolumn_delivery_date;

	private ArrayList<IEntity> closes_reservations = new ArrayList<>();

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		initializeConfigurationShopSalesTable();

		InputStream dinoResource = getClass().getResourceAsStream("/boundaries/images/dino.gif");
		if (dinoResource != null) {
			Image image = new Image(dinoResource);
			imageview_dino.setImage(image);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		correct_title = title;
		switch (title) {
			case "Add survey":
				anchorpane_option1.setVisible(false);
				anchorpane_option2.setVisible(false);
				anchorpane_dino.setVisible(false);
				initializesurveys();
			break;
			case "Close Reservations":
				anchorpane_option1.setVisible(false);
				anchorpane_option2.setVisible(true);
				anchorpane_dino.setVisible(false);
				initialReservations();
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
		return new String[] { "Add survey", "Close Reservations" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (correct_title.equals("Add survey")) {
			if (messageData instanceof EntityData) {
				if (((EntityData) messageData).getEntity() instanceof ShopEmployee) {
					manager_id = ((ShopEmployee) ((EntityData) messageData).getEntity()).getShopManagerId();
					Survey survey_entity = new Survey();
					msg = MessagesFactory.createGetAllEntityMessage(survey_entity);
					m_Client.sendMessageToServer(msg);
				}
			} else if (messageData instanceof EntitiesListData) {
				if (((EntitiesListData) messageData).getEntities().get(0) instanceof Survey) {
					List<IEntity> temp = ((EntitiesListData) messageData).getEntities();
					for (int i = 0; i < temp.size(); i++) {
						if (((Survey) temp.get(i)).getManagerId() == manager_id) {
							if (((Survey) temp.get(i)).getEndDate().after(new Date())) {
								correct_survey = (Survey) temp.get(i);
							}
						}
					}
					if (correct_survey == null) {
						showAlertMessage("There are no surveys for your shop.", AlertType.INFORMATION);
						anchorpane_dino.setVisible(true);
					} else {
						anchorpane_option1.setVisible(true);
						textfiled_question1.setText(questions[0]);
						textfiled_question2.setText(questions[1]);
						textfiled_question3.setText(questions[2]);
						textfiled_question4.setText(questions[3]);
						textfiled_question5.setText(questions[4]);
						textfiled_question6.setText(questions[5]);
					}
				}
			} else if (messageData instanceof RespondMessageData) {
				if (((RespondMessageData) messageData).getMessageData() instanceof EntityData) {
					if (((EntityData) ((RespondMessageData) messageData).getMessageData())
							.getEntity() instanceof SurveyResult) {
						if (((RespondMessageData) messageData).isSucceed()) {
							m_Logger.severe("Survey added succssefuly");
							javafx.application.Platform.runLater(() -> {
								combobox_answer1.getValueFactory().setValue(0);
								combobox_answer2.getValueFactory().setValue(0);
								combobox_answer3.getValueFactory().setValue(0);
								combobox_answer4.getValueFactory().setValue(0);
								combobox_answer5.getValueFactory().setValue(0);
								combobox_answer6.getValueFactory().setValue(0);
								combobox_answer1.setValueFactory(svf1);
								combobox_answer2.setValueFactory(svf2);
								combobox_answer3.setValueFactory(svf3);
								combobox_answer4.setValueFactory(svf4);
								combobox_answer5.setValueFactory(svf5);
								combobox_answer6.setValueFactory(svf6);
								showAlertMessage("Successfully added", AlertType.INFORMATION);
							});
						}
					}
				}
			}
		} else if (correct_title.equals("Close Reservations")) {
			if (messageData instanceof EntitiesListData) {
				if (((EntitiesListData) messageData).getEntities().get(0) instanceof Reservation) {
					reservations = ((EntitiesListData) messageData).getEntities();
					Reservation reservation;
					for (int i = 0; i < reservations.size(); i++) {
						reservation = (Reservation) reservations.get(i);
						if (reservation.getType().equals(EntitiesEnums.ReservationType.Open)) {
							ReservationStatusRow statusRow = new ReservationStatusRow(reservation.getId(),
									reservation.getCostumerId(), reservation.getPrice(),
									reservation.getType().toString(), reservation.getDeliveryDate());
							open_reservations.add(statusRow);
						}
					}
					reservation_list.setAll(open_reservations);
					open_reservations.clear();
					javafx.application.Platform.runLater(() -> {
						drewContantToTable();
					});
					needToBeClosed();
				}
			} else if (messageData instanceof RespondMessageData) {
				if (((RespondMessageData) messageData).isSucceed()) {
					showAlertMessage("Your reservations have been successfully closed", AlertType.INFORMATION);
					reservation_list.clear();
					closes_reservations.clear();
					drewContantToTable();
					initialReservations();
				}
			}
		}
	}

	// end region -> BaseController Implementation

	// UI event region

	private void initializesurveys()
	{
		String username = m_ConnectedUser.getUserName();
		ShopEmployee shopemployee_entity = new ShopEmployee();
		shopemployee_entity.setUserName(username);
		Message msg = MessagesFactory.createGetEntityMessage(shopemployee_entity);
		m_Client.sendMessageToServer(msg);
		ArrayList<Integer> answers = new ArrayList<Integer>();
		for (int i = 1; i < 11; i++) {
			answers.add(i);
		}
		combobox_answer1.setValueFactory(svf1);
		combobox_answer2.setValueFactory(svf2);
		combobox_answer3.setValueFactory(svf3);
		combobox_answer4.setValueFactory(svf4);
		combobox_answer5.setValueFactory(svf5);
		combobox_answer6.setValueFactory(svf6);
	}

	/**
	 * Initialize Shop Sales table columns and define action when double click on
	 * table row.
	 *
	 */
	private void initializeConfigurationShopSalesTable()
	{
		reservation_table.setRowFactory(param -> {
			TableRow<ReservationStatusRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					ReservationStatusRow rowData = tableRow.getItem();

					if (rowData.getReservationStatus() == "Closed") return;

					Alert alert = new AlertBuilder().setAlertType(AlertType.CONFIRMATION)
							.setContentText("Are you sure you want to close the reservation?").build();
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						rowData.setReservationStatus("Closed");
						for (int i = 0; i < reservations.size(); i++) {
							if (rowData.getReservationId() == ((Reservation) reservations.get(i)).getId()) {
								((Reservation) reservations.get(i)).setType(EntitiesEnums.ReservationType.Closed);
								closes_reservations.add(((Reservation) reservations.get(i)));
							}
						}
					} else {
						return;
					}
					drewContantToTable();
				}
			});
			return tableRow;
		});

		tablecolumn_reservation_id
				.setCellValueFactory(new PropertyValueFactory<ReservationStatusRow, Integer>("reservationId"));
		tablecolumn_costumer_id
				.setCellValueFactory(new PropertyValueFactory<ReservationStatusRow, Integer>("costumerId"));
		tablecolumn_reservation_price
				.setCellValueFactory(new PropertyValueFactory<ReservationStatusRow, Float>("reservationPrice"));
		tablecolumn_reservation_type
				.setCellValueFactory(new PropertyValueFactory<ReservationStatusRow, String>("reservationStatus"));
		tablecolumn_delivery_date
				.setCellValueFactory(new PropertyValueFactory<ReservationStatusRow, String>("deliveryDateInFormat"));
		drewContantToTable();
	}

	/**
	 * Add survey to shop
	 *
	 * @param event
	 *            add button clicked
	 */
	@FXML
	private void addSurvey(ActionEvent event)
	{
		if ((combobox_answer1.getValue() == 0) || (combobox_answer2.getValue() == 0)
				|| (combobox_answer3.getValue() == 0) || (combobox_answer4.getValue() == 0)
				|| (combobox_answer5.getValue() == 0) || (combobox_answer6.getValue() == 0)) {
			showAlertMessage("One or more of the answers are 0 it needs to be at least 1.", AlertType.WARNING);
		} else {
			SurveyResult surveyResult = new SurveyResult();
			surveyResult.setFirstAnswer(combobox_answer1.getValue());
			surveyResult.setSecondAnswer(combobox_answer2.getValue());
			surveyResult.setThirdAnswer(combobox_answer3.getValue());
			surveyResult.setFourthanswer(combobox_answer4.getValue());
			surveyResult.setFifthAnswer(combobox_answer5.getValue());
			surveyResult.setSixthAnswer(combobox_answer6.getValue());
			surveyResult.setSurveyId(correct_survey.getId());
			surveyResult.setEnterDate(new Date());
			Message msg = MessagesFactory.createAddEntityMessage(surveyResult);
			m_Client.sendMessageToServer(msg);
		}
	}

	private void initialReservations()
	{
		reservation_list.clear();
		Reservation res = new Reservation();
		Message msg = MessagesFactory.createGetAllEntityMessage(res);
		m_Client.sendMessageToServer(msg);
	}

	@FXML
	private void closeReservation(ActionEvent event)
	{
		if (closes_reservations.isEmpty()) {
			showAlertMessage("There are no changes.", AlertType.INFORMATION);
			return;
		}
		Message msg = MessagesFactory.createUpdateEntitiesMessage(closes_reservations);
		m_Client.sendMessageToServer(msg);
	}

	/**
	 * Insert data into table and show the updated table.
	 * 
	 */
	private void drewContantToTable()
	{
		reservation_table.setItems(reservation_list);
		reservation_table.refresh();
	}

	private void needToBeClosed()
	{
		String string_toclose="";
		Date today=new Date();
		for(int i=0;i<reservations.size();i++)
		{
			Reservation res=(Reservation)reservations.get(i);
			if((res.getType().equals(EntitiesEnums.ReservationType.Open))&&(res.getDeliveryDate().getTime()<today.getTime()))
			{
				Date delivery=res.getDeliveryDate();
				if(string_toclose.equals(""))
				{
					string_toclose=string_toclose+" "+res.getId();
					
				}
				else
				{
					string_toclose=string_toclose+" , "+res.getId();
				}
				delivery.getTime();
			}
		}
		if(!(string_toclose.equals("")))
		{
			showAlertMessage("The following reservations require yours attention: " + string_toclose + " Since their delivery time has already passed!", AlertType.INFORMATION);
		}
	}
	// end region -> BUI event region
}
