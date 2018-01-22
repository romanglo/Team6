
package newControllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import boundaries.ShopCostumerRow;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import newEntities.ComplaintsReport;
import newEntities.Costumer;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.IncomesReport;
import newEntities.Report;
import newEntities.ReservationsReport;
import newEntities.ShopCostumer;
import newEntities.ShopManager;
import newEntities.SurveysReport;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.EntityDataOperation;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;


/**
 *
 * ExampleController: TODO Yoni
 * 
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
	private ArrayList<Integer> costumers = new ArrayList<>();
	// End of --> Registered Company Costumers
	
	// Registered Shop Costumers
	ObservableList<ShopCostumerRow> costumerInShop = FXCollections.observableArrayList();
	// End of --> Registered Shop Costumers
	
	/* End of -> Shop Manager - Add Costumer To Shop Stage Fields */
	
	 @FXML private ToggleButton toggleButton_compareReport;

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		if (m_ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
			initializeShopManagerUser();
			getAllCostumer();
		} else {
			vbox_sidebar.getChildren().get(1).setVisible(false);
			getStoreIdListFromServer();
		}
		initializeChartPane();
	}

	private void initializeShopManagerUser()
	{
		ShopManager shopManager = new ShopManager();
		shopManager.setUserName(m_ConnectedUser.getUserName());
		Message entityMessage = MessagesFactory.createGetEntityMessage(shopManager);
		m_Client.sendMessageToServer(entityMessage);
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
			year--;
		}

		spinnerValueFactory_selectionYear = new SpinnerValueFactory.IntegerSpinnerValueFactory(2010, year);
		spinner_selectionYear.setValueFactory(spinnerValueFactory_selectionYear);
		spinner_selectionYear.getValueFactory().setValue(year);
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
			comboBox_selectionStore.setValue(m_shopManagerUserID + " - " + m_shopManagerUser.getName());
		});

		if (m_ConnectedUser.getPrivilege() == EntitiesEnums.UserPrivilege.ShopManager) {
			Platform.runLater(() -> {
				comboBox_selectionQuarter.setVisible(false);
				spinner_selectionYear.setVisible(false);
				toggleButton_compareReport.setVisible(false);
				comboBox_selectionStore.setVisible(false);
			});
		}

		initializeStoreReportVariables();
	}

	private void initializeStoreReportVariables()
	{
		String store = getShopID(comboBox_selectionStore.getValue());
		int storeId = Integer.parseInt(store);
		int quarter = quarters.indexOf(comboBox_selectionQuarter.getValue()) + 1;
		int yearInt = spinner_selectionYear.getValue();
		Calendar year = Calendar.getInstance();
		year.set(Calendar.YEAR, yearInt);
		getStoreReportsFromServer(storeId, quarter, year.getTime());
	}

	@FXML
	private void initializeCompareReportVariables()
	{
		Calendar year = Calendar.getInstance();
		secondReportQuarter = new ComboBox<>(quarters);
		secondReportQuarter.setValue(comboBox_selectionQuarter.getValue());
		secondReportQuarter.setPrefWidth(93);
		secondReportYear = new Spinner<>();
		secondReportYear.setPrefWidth(69);
		spinnerValueFactory_secondSelectionYear = new SpinnerValueFactory.IntegerSpinnerValueFactory(2010,
				year.get(Calendar.YEAR));
		secondReportYear.setValueFactory(spinnerValueFactory_secondSelectionYear);
		secondReportYear.getValueFactory().setValue(spinner_selectionYear.getValue());
		secondReportType = new TextField();
		secondReportType.setPrefWidth(80);
		secondReportType.setText(comboBox_selectionReportType.getValue());
		secondReportType.setDisable(true);
		secondReportType.setPrefWidth(175);
		secondReportStore = new ComboBox<>(stores);
		secondReportStore.setValue(comboBox_selectionStore.getValue());
		secondReportStore.setPrefWidth(120);
		if (barChart_currentChart.getTitle() != null) {
			String reportType = secondReportType.getText();
			reportType = reportType.replaceAll("Report", "");
			barChartAfterChangeInitialize(compareChart, secondReportStore.getValue(), reportType,
					secondReportYear.getValue().toString(), secondReportQuarter.getValue());
		}

		secondSubmitButton = new Button("Show Report");
		secondSubmitButton.setPrefWidth(100);
		secondSubmitButton.setPrefHeight(25);
		secondSubmitButton.setOnAction(button_submit.getOnAction());
		// yearChangeListener = (obs, oldText, newText) ->
		// selectionYearChanged(secondReportYear, (String) oldText);
		// secondReportYear.textProperty().addListener(yearChangeListener);
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

	@FXML
	private void initializeChartPane()
	{
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		barChart_currentChart = new BarChart<String, Number>(xAxis, yAxis);
		Platform.runLater(() -> {
			pane_dataPane.getChildren().add(barChart_currentChart);
		});
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
		Platform.runLater(() -> {
			mainStageSizeChanged_StageReorder();
		});
	}

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
					List<String> choices = new ArrayList<>();
					choices.add("Monthly");
					choices.add("YearLy");

					ChoiceDialog<String> dialog = new ChoiceDialog<>("None", choices);
					dialog.setTitle("Edit Costumer Subscription");
					dialog.setHeaderText("Change Subscription To Costumer ID -> " + rowData.getID());
					dialog.setContentText("Choose costumer new subscription:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						if (result.get().equals("None")) return;
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

	private void saveShopCostumerSubscription(ShopCostumerRow changedShopCostumer, String newSubscription)
	{
		Date currentDate = new Date();
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setCostumerId(changedShopCostumer.getShopCostumerID());
		shopCostumer.setCostumerSubscription(Enum.valueOf(EntitiesEnums.CostumerSubscription.class, newSubscription));
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setSubscriptionStartDate(currentDate);
		Message msg = MessagesFactory.createUpdateEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

	private void updateTable(ShopCostumer updatedShopCostumer)
	{
		for (ShopCostumerRow rowData : costumerInShop)
			if (rowData.getID() == updatedShopCostumer.getCostumerId()) {
				rowData.setShopCostumerSubscription(updatedShopCostumer.getCostumerSubscription().toString());
				rowData.setSubscriptionStartDate(updatedShopCostumer.getSubscriptionStartDate());
			}
		drewContantToTable();
	}

	private boolean checkIfShopCostumerAlreadyExist(int costumerID)
	{
		for (ShopCostumerRow rowData : costumerInShop)
			if (rowData.getID() == costumerID) return true;
		return false;
	}

	private void drewContantToTable()
	{
		tableView_shopCostumer.setItems(costumerInShop);
		tableView_shopCostumer.refresh();
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
		});
		Platform.runLater(() -> {
			AnchorPane.setRightAnchor(((Node) comparePane), 15.0);
			AnchorPane.setTopAnchor(((Node) comparePane), 75.0);
			AnchorPane.setLeftAnchor(((Node) comparePane), 380.0);
			AnchorPane.setBottomAnchor(((Node) comparePane), 35.0);
		});
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
		if (storeID == 0) {
			if (m_shopManagerUserID != null) storeID = m_shopManagerUserID;
			else return;
		}
		getStoreReportsFromServer(storeID, quarter, year.getTime());
	}

	@FXML
	private void showSelectionReport(ActionEvent event)
	{
		if (event.getSource().equals(button_submit)) regularReport = true;
		else regularReport = false;
		if (event.getSource().equals(button_submit)) getReportByCurrentValues("Regular Report");
		else getReportByCurrentValues("Compare Report");
	}

	private void showSelectionReports(String report, String source)
	{
		String shop, year, quarter;
		switch (source) {
			case "Regular Report":
				barChart_currentChart.setLegendVisible(false);
				shop = comboBox_selectionStore.getValue();
				year = (spinner_selectionYear.getValue()).toString();
				quarter = comboBox_selectionQuarter.getValue();
				switch (report) {
					case "Financial Incomes Report":
						showFinancialIncomesOrComplaintsReport("Financial Incomes", barChart_currentChart, shop, year,
								quarter, m_incomesReport);
					break;
					case "Complaints Report":
						showFinancialIncomesOrComplaintsReport("Complaints", barChart_currentChart, shop, year, quarter,
								m_complaintsReport);
					break;
					case "Reservations Report":
						barChart_currentChart.setLegendVisible(true);
						showReservationsReport(barChart_currentChart, shop, year, quarter, m_reservationsReport);
					break;
					default:
						showSatisfactionReport(barChart_currentChart, shop, year, quarter, m_surveyReport);
				}
			break;
			case "Compare Report":
				compareChart.setLegendVisible(false);
				shop = secondReportStore.getValue();
				year = (secondReportYear.getValue()).toString();
				quarter = secondReportQuarter.getValue();
				switch (report) {
					case "Financial Incomes Report":
						showFinancialIncomesOrComplaintsReport("Financial Incomes", compareChart, shop, year, quarter,
								m_compareIncomesReport);
					break;
					case "Complaints Report":
						showFinancialIncomesOrComplaintsReport("Complaints", compareChart, shop, year, quarter,
								m_compareComplaintsReport);
					break;
					case "Reservations Report":
						compareChart.setLegendVisible(true);
						showReservationsReport(compareChart, shop, year, quarter, m_compareReservationsReport);
					break;
					default:
						showSatisfactionReport(compareChart, shop, year, quarter, m_compareSurveyReport);
				}
		}
	}

	private String getShopID(String fullNameShop)
	{
		String shopID = fullNameShop.substring(0, fullNameShop.indexOf("-") - 1);
		return shopID;
	}

	private String getShopName(String fullNameShop)
	{
		String shopName = fullNameShop.substring(fullNameShop.indexOf("-") + 1, fullNameShop.length());
		return shopName;
	}

	@FXML
	private void addShopCostumer(ActionEvent event)
	{
		int costumerId;
		try {
			costumerId = Integer.parseInt(textField_addShopCostumer.getText());
		}
		catch (Exception ex) {
			errorMSG("Invalid ID!");
			return;
		}
		if (!costumers.contains(costumerId)) {
			errorMSG("Costumer doesn't exist in the system.");
			return;
		}
		if (checkIfShopCostumerAlreadyExist(costumerId)) {
			errorMSG("Costumer already sign up!");
			return;
		}
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setCostumerId(costumerId);
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setCostumerSubscription(EntitiesEnums.CostumerSubscription.None);
		shopCostumer.setSubscriptionStartDate(new Date());
		Message msg = MessagesFactory.createAddEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

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

	/* region Private Methods */

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
	private void barChartAfterChangeInitialize(BarChart barToInit, String fullNameStore, String reportType, String year,
			String quarter)
	{
		// clear previous chart data and insert new values.
		barToInit.getData().clear();
		String store = fullNameStore.substring(fullNameStore.indexOf("-") + 1, fullNameStore.length());
		barToInit.setTitle(reportType + store + " Summary");
		barToInit.getXAxis().setLabel(quarter + " " + year);
		barToInit.getYAxis().setLabel(reportType);
	}

	/* end region -> Private Methods */

	/* Sending message to server methods region */

	private void getStoreIdListFromServer()
	{
		Message entityMessage;
		entityMessage = MessagesFactory.createGetAllEntityMessage(new ShopManager());
		m_Client.sendMessageToServer(entityMessage);
	}

	/**
	 * Activate when Store ComboBox value changed. Send Get messages for all store
	 * report.
	 * 
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

	private void getCostumersInShop()
	{
		ShopCostumer shopCostumer = new ShopCostumer();
		shopCostumer.setShopManagerId(m_shopManagerUserID);
		shopCostumer.setCostumerId(0);
		Message msg = MessagesFactory.createGetAllEntityMessage(shopCostumer);
		m_Client.sendMessageToServer(msg);
	}

	private void getAllCostumer()
	{
		Message msg = MessagesFactory.createGetAllEntityMessage(new Costumer());
		m_Client.sendMessageToServer(msg);
	}

	/* End of -> Sending message to server methods region */

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
								() -> showSelectionReports(comboBox_selectionReportType.getValue(), "Regular Report"));
					}
				} else {
					if (entity instanceof IncomesReport) m_compareIncomesReport = (IncomesReport) entity;
					else if (entity instanceof ReservationsReport)
						m_compareReservationsReport = (ReservationsReport) entity;
					else if (entity instanceof ComplaintsReport) m_compareComplaintsReport = (ComplaintsReport) entity;
					else {
						m_compareSurveyReport = (SurveysReport) entity;
						Platform.runLater(() -> showSelectionReports(secondReportType.getText(), "Compare Report"));
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
					Costumer costumer;
					for (IEntity entity : entities) {
						if (!(entity instanceof Costumer)) {
							m_Logger.warning("Received entity not of the type requested.");
							return;
						}
						costumer = (Costumer) entity;
						costumers.add(costumer.getId());
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
										barChart_currentChart.setTitle(reportType + shop + " Summary");
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
									compareChart.setTitle(reportType + shop + " Summary");
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
	
	@FXML
	private void reprotStageChange(ActionEvent event)
	{
		if(toggleButton_compareReport.isSelected())
		{
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
}
