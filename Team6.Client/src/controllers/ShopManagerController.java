
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import boundaries.ShopCostumerRow;
import boundaries.ToggleSwitch;
import entities.ComplaintsReport;
import entities.Costumer;
import entities.EntitiesEnums;
import entities.IEntity;
import entities.IncomesReport;
import entities.Report;
import entities.ReservationsReport;
import entities.ShopCostumer;
import entities.ShopManager;
import entities.SurveysReport;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messages.EntitiesListData;
import messages.EntityData;
import messages.EntityDataOperation;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 * ShopManagerController : Managed the Shop Manager and Chain Manager UI.
 * 
 */
public class ShopManagerController extends BaseController
{

	// region Actors Panes

	@FXML private AnchorPane anchorpane_shopCostumerManagement;

	@FXML private AnchorPane anchorPane_mainStage;

	// end region -> Actors Panes

	/* Reports stage */

	// Data AnchorPanes declaration
	@FXML private Pane pane_dataPane;

	@FXML private Pane comparePane;

	@FXML private AnchorPane anchorPane_viewStage;
	// End of --> Data AnchorPane declaration

	// Selection area declaration
	@FXML private ComboBox<String> comboBox_selectionReportType;

	@FXML private ComboBox<String> comboBox_selectionQuarter;

	@FXML private Spinner<Integer> spinner_selectionYear;

	@FXML private ComboBox<String> comboBox_selectionStore;

	@FXML private Button button_submit;

	@FXML private TextField secondReportType;

	@FXML private ComboBox<String> secondReportQuarter;

	@FXML private Spinner<Integer> secondReportYear;

	@FXML private ComboBox<String> secondReportStore;

	@FXML private Button secondSubmitButton;

	@FXML private ToggleSwitch toggleSwitch_compareReport;

	@FXML private Label label_compare;

	@FXML private Label label_year;

	@FXML private Label label_quarter;

	@FXML private Label label_store;
	// End of --> Selection area declaration

	// Charts declaration
	@FXML private BarChart<String, Number> barChart_currentChart;

	@FXML private BarChart<String, Number> compareChart;

	@FXML private Label label_noReports;
	// End of --> Charts declaration

	/* Private fields */

	// Year Spinners Value Factory
	private SpinnerValueFactory<Integer> spinnerValueFactory_selectionYear;

	private SpinnerValueFactory<Integer> spinnerValueFactory_secondSelectionYear;
	// End of --> Year Spinners Value Factory

	// Selection store reports
	private IncomesReport m_incomesReport;

	private ReservationsReport m_reservationsReport;

	private ComplaintsReport m_complaintsReport;

	private SurveysReport m_surveyReport;

	private IncomesReport m_compareIncomesReport;

	private ReservationsReport m_compareReservationsReport;

	private ComplaintsReport m_compareComplaintsReport;

	private SurveysReport m_compareSurveyReport;
	// End of --> Selection store reports

	// Flags
	private boolean firstTime = true;

	private boolean regularReport = true;
	// End of --> Flags

	/* End of --> Private fields */

	/* Local enums array */

	// Selection report type ComboBox data lists declaration
	ObservableList<String> reportsType = FXCollections.observableArrayList(
			EntitiesEnums.ReportType.Financial_Incomes_Report.toString().replaceAll("_", " "),
			EntitiesEnums.ReportType.Reservations_Report.toString().replaceAll("_", " "),
			EntitiesEnums.ReportType.Complaints_Report.toString().replaceAll("_", " "),
			EntitiesEnums.ReportType.Satisfaction_Report.toString().replaceAll("_", " "));

	ObservableList<String> quarters = FXCollections.observableArrayList(
			EntitiesEnums.Quarter.Jan_Mar.toString().replaceAll("_", " - "),
			EntitiesEnums.Quarter.Apr_Jun.toString().replaceAll("_", " - "),
			EntitiesEnums.Quarter.Jul_Sep.toString().replaceAll("_", " - "),
			EntitiesEnums.Quarter.Oct_Dec.toString().replaceAll("_", " - "));

	ObservableList<String> stores = FXCollections.observableArrayList("1");
	// End of --> Selection report type ComboBox data lists declaration

	// Month ArrayList
	final String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	// End of --> Month ArrayList

	/* End of --> Local enums array */
	/* End of --> Reports stage */

	// User ID (Only if User is ShopManager)
	private Integer m_shopManagerUserID;

	private ShopManager m_shopManagerUser;
	// End of --> User ID (Only if User is ShopManager)

	/* Shop Manager - Add Costumer To Shop Stage Fields */

	// Side Bar Menu VBbox
	@FXML private VBox vbox_sidebar;
	// End of --> Side Bar Menu VBbox

	// Add New Shop Costumer Fields
	@FXML private TextField textField_addShopCostumer;

	@FXML private Button button_addShopCostumer;

	@FXML private TextField textFiled_costumerDetails;

	@FXML private Button button_showCostumerDetails;
	// End of --> Add New Shop Costumer Fields

	// Subscription Table
	@FXML private TableView<ShopCostumerRow> tableView_shopCostumer;

	@FXML private TableColumn<ShopCostumerRow, Integer> tableColumn_shopCostumerID;

	@FXML private TableColumn<ShopCostumerRow, String> tableColumn_shopCostumerSubscription;

	@FXML private TableColumn<ShopCostumerRow, Date> tableColumn_shopCostumerSubscriptionStartDate;

	@FXML private TableColumn<ShopCostumerRow, String> tableColumn_shopCostumerCreditCard;

	@FXML private TableColumn<ShopCostumerRow, Float> tableColumn_shopCostumerCumulativePrice;
	// End of --> Subscription Table

	// Flags
	private boolean initFlag = true;
	// End of --> Flags

	// Registered Company Costumers
	private ArrayList<Costumer> costumers = new ArrayList<>();
	// End of --> Registered Company Costumers

	// Registered Shop Costumers
	ObservableList<ShopCostumerRow> costumerInShop = FXCollections.observableArrayList();
	// End of --> Registered Shop Costumers

	/* End of -> Shop Manager - Add Costumer To Shop Stage Fields */

	// region BaseController Implementation

	// ----------------------------------------------------------------------------INIT-------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		if (m_ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
			getShopManagerUser();
			getAllCostumer();
		} else {
			vbox_sidebar.getChildren().get(1).setVisible(false);
			getStoreIdListFromServer();
		}
		initializeChartPane();
	}

	/**
	 * Initialize all report selection fields accord to the current value in DB.
	 *
	 */
	@FXML
	private void initializeSelection()
	{
		Calendar currentDate = Calendar.getInstance();
		int currentQuarter = currentDate.get(Calendar.MONTH);
		int year = currentDate.get(Calendar.YEAR);
		final int finalQuarter;

		if (currentQuarter >= 10) finalQuarter = 3;
		else if (currentQuarter >= 7) finalQuarter = 2;
		else if (currentQuarter >= 4) finalQuarter = 1;
		else {
			finalQuarter = 4;
			year--;
		}

		toggleSwitch_compareReport.setSwitchedListener((isOn) -> reprotStageChange(isOn));
		spinnerValueFactory_selectionYear = new SpinnerValueFactory.IntegerSpinnerValueFactory(2010, year);
		spinnerValueFactory_secondSelectionYear = new SpinnerValueFactory.IntegerSpinnerValueFactory(2010, year);
		spinner_selectionYear.setValueFactory(spinnerValueFactory_selectionYear);
		spinner_selectionYear.getValueFactory().setValue(year);
		comboBox_selectionReportType.setItems(reportsType);
		comboBox_selectionQuarter.setItems(quarters);
		comboBox_selectionStore.setItems(stores);
		Platform.runLater(() -> {
			comboBox_selectionReportType.setValue(reportsType.get(0));
			comboBox_selectionQuarter.setValue(quarters.get(finalQuarter - 1));
			if (m_shopManagerUserID == null) comboBox_selectionStore.setValue(stores.get(0));
			else comboBox_selectionStore.setValue(m_shopManagerUserID + " - " + m_shopManagerUser.getName());

			if (m_ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
				comboBox_selectionQuarter.setVisible(false);
				spinner_selectionYear.setVisible(false);
				comboBox_selectionStore.setVisible(false);
				label_year.setVisible(false);
				label_quarter.setVisible(false);
				label_store.setVisible(false);
				toggleSwitch_compareReport.setVisible(false);
				label_compare.setVisible(false);
				AnchorPane.setBottomAnchor(((Node) pane_dataPane), 15.0);
			}
		});

		getReportByCurrentValues("Regular Report");
	}

	/**
	 * Initialize compare reports values when Compare stage requested.
	 *
	 */
	@FXML
	private void initializeCompareReportVariables()
	{
		secondReportQuarter = new ComboBox<>(quarters);
		secondReportYear = new Spinner<>();
		secondReportType = new TextField();
		secondReportStore = new ComboBox<>(stores);
		secondSubmitButton = new Button("Show Report");

		secondReportQuarter.setPrefWidth(93);
		secondReportYear.setPrefWidth(69);
		secondReportType.setPrefWidth(80);
		secondReportType.setPrefWidth(175);
		secondReportStore.setPrefWidth(120);
		secondSubmitButton.setPrefWidth(100);
		secondSubmitButton.setPrefHeight(25);

		secondReportQuarter.setValue(comboBox_selectionQuarter.getValue());
		secondReportYear.setValueFactory(spinnerValueFactory_secondSelectionYear);
		secondReportYear.getValueFactory().setValue(spinner_selectionYear.getValue());
		secondReportType.setText(comboBox_selectionReportType.getValue());
		secondReportType.setDisable(true);
		secondReportStore.setValue(comboBox_selectionStore.getValue());
		secondSubmitButton.setOnAction(button_submit.getOnAction());

		if (barChart_currentChart.getTitle() != null) {
			String reportType = secondReportType.getText();
			reportType = reportType.replaceAll("Report", "");
			barChartAfterChangeInitialize(compareChart, secondReportStore.getValue(), reportType,
					secondReportYear.getValue().toString(), secondReportQuarter.getValue());
		}

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
		AnchorPane.setLeftAnchor(((Node) secondReportStore), 373.0);
		AnchorPane.setLeftAnchor(((Node) secondSubmitButton), 500.0);
		secondReportStore.setVisible(true);
	}

	/**
	 * Initialize the pane of regular(not compare) report BarChart view.
	 *
	 */
	@FXML
	private void initializeChartPane()
	{
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		barChart_currentChart = new BarChart<String, Number>(xAxis, yAxis);

		Platform.runLater(() -> {
			pane_dataPane.getChildren().add(barChart_currentChart);
			mainStageSizeChanged_StageReorder();
		});
		// Add listener to pane size.
		pane_dataPane.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Platform.runLater(() -> {
					mainStageSizeChanged_StageReorder();
				});
			}
		});
		pane_dataPane.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Platform.runLater(() -> {
					mainStageSizeChanged_StageReorder();
				});
			}
		});
	}

	/**
	 * 
	 * Initialize Shop Sales table columns and define action when double click on
	 * table row.
	 *
	 */
	private void initializeShopCostumerConfigurationTable()
	{
		tableView_shopCostumer.setRowFactory(param -> {
			TableRow<ShopCostumerRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					ShopCostumerRow rowData = tableRow.getItem();
					if (rowData.getShopCostumerSubscription() != EntitiesEnums.CostumerSubscription.None.toString()) {
						showInformationMessage("Client already have subscirption.");
						return;
					}
					String optionalCreditCard = null;
					if (rowData.getCreditCard() == "None") {
						TextInputDialog addCreditCard = new TextInputDialog();
						addCreditCard.setTitle("Add Costumer Credit Card");
						addCreditCard.setHeaderText("Add credit card to Costumer ID -> " + rowData.getShopCostumerID());
						addCreditCard.setContentText("Credit Card Number: ");
						addCreditCard.contentTextProperty().addListener((obs, oldValue, newValue) -> {
							if (newValue.length() == 17) addCreditCard.setContentText(oldValue);
						});
						// Traditional way to get the response value.
						Optional<String> result = addCreditCard.showAndWait();
						if (result.isPresent()) {
							if (result.get().isEmpty()) {
								showInformationMessage("Please fill Credit Card filed in order to continue.");
								return;
							}
							optionalCreditCard = result.get();
						} else {
							showInformationMessage("Costumer subscription required valid credit card.");
							return;
						}
					}
					List<String> choices = new ArrayList<>();
					choices.add("Monthly");
					choices.add("Yearly");

					ChoiceDialog<String> dialog = new ChoiceDialog<>("None", choices);
					dialog.setTitle("Edit Costumer Subscription");
					dialog.setHeaderText("Change Subscription To Costumer ID -> " + rowData.getShopCostumerID());
					dialog.setContentText("Choose costumer new subscription:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						if (result.get().equals("None")) return;
						if (optionalCreditCard != null) rowData.setCreditCard(optionalCreditCard);
						saveShopCostumerSubscription(rowData, result.get());
					}
					drewContantToTable();
				}
			});
			return tableRow;
		});

		tableColumn_shopCostumerID.setCellValueFactory(new PropertyValueFactory<ShopCostumerRow, Integer>("ID"));
		tableColumn_shopCostumerSubscription
				.setCellValueFactory(new PropertyValueFactory<ShopCostumerRow, String>("Subscription"));
		tableColumn_shopCostumerSubscriptionStartDate
				.setCellValueFactory(new PropertyValueFactory<ShopCostumerRow, Date>("StartDate"));
		tableColumn_shopCostumerCreditCard
				.setCellValueFactory(new PropertyValueFactory<ShopCostumerRow, String>("CreditCard"));
		tableColumn_shopCostumerCumulativePrice
				.setCellValueFactory(new PropertyValueFactory<ShopCostumerRow, Float>("CumulativePrice"));
		drewContantToTable();
	}

	// --------------------------------------------------------------------------INIT---------------------------------------------------------------

	// ----------------------------------------------------------------------------PRIVATE-----------------------------------------------------------

	/**
	 * Re-set Subscription table values.
	 *
	 */
	private void drewContantToTable()
	{
		tableView_shopCostumer.setItems(costumerInShop);
		tableView_shopCostumer.refresh();
	}

	/**
	 * Update Subscription table.
	 *
	 * @param updatedShopCostumer
	 *            Edited Shop Costumer.
	 */
	private void updateTable(ShopCostumer updatedShopCostumer)
	{
		for (ShopCostumerRow rowData : costumerInShop)
			if (rowData.getShopCostumerID() == updatedShopCostumer.getCostumerId()) {
				rowData.setShopCostumerSubscription(updatedShopCostumer.getCostumerSubscription().toString());
				rowData.setSubscriptionStartDate(updatedShopCostumer.getSubscriptionStartDate());
			}
		drewContantToTable();
	}

	/**
	 * Check if Costumer already signed to the shop.
	 *
	 * @param costumerID
	 *            The Costumer ID.
	 * @return <code>true</code> if Costumer already signed to the current store and
	 *         return <code>false</code> otherwise.
	 */
	private boolean checkIfShopCostumerAlreadyExist(int costumerID)
	{
		for (ShopCostumerRow rowData : costumerInShop)
			if (rowData.getShopCostumerID() == costumerID) return true;
		return false;
	}

	/**
	 * Check if costumer is exist in DB.
	 *
	 * @param costumerId
	 *            The Costumer ID.
	 * @return <code>true</code> if costumer exist and <code>false</code> otherwise.
	 */
	private int checkIfCostumerExist(int costumerId)
	{
		for (Costumer costumer : costumers)
			if (costumer.getId() == costumerId) return costumers.indexOf(costumer);
		return -1;
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

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		compareChart = new BarChart<String, Number>(xAxis, yAxis);

		initializeCompareReportVariables();

		comparePane = new Pane(compareChart);

		Platform.runLater(() -> {
			anchorPane_viewStage.getChildren().add(comparePane);
			AnchorPane.setRightAnchor(((Node) comparePane), 15.0);
			AnchorPane.setTopAnchor(((Node) comparePane), 75.0);
			AnchorPane.setLeftAnchor(((Node) comparePane), 380.0);
			AnchorPane.setBottomAnchor(((Node) comparePane), 35.0);
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
			}
		}
	}

	/**
	 * Check the source and decide which Bar Chart to change.
	 *
	 * @param reportType
	 *            Report type to show.
	 * @param source
	 *            The source who ask for reports. Options : Regular Report or
	 *            Complaint Report.
	 */
	private void reportToShow(String reportType, String source)
	{
		String shop, year, quarter;
		BarChart<String, Number> barToChange;
		Report report;

		if (source.equals("Regular Report")) {
			barChart_currentChart.setLegendVisible(false);
			barToChange = barChart_currentChart;
			shop = comboBox_selectionStore.getValue();
			year = (spinner_selectionYear.getValue()).toString();
			quarter = comboBox_selectionQuarter.getValue();
			switch (reportType) {
				case "Financial Incomes Report":
					report = m_incomesReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				case "Complaints Report":
					report = m_complaintsReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				case "Reservations Report":
					barChart_currentChart.setLegendVisible(true);
					report = m_reservationsReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				default:
					report = m_surveyReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
			}
		} else {
			compareChart.setLegendVisible(false);
			barToChange = compareChart;
			shop = secondReportStore.getValue();
			year = (secondReportYear.getValue()).toString();
			quarter = secondReportQuarter.getValue();
			switch (reportType) {
				case "Financial Incomes Report":
					report = m_compareIncomesReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				case "Complaints Report":
					report = m_compareComplaintsReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				case "Reservations Report":
					barChart_currentChart.setLegendVisible(true);
					report = m_compareReservationsReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
				break;
				default:
					report = m_compareSurveyReport;
					showRequestedReport(reportType, barToChange, shop, year, quarter, report);
			}
		}
	}

	/**
	 * Change Bar Chart values.
	 *
	 * @param reportTypeName
	 *            The report type.
	 * @param barToChange
	 *            The Bar Chart to change.
	 * @param store
	 *            The report store name.
	 * @param year
	 *            The report year.
	 * @param quarter
	 *            The report quarter.
	 * @param reportData
	 *            The report to show.
	 */
	private void showRequestedReport(String reportTypeName, BarChart<String, Number> barToChange, String store,
			String year, String quarter, Report reportData)
	{
		if (reportData == null) return;
		switch (reportTypeName) {
			case "Financial Incomes Report":
				showFinancialIncomesOrComplaintsReport("Financial Incomes", barToChange, store, year, quarter,
						reportData);
			break;
			case "Complaints Report":
				showFinancialIncomesOrComplaintsReport("Complaints", barToChange, store, year, quarter, reportData);
			break;
			case "Reservations Report":
				barToChange.setLegendVisible(true);
				showReservationsReport(barToChange, store, year, quarter, (ReservationsReport) reportData);
			break;
			default:
				showSatisfactionReport(barToChange, store, year, quarter, (SurveysReport) reportData);
		}
	}

	/**
	 * Change Bar Chart accord to report values. Method for Incomes or Complaints
	 * report.
	 *
	 * @param reportTypeName
	 *            The report type.
	 * @param barToChange
	 *            The Bar Chart to change.
	 * @param storeId
	 *            The report store.
	 * @param year
	 *            The report year.
	 * @param quarter
	 *            The report quarter.
	 * @param reportData
	 *            The report.
	 */
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

	/**
	 * Change Bar Chart accord to report values. Method for Reservations report.
	 *
	 * @param reportTypeName
	 *            The report type.
	 * @param barToChange
	 *            The Bar Chart to change.
	 * @param storeId
	 *            The report store.
	 * @param year
	 *            The report year.
	 * @param quarter
	 *            The report quarter.
	 * @param reportData
	 *            The report.
	 */
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

	/**
	 * Change Bar Chart accord to report values. Method for Survey report.
	 *
	 * @param reportTypeName
	 *            The report type.
	 * @param barToChange
	 *            The Bar Chart to change.
	 * @param storeId
	 *            The report store.
	 * @param year
	 *            The report year.
	 * @param quarter
	 *            The report quarter.
	 * @param reportData
	 *            The report.
	 */
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

	/**
	 * Reset the old Bar Chart values and update the Bar Chart title.
	 *
	 * @param barToInit
	 *            The Bar Chart to change.
	 * @param fullNameStore
	 *            The store full name.
	 * @param reportType
	 *            The report type.
	 * @param year
	 *            The report year.
	 * @param quarter
	 *            The report quarter.
	 */
	@SuppressWarnings("rawtypes")
	private void barChartAfterChangeInitialize(BarChart barToInit, String fullNameStore, String reportType, String year,
			String quarter)
	{
		// clear previous chart data and insert new values.
		barToInit.getData().clear();
		String store = fullNameStore.substring(fullNameStore.indexOf("-") + 1, fullNameStore.length());
		barToInit.setTitle(reportType + store + " Report");
		barToInit.getXAxis().setLabel(quarter + " " + year);
		barToInit.getYAxis().setLabel(reportType);
	}

	/**
	 * Initialize {@link ShopCostumerRow} by available {@link ShopCostumer} values.
	 *
	 * @param shopCostumer
	 *            The {@link ShopCostumer}.
	 * @return {@link ShopCostumerRow} with all {@link ShopCostumer} values.
	 */
	private ShopCostumerRow costumerRowInit(ShopCostumer shopCostumer)
	{
		ShopCostumerRow shopCostumerRow;
		if (shopCostumer.getCreditCard() != null && shopCostumer.getCreditCard().equals("null"))
			shopCostumer.setCreditCard("None");
		if (shopCostumer.getCreditCard() == null
				|| shopCostumer.getCostumerSubscription() == EntitiesEnums.CostumerSubscription.None) {
			if (shopCostumer.getCreditCard() == null
					&& shopCostumer.getCostumerSubscription() == EntitiesEnums.CostumerSubscription.None) {
				shopCostumerRow = new ShopCostumerRow(shopCostumer.getCostumerId(),
						shopCostumer.getCostumerSubscription().toString(), shopCostumer.getCumulativePrice());
			} else if (shopCostumer.getCreditCard() == null) {
				shopCostumerRow = new ShopCostumerRow(shopCostumer.getCostumerId(),
						shopCostumer.getCostumerSubscription().toString(), shopCostumer.getSubscriptionStartDate(),
						shopCostumer.getCumulativePrice());
			} else {
				shopCostumerRow = new ShopCostumerRow(shopCostumer.getCostumerId(),
						shopCostumer.getCostumerSubscription().toString(), shopCostumer.getCreditCard(),
						shopCostumer.getCumulativePrice());
			}
		} else {
			shopCostumerRow = new ShopCostumerRow(shopCostumer.getCostumerId(),
					shopCostumer.getCostumerSubscription().toString(), shopCostumer.getSubscriptionStartDate(),
					shopCostumer.getCreditCard(), shopCostumer.getCumulativePrice());
		}
		return shopCostumerRow;
	}

	/**
	 * Activated when {@link ToggleSwitch} changed.
	 *
	 * @param isOn
	 *            <code>true</code> if {@link ToggleSwitch} is on,
	 *            <code>false</code> otherwise.
	 */
	private void reprotStageChange(boolean isOn)
	{
		if (isOn) {
			Stage primaryStage = (Stage) button_submit.getScene().getWindow();
			Platform.runLater(() -> {
				primaryStage.setMinWidth(885);
				primaryStage.setMinHeight(600);
			});
			compareReportsNewStage();
			secondReportQuarter.setVisible(true);
			secondReportStore.setVisible(true);
			secondReportType.setVisible(true);
			secondReportYear.setVisible(true);
			secondSubmitButton.setVisible(true);
		} else {
			secondReportQuarter.setVisible(false);
			secondReportStore.setVisible(false);
			secondReportType.setVisible(false);
			secondReportYear.setVisible(false);
			secondSubmitButton.setVisible(false);
			comparePane.getChildren().clear();
			comparePane = null;
			AnchorPane.setRightAnchor(((Node) pane_dataPane), 10.0);
			AnchorPane.setTopAnchor(((Node) pane_dataPane), 50.0);
			barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
			Platform.runLater(() -> {
				mainStageSizeChanged_StageReorder();
			});
		}
	}

	/**
	 * Get the shop full name and return store ID.
	 *
	 * @param fullNameShop
	 *            The shop full name.
	 * @return Shop ID.
	 */
	private String getShopID(String fullNameShop)
	{
		String shopID = fullNameShop.substring(0, fullNameShop.indexOf("-") - 1);
		return shopID;
	}

	/**
	 * Get the shop full name and return store name.
	 *
	 * @param fullNameShop
	 *            The shop full name.
	 * @return Shop name.
	 */
	private String getShopName(String fullNameShop)
	{
		String shopName = fullNameShop.substring(fullNameShop.indexOf("-") + 1, fullNameShop.length());
		return shopName;
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

	// ----------------------------------------------------------------------------PRIVATE-----------------------------------------------------------

	// -----------------------------------------------------------------------------FXML------------------------------------------------------------

	/**
	 * Activate when one of the "Show Report" pressed.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void showReportButtonPressed(ActionEvent event)
	{
		if (event.getSource().equals(button_submit)) {
			regularReport = true;
			getReportByCurrentValues("Regular Report");
		} else {
			regularReport = false;
			getReportByCurrentValues("Compare Report");
		}
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
	 * Activated when "Show Costumer Details" button pressed.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void showCostumerDetails(ActionEvent event)
	{
		int costumerId, costumerIndexInArray;
		try {
			costumerId = Integer.parseInt(textField_addShopCostumer.getText());
		}
		catch (Exception ex) {
			errorMSG("Invalid ID!");
			textField_addShopCostumer.clear();
			textFiled_costumerDetails.clear();
			return;
		}
		if ((costumerIndexInArray = checkIfCostumerExist(costumerId)) == -1) {
			errorMSG("Costumer doesn't exist in the system.");
			textField_addShopCostumer.clear();
			textFiled_costumerDetails.clear();
			return;
		}
		if (checkIfShopCostumerAlreadyExist(costumerId)) {
			showInformationMessage("Costumer already sign up!");
			textField_addShopCostumer.clear();
			textFiled_costumerDetails.clear();
			return;
		}
		Costumer costumer = costumers.get(costumerIndexInArray);
		textFiled_costumerDetails.setText("ID: " + costumer.getId() + " , UserName:  " + costumer.getUserName());
	}

	/**
	 * Activated when "Add Shop Costumer" button pressed.
	 *
	 * @param event
	 *            Action Event of the button click.
	 */
	@FXML
	private void addShopCostumer(ActionEvent event)
	{
		int costumerId;
		String costumerDetails = textFiled_costumerDetails.getText();
		if (costumerDetails.isEmpty()) {
			errorMSG("Enter Costumer ID!");
			return;
		}
		costumerDetails = costumerDetails.substring(costumerDetails.indexOf(":") + 2, costumerDetails.indexOf(",") - 1);
		costumerId = Integer.parseInt(costumerDetails);

		textFiled_costumerDetails.clear();
		textField_addShopCostumer.clear();
		addNewShopCostumer(costumerId);
	}

	// -----------------------------------------------------------------------------FXML------------------------------------------------------------

	// ----------------------------------------------------------------------------SERVER-COMMUNICATION---------------------------------------------

	/**
	 * Send message to server to get connected shop manager entity.
	 *
	 */
	private void getShopManagerUser()
	{
		ShopManager shopManager = new ShopManager();
		shopManager.setUserName(m_ConnectedUser.getUserName());
		Message entityMessage = MessagesFactory.createGetEntityMessage(shopManager);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Send Get All message to server for all Shop Costumer.
	 *
	 */
	private void getCostumersInShop()
	{
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setCostumerId(0);
		Message msg = MessagesFactory.createGetAllEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

	/**
	 * Send Get All message to server for all Costumer.
	 *
	 */
	private void getAllCostumer()
	{
		Message msg = MessagesFactory.createGetAllEntityMessage(new Costumer());
		m_Client.sendMessageToServer(msg);
	}

	/**
	 * Send get message to server for all exist stores.
	 *
	 */
	private void getStoreIdListFromServer()
	{
		Message entityMessage;
		entityMessage = MessagesFactory.createGetAllEntityMessage(new ShopManager());
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Send Get messages for requested store report.
	 * 
	 * @param store
	 *            The store.
	 * @param quarter
	 *            The report quarter.
	 * @param year
	 *            The report year.
	 */
	private void getStoreReportsFromServer(int store, int quarter, java.util.Date year)
	{
		if (store == 0) return;
		Report report;
		Message entityMessage;

		// Ask from server for IncomesReport
		report = new IncomesReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_Client.sendMessageToServer(entityMessage);

		// Ask from server for ReservationsReport
		report = new ReservationsReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_Client.sendMessageToServer(entityMessage);

		// Ask from server for ComplaintsReport
		report = new ComplaintsReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_Client.sendMessageToServer(entityMessage);

		// Ask from server for SurveysReport
		report = new SurveysReport();
		report.setShopManagerId(store);
		report.setQuarter(quarter);
		report.setYear(year);
		entityMessage = MessagesFactory.createGetEntityMessage(report);
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Send update message to server with new shop costumer subscription.
	 *
	 * @param changedShopCostumer
	 *            The Shop Costumer.
	 * @param newSubscription
	 *            The new subscription.
	 */
	private void saveShopCostumerSubscription(ShopCostumerRow changedShopCostumer, String newSubscription)
	{
		Date currentDate = new Date();
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setCostumerId(changedShopCostumer.getShopCostumerID());
		shopCostumer.setCostumerSubscription(Enum.valueOf(EntitiesEnums.CostumerSubscription.class, newSubscription));
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setSubscriptionStartDate(currentDate);
		if (changedShopCostumer.getCreditCard() != "None")
			shopCostumer.setCreditCard(changedShopCostumer.getCreditCard());
		Message msg = MessagesFactory.createUpdateEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

	/**
	 * Send get message to server with requested report details.
	 *
	 * @param source
	 *            The source who ask for reports. Options : Regular Report or
	 *            Compare Report.
	 */
	private void getReportByCurrentValues(String source)
	{
		int storeID, quarter, yearInt = 2017;
		String store;
		Calendar year = Calendar.getInstance();
		if (source.equals("Regular Report")) {
			store = getShopID(comboBox_selectionStore.getValue());
			storeID = Integer.parseInt(store);
			yearInt = spinner_selectionYear.getValue();
			quarter = quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1;
		} else {
			store = getShopID(secondReportStore.getValue());
			storeID = Integer.parseInt(store);
			yearInt = secondReportYear.getValue();
			quarter = quarters.indexOf(secondReportQuarter.getValue()) + 1;
		}
		year.set(Calendar.YEAR, yearInt);
		getStoreReportsFromServer(storeID, quarter, year.getTime());
	}

	/**
	 * Send add message to server with new Shop Costumer.
	 *
	 * @param costumerId
	 *            The Shop Costumer ID.
	 */
	private void addNewShopCostumer(int costumerId)
	{
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setCostumerId(costumerId);
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setCostumerSubscription(EntitiesEnums.CostumerSubscription.None);
		shopCostumer.setSubscriptionStartDate(new Date());
		Message msg = MessagesFactory.createAddEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

	// ----------------------------------------------------------------------------SERVER-COMMUNICATION---------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		switch (title) {
			case "Reports":
				anchorPane_mainStage.setVisible(true);
				anchorpane_shopCostumerManagement.setVisible(false);
			break;

			case "Costumers\nManagement":
				anchorPane_mainStage.setVisible(false);
				anchorpane_shopCostumerManagement.setVisible(true);
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
		return new String[] { "Reports", "Costumers\nManagement" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (!(messageData instanceof EntityData) && !(messageData instanceof RespondMessageData)
				&& !(messageData instanceof EntitiesListData)) {
			m_Logger.warning("Received message data not of the type requested.");
			return;
		}

		if (messageData instanceof EntityData) {
			IEntity entity = ((EntityData) messageData).getEntity();
			if (entity instanceof IncomesReport || entity instanceof ReservationsReport
					|| entity instanceof ComplaintsReport || entity instanceof SurveysReport) {
				if (firstTime) firstTime = false;
				Report report = (Report) entity;
				Calendar year = Calendar.getInstance();
				year.setTime(report.getYear());
				if (regularReport) {
					if (entity instanceof IncomesReport) m_incomesReport = (IncomesReport) entity;
					else if (entity instanceof ReservationsReport) m_reservationsReport = (ReservationsReport) entity;
					else if (entity instanceof ComplaintsReport) m_complaintsReport = (ComplaintsReport) entity;
					else {
						m_surveyReport = (SurveysReport) entity;
						Platform.runLater(
								() -> reportToShow(comboBox_selectionReportType.getValue(), "Regular Report"));
					}
				} else {
					if (entity instanceof IncomesReport) m_compareIncomesReport = (IncomesReport) entity;
					else if (entity instanceof ReservationsReport)
						m_compareReservationsReport = (ReservationsReport) entity;
					else if (entity instanceof ComplaintsReport) m_compareComplaintsReport = (ComplaintsReport) entity;
					else {
						m_compareSurveyReport = (SurveysReport) entity;
						Platform.runLater(() -> reportToShow(secondReportType.getText(), "Compare Report"));
					}
				}
			} else {
				ShopManager shopManager = (ShopManager) entity;
				m_shopManagerUser = new ShopManager();
				m_shopManagerUser.setUser(shopManager);
				m_shopManagerUser.setId(shopManager.getId());
				m_shopManagerUser.setName(shopManager.getName());
				m_shopManagerUserID = ((ShopManager) entity).getId();
				getCostumersInShop();
				initializeSelection();
			}
		} // End of if (messageData instanceof EntityData)
		else if (messageData instanceof EntitiesListData) {
			List<IEntity> entities = ((EntitiesListData) messageData).getEntities();
			if (!entities.isEmpty()) {
				if (entities.get(0) instanceof ShopManager) {
					String shop;
					stores.clear();
					for (IEntity entity : entities) {
						if (!(entity instanceof ShopManager)) {
							m_Logger.warning("Received entity not of the type requested.");
							return;
						}
						shop = ((ShopManager) entity).getId() + " - ";
						shop = shop + ((ShopManager) entity).getName();
						stores.add(shop);
					}
					initializeSelection();
				} else if (entities.get(0) instanceof ShopCostumer) {
					ShopCostumer shopCostumer;
					ShopCostumerRow shopCostumerRow;
					costumerInShop.clear();
					for (IEntity entity : entities) {
						if (!(entity instanceof ShopCostumer)) {
							m_Logger.warning("Received entity not of the type requested.");
							return;
						}
						shopCostumer = (ShopCostumer) entity;
						shopCostumerRow = costumerRowInit(shopCostumer);
						costumerInShop.add(shopCostumerRow);
					}
					drewContantToTable();
					if (initFlag) {
						initFlag = false;
						initializeShopCostumerConfigurationTable();
					}
				} else if (entities.get(0) instanceof Costumer) {
					for (IEntity entity : entities) {
						if (!(entity instanceof Costumer)) {
							m_Logger.warning("Received entity not of the type requested.");
							return;
						}
						costumers.add((Costumer) entity);
					}
				}
			}
		} // End of else if(messageData instanceof EntitiesListData)
		else if (messageData instanceof RespondMessageData) {
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			boolean succeed = respondMessageData.isSucceed();
			EntityData msgData = (EntityData) respondMessageData.getMessageData();
			IEntity entity = msgData.getEntity();
			if (!succeed) {
				if (entity instanceof IncomesReport || entity instanceof ReservationsReport
						|| entity instanceof ComplaintsReport || entity instanceof SurveysReport) {
					Report report = (Report) entity;
					Calendar year = Calendar.getInstance();
					year.setTime(report.getYear());
					if (regularReport) {
						if (entity instanceof IncomesReport) m_incomesReport = null;
						else if (entity instanceof ReservationsReport) m_reservationsReport = null;
						else if (entity instanceof ComplaintsReport) m_complaintsReport = null;
						else {
							m_surveyReport = null;
							if (m_shopManagerUserID != null) {
								showInformationMessage("No report for current shop.");
							} else {
								String title = barChart_currentChart.getTitle();
								final String shop = getShopName(comboBox_selectionStore.getValue());
								Platform.runLater(() -> {
									barChart_currentChart.getData().clear();
									if (barChart_currentChart.getXAxis().getLabel() != null)
										barChart_currentChart.getXAxis().setLabel(
												comboBox_selectionQuarter.getValue() + " " + year.get(Calendar.YEAR));
									if (title != null) {
										String reportType = comboBox_selectionReportType.getValue();
										reportType = reportType.substring(0, reportType.lastIndexOf(' '));
										barChart_currentChart.setTitle(reportType + shop + " Report");
									}
								});
								if (!firstTime) errorMSG("There are no reports for:\n" + shop + " Store, "
										+ comboBox_selectionQuarter.getValue() + " Quarter in "
										+ spinner_selectionYear.getValue());
								else firstTime = false;
							}
						}
					} else {
						if (entity instanceof IncomesReport) m_compareIncomesReport = null;
						else if (entity instanceof ReservationsReport) m_compareReservationsReport = null;
						else if (entity instanceof ComplaintsReport) m_compareComplaintsReport = null;
						else {
							m_compareSurveyReport = null;
							String title = compareChart.getTitle();
							final String shop = getShopName(secondReportStore.getValue());
							Platform.runLater(() -> {
								compareChart.getData().clear();
								if (compareChart.getXAxis().getLabel() != null) compareChart.getXAxis()
										.setLabel(secondReportQuarter.getValue() + " " + year.get(Calendar.YEAR));
								if (title != null) {
									String reportType = secondReportType.getText();
									reportType = reportType.replaceAll("Report", "");
									compareChart.setTitle(reportType + shop + " Report");
								}
							});
							errorMSG("There are no reports for:\n" + shop + " Store, " + secondReportQuarter.getValue()
									+ " Quarter in " + secondReportYear.getValue());
						}
					}
				}
			} else {
				if (m_shopManagerUserID != null && anchorpane_shopCostumerManagement.isVisible()) {
					if (msgData.getOperation() == EntityDataOperation.Update) {
						showInformationMessage("Successfull Update.");
						updateTable((ShopCostumer) entity);
					} else if (msgData.getOperation() == EntityDataOperation.Add) {
						showInformationMessage("Successfull New Costumer Add.");
						ShopCostumer shopCostumer = (ShopCostumer) entity;
						ShopCostumerRow shopCostumerRow = costumerRowInit(shopCostumer);
						costumerInShop.add(shopCostumerRow);
						drewContantToTable();
					}
				}
			}
		} // End of else if (messageData instanceof EntitiesListData)
	}

	// end region -> BaseController Implementation
}
