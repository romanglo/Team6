
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
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

	// Data AnchorPane declaration
	@FXML private Pane pane_dataPane;

	@FXML private Pane comparePane;

	@FXML private AnchorPane anchorPane_viewStage;

	@FXML private AnchorPane anchorPane_mainStage;
	// End of --> Data AnchorPane declaration

	// Title images
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	// End of --> Title image

	// Selection area declaration
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
	// End of --> Selection area declaration

	// Charts declaration
	@FXML private BarChart<String, Number> barChart_currentChart;

	@FXML private BarChart<String, Number> compareChart;
	// End of --> Charts declaration

	/* End of --> UI Binding Fields region */

	/* Private fields */

	// Fields
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	// End of --> Fields region

	// Selection store reports
	private IncomesReport m_incomesReport;

	private ReservationsReport m_reservationsReport;

	private ComplaintsReport m_complaintsReport;

	private SurveysReport m_surveyReport;

	private IncomesReport m_compareIncomesReport;

	private ReservationsReport m_compareReservationsReport;

	private ComplaintsReport m_compareComplaintsReport;

	private SurveysReport m_compareSurveyReport;

	private ChangeListener<String> yearChangeListener;
	// End of --> Selection store reports

	// User ID (Only if User is ShopManager)
	private Integer m_shopManagerUserID;
	// End of --> User ID (Only if User is ShopManager)

	private boolean initializeFlag = true;

	private boolean firstTime = true;

	private boolean listenerFlag = true;

	/* End of --> Private fields */

	/* Local enums array */

	// Selection report type ComboBox data lists declaration
	ObservableList<String> reportsType = FXCollections.observableArrayList(
			EntitiesEnums.ReportType.Financial_Incomes_Report.toString(),
			EntitiesEnums.ReportType.Reservations_Report.toString(),
			EntitiesEnums.ReportType.Complaints_Report.toString(),
			EntitiesEnums.ReportType.Satisfaction_Report.toString());

	ObservableList<String> quarters = FXCollections.observableArrayList(EntitiesEnums.Quarter.Jan_Mar.toString(),
			EntitiesEnums.Quarter.Apr_Jun.toString(), EntitiesEnums.Quarter.Jul_Sep.toString(),
			EntitiesEnums.Quarter.Oct_Dec.toString());

	ObservableList<String> stores = FXCollections.observableArrayList("1");
	// End of --> Selection report type ComboBox data lists declaration

	// Month ArrayList
	final String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	// End of --> Month ArrayList

	/* End of --> Local enums array */

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
		else getStoreIdListFromServer();
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

	@FXML
	private void initializeSelection()
	{
		Calendar currentDate = Calendar.getInstance();
		int currentQuarter = currentDate.get(Calendar.MONTH);
		final int finalQuarter;
		int year = currentDate.get(Calendar.YEAR);

		if (currentQuarter >= 10) finalQuarter = 3;
		else if (currentQuarter >= 7) finalQuarter = 2;
		else if (currentQuarter >= 4) finalQuarter = 1;
		else {
			finalQuarter = 4;
			// year--;
		}

		textField_selectionYear.setText(Integer.toString(year));
		comboBox_selectionReportType.setItems(reportsType);
		Platform.runLater(() -> {
			comboBox_selectionReportType.setValue(reportsType.get(0));
		});
		comboBox_selectionQuarter.setItems(quarters);
		Platform.runLater(() -> {
			comboBox_selectionQuarter.setValue(quarters.get(finalQuarter - 1));
		});
		comboBox_selectionStore.setItems(stores);
		if (m_shopManagerUserID == null) Platform.runLater(() -> {
			comboBox_selectionStore.setValue(stores.get(0));
		});
		else Platform.runLater(() -> {
			comboBox_selectionStore.setValue(m_shopManagerUserID.toString());
		});

		if (ApplicationEntryPoint.ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
			comboBox_selectionQuarter.setVisible(false);
			textField_selectionYear.setVisible(false);
			button_compare.setVisible(false);
			comboBox_selectionStore.setVisible(false);
		}
		yearChangeListener = (obs, oldText, newText) -> selectionYearChanged(textField_selectionYear, (String) oldText);
		textField_selectionYear.textProperty().addListener(yearChangeListener);

		initializeStoreReportVariables();
	}

	private void initializeStoreReportVariables()
	{
		int storeId = Integer.parseInt(comboBox_selectionStore.getValue());
		int quarter = quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1;
		int yearInt = Integer.parseInt(textField_selectionYear.getText());
		Calendar year = Calendar.getInstance();
		year.set(Calendar.YEAR, yearInt);
		getStoreReportsFromServer(storeId, quarter, year.getTime());
	}

	@FXML
	private void initializeCompareReportVariables()
	{
		secondReportQuarter = new ComboBox<>(quarters);
		secondReportQuarter.setValue(comboBox_selectionQuarter.getValue());
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
		secondReportStore.setValue(comboBox_selectionStore.getValue());
		secondReportStore.setPrefWidth(93);
		secondReportStore.setOnAction(comboBox_selectionStore.getOnAction());
		secondReportQuarter.setOnAction(comboBox_selectionQuarter.getOnAction());
		secondSubmitButton = new Button("Show Report");
		secondSubmitButton.setPrefWidth(84);
		secondSubmitButton.setPrefHeight(25);
		secondSubmitButton.setOnAction(button_submit.getOnAction());
		yearChangeListener = (obs, oldText, newText) -> selectionYearChanged(secondReportYear, (String) oldText);
		secondReportYear.textProperty().addListener(yearChangeListener);
		m_compareIncomesReport = m_incomesReport;
		m_compareReservationsReport = m_reservationsReport;
		m_compareComplaintsReport = m_complaintsReport;
		m_compareSurveyReport = m_surveyReport;

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

	@FXML
	private void initializeChartPane()
	{
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		barChart_currentChart = new BarChart<String, Number>(xAxis, yAxis);
		barChart_currentChart.setCategoryGap(2.5);
		Platform.runLater(() -> {
			pane_dataPane.getChildren().add(barChart_currentChart);
		});
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
		mainStageSizeChanged_StageReorder();
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

	private boolean checkIfRegularReport(Report report, Calendar year)
	{
		if (!secondReportStore.isVisible()) return true;
		if (report.getQuarter() == (quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1)
				&& year.get(Calendar.YEAR) == Integer.parseInt(textField_selectionYear.getText())
				&& report.getShopManagerId() == Integer.parseInt(comboBox_selectionStore.getValue()))
			return true;
		return false;
	}

	private boolean checkIfCompareReport(Report report, Calendar year)
	{
		if (!secondReportStore.isVisible()) return false;
		if (report.getQuarter() == (quarters.indexOf(secondReportQuarter.getValue()) + 1)
				&& year.get(Calendar.YEAR) == Integer.parseInt(secondReportYear.getText())
				&& report.getShopManagerId() == Integer.parseInt(secondReportStore.getValue()))
			return true;
		return false;
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
	@FXML
	private void requestedReportDetailsChanged(ActionEvent event)
	{
		if (firstTime && event.getSource().equals(comboBox_selectionQuarter)) return;
		int storeID, quarter, yearInt = 2017;
		Calendar year = Calendar.getInstance();
		if (event.getSource().equals(comboBox_selectionQuarter) || event.getSource().equals(comboBox_selectionStore)) {
			storeID = Integer.parseInt(comboBox_selectionStore.getValue());
			yearInt = Integer.parseInt(textField_selectionYear.getText());
			quarter = quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1;
		} else {
			storeID = Integer.parseInt(secondReportStore.getValue());
			yearInt = Integer.parseInt(secondReportYear.getText());
			quarter = quarters.indexOf(secondReportQuarter.getValue()) + 1;
		}
		year.set(Calendar.YEAR, yearInt);
		getStoreReportsFromServer(storeID, quarter, year.getTime());
	}

	private void selectionYearChanged(TextField source, String oldText)
	{
		if (!listenerFlag) return;
		String sourceValue = source.getText();
		int yearInt, storeID, quarter;
		Calendar year = Calendar.getInstance();
		if (sourceValue.length() > 4) {
			errorMSG("Invalid year input!\nEnter 4 digit number.");
			listenerFlag = false;
			source.setText(oldText);
			listenerFlag = true;
			return;
		}
		try {
			yearInt = Integer.parseInt(sourceValue);
			year.set(Calendar.YEAR, yearInt);
		}
		catch (Exception e) {
			errorMSG("Invalid input!\nInsert only numbers.");
			listenerFlag = false;
			source.setText(oldText);
			listenerFlag = true;
			return;
		}
		if (sourceValue.length() == 4) {
			if (source.equals(textField_selectionYear)) {
				storeID = Integer.parseInt(comboBox_selectionStore.getValue());
				quarter = quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1;
			} else {
				storeID = Integer.parseInt(secondReportStore.getValue());
				quarter = quarters.indexOf(secondReportQuarter.getValue()) + 1;
			}
			getStoreReportsFromServer(storeID, quarter, year.getTime());
		}

	}

	@FXML
	private void showSelectionReport(ActionEvent event)
	{
		if (event.getSource().equals(button_submit)) {
			if (m_reservationsReport == null || m_complaintsReport == null || m_incomesReport == null
					|| m_surveyReport == null) {
				errorMSG("Report doesn't exist!");
				return;
			}
		} else if (m_compareIncomesReport == null || m_compareReservationsReport == null
				|| m_compareComplaintsReport == null || m_compareSurveyReport == null) {
			errorMSG("Report doesn't exist!");
			return;
		}

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

		if (secondReportStore.isVisible() && event.getSource().equals(button_submit))
			if (barChart_currentChart.getYAxis().getLabel() != compareChart.getYAxis().getLabel())
				Platform.runLater(() -> {
					secondSubmitButton.fire();
				});

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
			if (entity instanceof IncomesReport || entity instanceof ReservationsReport
					|| entity instanceof ComplaintsReport || entity instanceof SurveysReport) {
				if (firstTime) {
					if (entity instanceof IncomesReport) m_incomesReport = (IncomesReport) entity;
					else if (entity instanceof ReservationsReport) m_reservationsReport = (ReservationsReport) entity;
					else if (entity instanceof ComplaintsReport) m_complaintsReport = (ComplaintsReport) entity;
					else {
						m_surveyReport = (SurveysReport) entity;
						firstTime = false;
						if (initializeFlag) {
							initializeChartPane();
							initializeFlag = false;
						}
					}
				} else {
					Report report = (Report) entity;
					Calendar year = Calendar.getInstance();
					year.setTime(report.getYear());
					if (checkIfRegularReport(report, year)) {
						if (entity instanceof IncomesReport) m_incomesReport = (IncomesReport) entity;
						else if (entity instanceof ReservationsReport)
							m_reservationsReport = (ReservationsReport) entity;
						else if (entity instanceof ComplaintsReport) m_complaintsReport = (ComplaintsReport) entity;
						else {
							m_surveyReport = (SurveysReport) entity;
							if (initializeFlag) {
								initializeChartPane();
								initializeFlag = false;
							}
						}
					}
					if (checkIfCompareReport(report, year)) {
						if (entity instanceof IncomesReport) m_compareIncomesReport = (IncomesReport) entity;
						else if (entity instanceof ReservationsReport)
							m_compareReservationsReport = (ReservationsReport) entity;
						else if (entity instanceof ComplaintsReport)
							m_compareComplaintsReport = (ComplaintsReport) entity;
						else {
							m_compareSurveyReport = (SurveysReport) entity;
							if (initializeFlag) {
								initializeChartPane();
								initializeFlag = false;
							}
						}
					}
				}
			} else {
				m_shopManagerUserID = ((ShopManager) entity).getId();
				initializeSelection();
			}
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
			initializeSelection();
			// comboBox_selectionStore.setValue(stores.get(0));
		} // End of else if(messageData instanceof EntitiesListData)
		else if (messageData instanceof RespondMessageData) {
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			boolean succeed = respondMessageData.isSucceed();
			if (!succeed) {
				EntityData msgData = (EntityData) respondMessageData.getMessageData();
				IEntity entity = msgData.getEntity();
				if (entity instanceof IncomesReport || entity instanceof ReservationsReport
						|| entity instanceof ComplaintsReport || entity instanceof SurveysReport) {
					Report report = (Report) entity;
					Calendar year = Calendar.getInstance();
					year.setTime(report.getYear());
					if (checkIfRegularReport(report, year)) {
						if (entity instanceof IncomesReport) m_incomesReport = null;
						else if (entity instanceof ReservationsReport) m_reservationsReport = null;
						else if (entity instanceof ComplaintsReport) m_complaintsReport = null;
						else {
							m_surveyReport = null;
							Platform.runLater(() -> {
								barChart_currentChart.getData().clear();
							});
							errorMSG("There is no reports for the current store ID!");
						}
					}
					if (checkIfCompareReport(report, year)) {
						if (entity instanceof IncomesReport) m_compareIncomesReport = null;
						else if (entity instanceof ReservationsReport) m_compareReservationsReport = null;
						else if (entity instanceof ComplaintsReport) m_compareComplaintsReport = null;
						else {
							m_compareSurveyReport = null;
							Platform.runLater(() -> {
								compareChart.getData().clear();
							});
							errorMSG("There is no reports for the current store ID!");
						}
					}

				}
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
