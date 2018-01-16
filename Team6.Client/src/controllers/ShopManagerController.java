
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;
import newEntities.ComplaintsReport;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.IncomesReport;
import newEntities.Report;
import newEntities.ReservationsReport;
import newEntities.ShopManager;
import newEntities.SurveysReport;

/**
 *
 * CompanyEmployeeController : TODO Shimon: Create class description
 * 
 */
public class ShopManagerController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	/* Data AnchorPane declaration */
	@FXML private Pane pane_dataPane;

	@FXML private Pane comparePane;

	@FXML private AnchorPane anchorPane_viewStage;

	@FXML private AnchorPane anchorPane_mainStage;
	/* End of --> Data AnchorPane declaration */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	/* End of --> UI Binding Fields region */

	/* Selection area declaration */
	@FXML private ComboBox<String> comboBox_selectionReportType;

	@FXML private ComboBox<String> comboBox_selectionQuarter;

	@FXML private TextField textField_selectionYear;

	@FXML private ComboBox<String> comboBox_selectionStore;

	@FXML private Button button_submit;

	@FXML private Button button_compare;

	@FXML private TextField secondReportType;

	@FXML private ComboBox<String> secondReportQuarter;

	@FXML private TextField secondReportYear;

	@FXML private ComboBox<String> secondReportStore;

	@FXML private Button secondSubmitButton;
	/* End of --> Selection area declaration */

	/* Charts declaration */
	@FXML private BarChart<String, Number> barChart_currentChart;

	@FXML private BarChart<String, Number> compareChart;

	/* UI events region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

	/* Selection store reports */
	private IncomesReport m_incomesReport;

	private ReservationsReport m_reservationsReport;

	private ComplaintsReport m_complaintsReport;

	private SurveysReport m_surveyReport;

	private IncomesReport m_compareIncomesReport;

	private ReservationsReport m_compareReservationsReport;

	private ComplaintsReport m_compareComplaintsReport;

	private SurveysReport m_compareSurveyReport;
	/* End of --> Selection store reports */

	/* User ID (Only if User is ShopManager) */

	private Integer m_shopManagerUserID;

	/* End of --> User ID (Only if User is ShopManager) */

	/* Selection report type ComboBox data lists declaration */
	ObservableList<String> reportsType = FXCollections.observableArrayList(
			EntitiesEnums.ReportType.Financial_Incomes_Report.toString(),
			EntitiesEnums.ReportType.Reservations_Report.toString(),
			EntitiesEnums.ReportType.Complaints_Report.toString(),
			EntitiesEnums.ReportType.Satisfaction_Report.toString());

	ObservableList<String> quarters = FXCollections.observableArrayList(EntitiesEnums.Quarter.Jan_Mar.toString(),
			EntitiesEnums.Quarter.Apr_Jun.toString(), EntitiesEnums.Quarter.Jul_Sep.toString(),
			EntitiesEnums.Quarter.Oct_Dec.toString());

	ObservableList<String> stores = FXCollections.observableArrayList("1");
	/* End of --> Selection report type ComboBox data lists declaration */

	/* Month ArrayList */
	final String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	/* End of --> Month ArrayList */

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

		if (ApplicationEntryPoint.ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager)
			getShopManagerId();

		getStoreIdListFromServer();
		initializeSelection();
		initializeStoreReportVariables();
		initializeChartPane();
	}

	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
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

	private void initializeSelection()
	{
		Calendar currentDate = Calendar.getInstance();
		int currentQuarter = currentDate.get(Calendar.MONTH);

		if (currentQuarter >= 10) currentQuarter = 3;
		else if (currentQuarter >= 7) currentQuarter = 2;
		else if (currentQuarter >= 4) currentQuarter = 1;
		else currentQuarter = 4;

		comboBox_selectionReportType.setItems(reportsType);
		comboBox_selectionReportType.setValue(reportsType.get(0));
		comboBox_selectionQuarter.setItems(quarters);
		comboBox_selectionQuarter.setValue(quarters.get(currentQuarter - 1));
		;
		comboBox_selectionStore.setItems(stores);
		comboBox_selectionStore.setValue(stores.get(0));
		textField_selectionYear.setText(Integer.toString(currentDate.get(Calendar.YEAR)));
		if (ApplicationEntryPoint.ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
			comboBox_selectionQuarter.setVisible(false);
			textField_selectionYear.setVisible(false);
			button_compare.setVisible(false);
			comboBox_selectionStore.setVisible(false);
		}
	}

	@SuppressWarnings("deprecation")
	private void initializeStoreReportVariables()
	{
		int storeId = Integer.parseInt(comboBox_selectionStore.getValue());
		int quarter = quarters.indexOf(comboBox_selectionQuarter.getValue());
		int yearInt = Integer.parseInt(textField_selectionYear.getText()) - 1900;
		Date year = new Date();
		year.setYear(yearInt);
		getStoreReportsFromServer(storeId, quarter, year);
	}

	@SuppressWarnings("deprecation")
	private void initializeCompareReportVariables()
	{
		secondReportQuarter = new ComboBox<>(quarters);
		secondReportQuarter.setValue(quarters.get(0));
		secondReportQuarter.setPrefWidth(93);
		secondReportYear = new TextField();
		secondReportYear.setPrefWidth(69);
		secondReportYear.setText(textField_selectionYear.getText());
		secondReportType = new TextField();
		secondReportType.setPrefWidth(69);
		secondReportType.setText(comboBox_selectionReportType.getValue());
		secondReportType.setDisable(true);
		secondReportType.setPrefWidth(175);
		secondReportStore = new ComboBox<>(stores);
		secondReportStore.setValue(stores.get(0));
		secondReportStore.setPrefWidth(93);
		secondSubmitButton = new Button("Show Report");
		secondSubmitButton.setPrefWidth(84);
		secondSubmitButton.setPrefHeight(25);
		secondSubmitButton.setOnAction(button_submit.getOnAction());
		Date year = new Date();
		year.setYear(Integer.parseInt(secondReportYear.getText()));

		anchorPane_viewStage.getChildren().addAll(secondReportQuarter, secondReportYear, secondReportType,
				secondReportStore, secondSubmitButton);
		AnchorPane.setTopAnchor(((Node) secondReportQuarter), 50.0);
		AnchorPane.setTopAnchor(((Node) secondReportYear), 50.0);
		AnchorPane.setTopAnchor(((Node) secondReportType), 50.0);
		AnchorPane.setTopAnchor(((Node) secondReportStore), 50.0);
		AnchorPane.setTopAnchor(((Node) secondSubmitButton), 50.0);

		AnchorPane.setLeftAnchor(((Node) secondReportQuarter), 12.0);
		AnchorPane.setLeftAnchor(((Node) secondReportYear), 112.0);
		AnchorPane.setLeftAnchor(((Node) secondReportType), 187.0);
		AnchorPane.setLeftAnchor(((Node) secondReportStore), 369.0);
		AnchorPane.setLeftAnchor(((Node) secondSubmitButton), 466.0);
		secondReportStore.setVisible(true);
	}

	private void initializeChartPane()
	{
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		barChart_currentChart = new BarChart<String, Number>(xAxis, yAxis);
		barChart_currentChart.setCategoryGap(2.5);
		pane_dataPane.getChildren().add(barChart_currentChart);
		pane_dataPane.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				mainStageSizeChanged_StageReorder();
			}
		});
		pane_dataPane.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				mainStageSizeChanged_StageReorder();
			}
		});
	}

	/**
	 * Set new order and size for AnchorPane children accord to the new stage size
	 * 
	 */
	private void mainStageSizeChanged_StageReorder()
	{
		if (!(pane_dataPane.getChildren().isEmpty())) {
			if (comparePane == null)
				barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
			else {
				AnchorPane.setRightAnchor(((Node) pane_dataPane), (anchorPane_viewStage.getWidth() - 10) / 2);
				barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
				AnchorPane.setTopAnchor(((Node) comparePane), AnchorPane.getTopAnchor(pane_dataPane));
				AnchorPane.setLeftAnchor(((Node) comparePane), (anchorPane_viewStage.getWidth() - 10) / 2);
				AnchorPane.setRightAnchor(((Node) comparePane), 15.0);
				compareChart.setPrefSize(comparePane.getWidth(), comparePane.getHeight());
				button_submit.fire();
				secondSubmitButton.fire();
			}
		}
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
	 * Parse string to the month number.
	 * 
	 * @param monthString
	 *            Input string.
	 * @return The month number.
	 */
	private int parseMonthToNum(String monthString)
	{
		switch (monthString) {
			case "Jan":
				return 1;
			case "Apr":
				return 4;
			case "Jul":
				return 7;
			default:
				return 10;
		}
	}

	/**
	 * Create and initialize the compare report variables and insert them into main
	 * stage.
	 * 
	 */
	private void compareReportsNewStage()
	{
		AnchorPane.setRightAnchor(((Node) pane_dataPane), 380.0);
		AnchorPane.setTopAnchor(((Node) pane_dataPane), 75.0);

		initializeCompareReportVariables();

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		compareChart = new BarChart<String, Number>(xAxis, yAxis);
		compareChart.setCategoryGap(2.5);

		comparePane = new Pane(compareChart);
		anchorPane_viewStage.getChildren().add(comparePane);
		AnchorPane.setRightAnchor(((Node) comparePane), 15.0);
		AnchorPane.setTopAnchor(((Node) comparePane), 75.0);
		AnchorPane.setLeftAnchor(((Node) comparePane), 380.0);
		AnchorPane.setBottomAnchor(((Node) comparePane), 35.0);
	}

	/**
	 * Activate when Compare Report button pressed. Create and initialize compare
	 * report variables.
	 * 
	 */
	@FXML
	private void compareReports(ActionEvent event)
	{
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		compareReportsNewStage();
		button_compare.setDisable(true);
	}

	/**
	 * Activate when Report Type ComboBox value changed. Set the Compare report type
	 * accord to the default report type.
	 * 
	 */
	@FXML
	private void reportTypeChanged(ActionEvent event)
	{
		if (secondReportType == null) return;
		secondReportType.setText(comboBox_selectionReportType.getValue());
	}

	/**
	 * Activate when Store ComboBox value changed. Ask report from server.
	 * 
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@FXML
	private void selectionStoreChanged(ActionEvent event)
	{
		ComboBox<String> selectionComboBox = (ComboBox<String>) event.getSource();
		int storeId = Integer.parseInt(selectionComboBox.getValue());
		int quarter, yearInt;
		java.util.Date year = new Date();
		if (selectionComboBox.getItems().containsAll(secondReportStore.getItems())) {
			yearInt = Integer.parseInt(textField_selectionYear.getText()) - 1900;
			quarter = quarters.indexOf(comboBox_selectionQuarter.getValue());
			year.setYear(yearInt);
		} else {
			yearInt = Integer.parseInt(secondReportYear.getText()) - 1900;
			quarter = Integer.parseInt(comboBox_selectionQuarter.getValue());
			year.setYear(yearInt);
		}
		getStoreReportsFromServer(storeId, quarter, year);
	}

	@FXML
	private void showSelectionReport(ActionEvent event)
	{
		BarChart<String, Number> barToChange;
		String selectionStore, reportType, quarter;
		int selectionYear;
		Report[] reportData = new Report[4];
		try {
			if (event.getSource().equals(button_submit)) {
				barToChange = barChart_currentChart;
				selectionStore = comboBox_selectionStore.getValue();
				selectionYear = Integer.parseInt(textField_selectionYear.getText());
				reportType = comboBox_selectionReportType.getValue();
				quarter = comboBox_selectionQuarter.getValue();
				reportData[0] = m_incomesReport;
				reportData[1] = m_complaintsReport;
				reportData[2] = m_reservationsReport;
				reportData[3] = m_surveyReport;
			} else {
				barToChange = compareChart;
				selectionStore = secondReportStore.getValue();
				selectionYear = Integer.parseInt(secondReportYear.getText());
				reportType = secondReportType.getText();
				quarter = secondReportQuarter.getValue();
				reportData[0] = m_compareIncomesReport;
				reportData[1] = m_compareComplaintsReport;
				reportData[2] = m_compareReservationsReport;
				reportData[3] = m_compareSurveyReport;
			}
		}
		catch (NumberFormatException e) {
			errorMSG("The year you entered is invalid!");
			m_logger.warning("Entered invalid year input");
			return;
		}
		catch (Exception e) {
			errorMSG("Faild to read the inputed values");
			m_logger.warning("Faild to read the inputed values");
			return;
		}

		barToChange.setLegendVisible(false);
		switch (reportType) {
			case "Financial_Incomes_Report":
				showFinancialIncomesOrComplaintsReport("Financial Incomes", barToChange, selectionStore,
						Integer.toString(selectionYear), quarter, reportData[0]);
			break;
			case "Complaints_Report":
				showFinancialIncomesOrComplaintsReport("Complaints", barToChange, selectionStore,
						Integer.toString(selectionYear), quarter, reportData[1]);
			break;
			case "Reservations_Report":
				barToChange.setLegendVisible(true);
				showReservationsReport(barToChange, selectionStore, Integer.toString(selectionYear), quarter,
						(ReservationsReport) reportData[2]);
			break;
			default:
				showSatisfactionReport(barToChange, selectionStore, Integer.toString(selectionYear), quarter,
						(SurveysReport) reportData[3]);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showFinancialIncomesOrComplaintsReport(String reportTypeName, BarChart barToChange, String storeId,
			String year, String quarter, Report reportData)
	{
		int monthNum = parseMonthToNum(quarter.substring(0, 3));

		// clear previous chart data and insert new values.
		barChartAfterChangeInitialize(barToChange, storeId, reportTypeName, year, quarter);

		// Chart data declaration.
		XYChart.Series barDataSeries = new XYChart.Series();
		XYChart.Data firstMonth;
		XYChart.Data secondMonth;
		XYChart.Data thirdMonth;

		// Set bars data accord to reportType.
		switch (reportTypeName) {
			case "Financial Incomes":
				firstMonth = new XYChart.Data(month[monthNum], ((IncomesReport) reportData).getIncomesInFirstMonth());
				secondMonth = new XYChart.Data(month[monthNum + 1],
						((IncomesReport) reportData).getIncomesInSecondMonth());
				thirdMonth = new XYChart.Data(month[monthNum + 2],
						((IncomesReport) reportData).getIncomesInThirdMonth());
			break;
			default:
				firstMonth = new XYChart.Data(month[monthNum],
						((ComplaintsReport) reportData).getNumberOfComplaintsFirstMonth());
				secondMonth = new XYChart.Data(month[monthNum + 1],
						((ComplaintsReport) reportData).getNumberOfComplaintsSecondMonth());
				thirdMonth = new XYChart.Data(month[monthNum + 2],
						((ComplaintsReport) reportData).getNumberOfComplaintsThirdMonth());
		}

		// Color changing for the second and the third bars.
		secondMonth.nodeProperty().addListener(new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode)
			{
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;");
				}
			}
		});
		thirdMonth.nodeProperty().addListener(new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode)
			{
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;");
				}
			}
		});

		// Add data bars to chart.
		barDataSeries.getData().add(firstMonth);
		barDataSeries.getData().add(secondMonth);
		barDataSeries.getData().add(thirdMonth);
		barToChange.getData().addAll(barDataSeries);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showReservationsReport(BarChart barToChange, String storeId, String year, String quarter,
			ReservationsReport reportData)
	{
		int monthNum = parseMonthToNum(quarter.substring(0, 3));

		// clear previous chart data and insert new values.
		barChartAfterChangeInitialize(barToChange, storeId, "Reservations", year, quarter);

		// Set Flower reservation data.
		XYChart.Series firstType = new XYChart.Series();
		firstType.setName("Flower");
		firstType.getData().add(new XYChart.Data(month[monthNum], reportData.getNumberOfOrderedFlowersInFirstMonth()));
		firstType.getData()
				.add(new XYChart.Data(month[monthNum + 1], reportData.getNumberOfOrderedFlowersInSecondMonth()));
		firstType.getData()
				.add(new XYChart.Data(month[monthNum + 2], reportData.getNumberOfOrderedFlowersInThirdMonth()));

		// Set FlowerPot reservation data.
		XYChart.Series secondType = new XYChart.Series();
		secondType.setName("FlowerPot");
		secondType.getData()
				.add(new XYChart.Data(month[monthNum], reportData.getNumberOfOrderedFlowerPotsInFirstMonth()));
		secondType.getData()
				.add(new XYChart.Data(month[monthNum + 1], reportData.getNumberOfOrderedFlowerPotsInSecondMonth()));
		secondType.getData()
				.add(new XYChart.Data(month[monthNum + 2], reportData.getNumberOfOrderedFlowerPotsInThirdMonth()));

		// Set BridalBucket reservation data.
		XYChart.Series thirdType = new XYChart.Series();
		thirdType.setName("BridalBucket");
		thirdType.getData()
				.add(new XYChart.Data(month[monthNum], reportData.getNumberOfOrderedBridalBouquetsInFirstMonth()));
		thirdType.getData()
				.add(new XYChart.Data(month[monthNum + 1], reportData.getNumberOfOrderedBridalBouquetsInSecondMonth()));
		thirdType.getData()
				.add(new XYChart.Data(month[monthNum + 2], reportData.getNumberOfOrderedBridalBouquetsInThirdMonth()));

		// Set FlowerArrangements reservation data.
		XYChart.Series fourthType = new XYChart.Series();
		fourthType.setName("FlowerArrangements");
		fourthType.getData()
				.add(new XYChart.Data(month[monthNum], reportData.getNumberOfOrderedFlowerArrangementsInFirstMonth()));
		fourthType.getData().add(
				new XYChart.Data(month[monthNum + 1], reportData.getNumberOfOrderedFlowerArrangementsInSecondMonth()));
		fourthType.getData().add(
				new XYChart.Data(month[monthNum + 2], reportData.getNumberOfOrderedFlowerArrangementsInThirdMonth()));

		// Add data to chart.
		barToChange.getData().addAll(firstType, secondType, thirdType, fourthType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showSatisfactionReport(BarChart barToChange, String storeId, String year, String quarter,
			SurveysReport reportData)
	{
		// clear previous chart data and insert new values.
		barChartAfterChangeInitialize(barToChange, storeId, "Satisfaction", year, quarter);

		// Set survey result data.
		XYChart.Series Question = new XYChart.Series();
		Question.getData().add(new XYChart.Data("Question1", reportData.getFirstAnswerAverage()));
		Question.getData().add(new XYChart.Data("Question2", reportData.getSecondAnswerAverage()));
		Question.getData().add(new XYChart.Data("Question3", reportData.getThirdAnswerAverage()));
		Question.getData().add(new XYChart.Data("Question4", reportData.getFourthAnswerAverage()));
		Question.getData().add(new XYChart.Data("Question5", reportData.getFifthAnswerAverage()));
		Question.getData().add(new XYChart.Data("Question6", reportData.getSixthAnswerAverage()));

		// Add data to chart.
		barToChange.getData().addAll(Question);
	}

	@SuppressWarnings("rawtypes")
	private void barChartAfterChangeInitialize(BarChart barToInit, String storeId, String reportType, String year,
			String quarter)
	{
		// clear previous chart data and insert new values.
		barToInit.getData().clear();
		barToInit.setTitle(reportType + " Summary. Store ID: " + storeId);
		barToInit.getXAxis().setLabel(quarter + " " + year);
		barToInit.getYAxis().setLabel(reportType);
	}

	/* End of --> Initializing methods region */

	/* Sending message to server methods region */

	private void getStoreIdListFromServer()
	{
		Message entityMessage;
		entityMessage = MessagesFactory.createGetAllEntityMessage(new ShopManager());
		m_client.sendMessageToServer(entityMessage);
	}

	private void getShopManagerId()
	{
		ShopManager shopManager = new ShopManager();
		shopManager.setUserName(ApplicationEntryPoint.ConnectedUser.getUserName());
		shopManager.setPrivilege(ApplicationEntryPoint.ConnectedUser.getPrivilege());
		shopManager.setEmail(ApplicationEntryPoint.ConnectedUser.getEmail());
		Message entityMessage = MessagesFactory.createGetEntityMessage(shopManager);
		m_client.sendMessageToServer(entityMessage);
	}

	/**
	 * Activate when Store ComboBox value changed. Send Get messages for all store
	 * report.
	 * 
	 */
	private void getStoreReportsFromServer(int store, int quarter, java.util.Date year)
	{
		Report report;
		Message entityMessage;

		// Ask from server for IncomesReport
		report = new IncomesReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_client.sendMessageToServer(entityMessage);

		// Ask from server for ReservationsReport
		report = new ReservationsReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_client.sendMessageToServer(entityMessage);

		// Ask from server for ComplaintsReport
		report = new ComplaintsReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_client.sendMessageToServer(entityMessage);

		// Ask from server for SurveysReport
		report = new SurveysReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_client.sendMessageToServer(entityMessage);
	}
	/* End of --> Sending message to server methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (!(messageData instanceof EntityData) && !(messageData instanceof RespondMessageData)
				&& !(messageData instanceof EntitiesListData)) {
			m_logger.warning("Received message data not of the type requested.");
			return;
		}

		if (messageData instanceof EntityData) {
			IEntity entity = ((EntityData) messageData).getEntity();
			if (entity instanceof IncomesReport) {
				IncomesReport report = (IncomesReport) entity;
				if (m_compareIncomesReport != null) {
					if (report.getQuarter() == quarters.indexOf(comboBox_selectionQuarter.getValue())
							&& report.getYear().getYear() == Integer.parseInt(textField_selectionYear.getText())
							&& report.getShopManagerId() == Integer.parseInt(comboBox_selectionStore.getValue()))
						m_incomesReport = report;
					else m_compareIncomesReport = report;
				} // End of if(m_compareIncomesReport != null).
				else {
					m_incomesReport = report;
					m_compareIncomesReport = m_incomesReport;
				} // End of else -> if(m_compareIncomesReport != null).
			} // End of if (entity instanceof IncomesReport).
			else if (entity instanceof ReservationsReport) {
				ReservationsReport report = (ReservationsReport) entity;
				if (m_compareReservationsReport != null) {
					if (report.getQuarter() == quarters.indexOf(comboBox_selectionQuarter.getValue())
							&& report.getYear().getYear() == Integer.parseInt(textField_selectionYear.getText())
							&& report.getShopManagerId() == Integer.parseInt(comboBox_selectionStore.getValue()))
						m_reservationsReport = report;
					else m_compareReservationsReport = report;
				} // End of if(m_compareReservationsReport != null).
				else {
					m_reservationsReport = report;
					m_compareReservationsReport = m_reservationsReport;
				} // End of else -> if(m_compareReservationsReport != null).
			} else if (entity instanceof ComplaintsReport) {
				ComplaintsReport report = (ComplaintsReport) entity;
				if (m_compareComplaintsReport != null) {
					if (report.getQuarter() == quarters.indexOf(comboBox_selectionQuarter.getValue())
							&& report.getYear().getYear() == Integer.parseInt(textField_selectionYear.getText())
							&& report.getShopManagerId() == Integer.parseInt(comboBox_selectionStore.getValue()))
						m_complaintsReport = report;
					else m_compareComplaintsReport = report;
				} // End of if(m_compareComplaintsReport != null).
				else {
					m_complaintsReport = report;
					m_compareComplaintsReport = m_complaintsReport;
				} // End of else -> if(m_compareComplaintsReport != null).
			} else if (entity instanceof SurveysReport) {
				SurveysReport report = (SurveysReport) entity;
				if (m_compareSurveyReport != null) {
					if (report.getQuarter() == quarters.indexOf(comboBox_selectionQuarter.getValue())
							&& report.getYear().getYear() == Integer.parseInt(textField_selectionYear.getText())
							&& report.getShopManagerId() == Integer.parseInt(comboBox_selectionStore.getValue()))
						m_surveyReport = report;
					else m_compareSurveyReport = report;
				} // End of if(m_compareSurveyReport != null).
				else {
					m_surveyReport = report;
					m_compareSurveyReport = m_surveyReport;
				} // End of else -> if(m_compareSurveyReport != null).
			} else m_shopManagerUserID = ((ShopManager) entity).getId();
		} // End of if (messageData instanceof EntityData)
		else if (messageData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
			Integer shopManagerId;
			stores.clear();
			for (IEntity entity : entities) {
				if (!(entity instanceof ShopManager)) {
					m_logger.warning("Received entity not of the type requested.");
					return;
				}
				shopManagerId = ((ShopManager) entity).getId();
				stores.add(shopManagerId.toString());
			}
			comboBox_selectionStore.setValue(stores.get(0));
		} // End of else if(messageData instanceof EntitiesListData)
		else if (messageData instanceof RespondMessageData) {
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			boolean succeed = respondMessageData.isSucceed();
			if (!succeed) {
				errorMSG("There is no reports for the current store ID!");
			}
		} // End of else if (messageData instanceof EntitiesListData)
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
