
package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import boundaries.CatalogItemRow;
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
import javafx.scene.layout.AnchorPane;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.Reservation;
import newEntities.ShopEmployee;
import newEntities.Survey;
import newEntities.SurveyResult;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * ShopEmployeeController: Manages shop employee UI.
 * 
 * 
 */
public class ShopEmployeeController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	private @FXML AnchorPane anchorpane_option2;

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

	private String[] questions = { "Question1", "Question2", "Question3", "Question4", "Question5", "Question6" };

	private SpinnerValueFactory<Integer> svf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	private SpinnerValueFactory<Integer> svf6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	List<IEntity> reservations;

	ArrayList<CatalogItemRow> open_reservations = new ArrayList<>();

	private ObservableList<CatalogItemRow> reservation_list = FXCollections.observableArrayList();

	private int selected_reservation_id;

	private Reservation selected_res;

	private @FXML Label enter_date;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private @FXML TableView<CatalogItemRow> reservation_table;

	private @FXML TableColumn<CatalogItemRow, Integer> tablecolumn_reservation_id;

	private @FXML TableColumn<CatalogItemRow, Float> tablecolumn_reservation_price;

	private @FXML TableColumn<CatalogItemRow, String> tablecolumn_reservation_type;

	private ArrayList<IEntity> closes_reservations=new ArrayList<>();
	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		initializeConfigurationShopSalesTable();
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
				anchorpane_option1.setVisible(true);
				anchorpane_option2.setVisible(false);
				initializesurveys();
			break;
			case "Close Reservations":
				anchorpane_option1.setVisible(false);
				anchorpane_option2.setVisible(true);
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
						showInformationMessage("There are no surveys for your shop.");
						anchorpane_option1.setVisible(false);
					} else {
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
								showInformationMessage("Successfully added");
							});

						}
					}
				}
			}
		} else if (correct_title.equals("Close Reservations")) {
			if (messageData instanceof EntitiesListData) {
				if (((EntitiesListData) messageData).getEntities().get(0) instanceof Reservation) {
					reservations = ((EntitiesListData) messageData).getEntities();
					for (int i = 0; i < reservations.size(); i++) {
						if (((Reservation) reservations.get(i)).getType().equals(EntitiesEnums.ReservationType.Open)) {
							CatalogItemRow catlogitem = new CatalogItemRow(((Reservation) reservations.get(i)).getId(),
									null, ((Reservation) reservations.get(i)).getPrice(), null, null,
									((Reservation) reservations.get(i)).getType().toString());
							open_reservations.add(catlogitem);
						}
					}
					reservation_list.setAll(open_reservations);
					open_reservations.clear();
					javafx.application.Platform.runLater(() -> {
						drewContantToTable();
					});
				}
			} else if (messageData instanceof RespondMessageData) {
				if (((RespondMessageData) messageData).isSucceed()) {
					showInformationMessage("Your reservations have been successfully closed");
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
		ObservableList<Integer> list = FXCollections.observableArrayList(answers);
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
			TableRow<CatalogItemRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					CatalogItemRow rowData = tableRow.getItem();
					
					if(rowData.getType() == "Closed")
						return;
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Close Reservation");
					alert.setHeaderText("Close Reservation");
					alert.setContentText("Are you sure you want to close the reservation?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
					    rowData.setM_type("Closed");
					    for(int i=0;i<reservations.size();i++)
					    {
					    	if(rowData.getM_id()==((Reservation)reservations.get(i)).getId())
					    	{
					    		((Reservation)reservations.get(i)).setType(EntitiesEnums.ReservationType.Closed);
					    		closes_reservations.add(((Reservation)reservations.get(i)));
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

		tablecolumn_reservation_id.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Integer>("id"));
		tablecolumn_reservation_price.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, Float>("price"));
		tablecolumn_reservation_type.setCellValueFactory(new PropertyValueFactory<CatalogItemRow, String>("type"));
		drewContantToTable();
	}

	@FXML
	public void addSurvey(ActionEvent event)
	{
		if ((combobox_answer1.getValue() == 0) || (combobox_answer2.getValue() == 0)
				|| (combobox_answer3.getValue() == 0) || (combobox_answer4.getValue() == 0)
				|| (combobox_answer5.getValue() == 0) || (combobox_answer6.getValue() == 0)) {
			showInformationMessage("One or more of the answers are 0 it needs to be at least 1.");
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
		Message msg=MessagesFactory.createUpdateEntitiesMessage(closes_reservations);
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

	// end region -> BUI event region
}
