
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.UserPrivilege;
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
import messages.Message;

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

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

	/* Selection report type ComboBox data lists declaration */
	ObservableList<String> reportsType = FXCollections.observableArrayList("Financial Incomes Report",
			"Reservations Report", "Complaints Report", "Satisfaction Report");

	ObservableList<String> quarters = FXCollections.observableArrayList("Jan - Mar", "Apr - Jun", "Jul - Sep",
			"Oct - Dec");

	ObservableList<String> stores = FXCollections.observableArrayList("Roladin", "BP", "Amos", "Kinder");
	/* End of --> Selection report type ComboBox data lists declaration */

	/* Month ArrayList */
	final String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	/* End of --> Month ArrayList */

	/* Charts declaration */
	@FXML private BarChart<String, Number> barChart_currentChart;

	@FXML private BarChart<String, Number> compareChart;

	/* UI events region */

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
		initializeSelection();
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

		if (currentQuarter >= 10) currentQuarter = 4;
		else if (currentQuarter >= 7) currentQuarter = 3;
		else if (currentQuarter >= 4) currentQuarter = 2;
		else currentQuarter = 1;

		comboBox_selectionReportType.setItems(reportsType);
		comboBox_selectionReportType.setValue("Financial Incomes Report");
		comboBox_selectionQuarter.setItems(quarters);
		comboBox_selectionQuarter.setValue(quarters.get(currentQuarter - 1));
		comboBox_selectionStore.setItems(stores);
		comboBox_selectionStore.setValue("Roladin");
		textField_selectionYear.setText(Integer.toString(currentDate.get(Calendar.YEAR)));
		if(ApplicationEntryPoint.ConnectedUser.getUserPrivilege() == UserPrivilege.ShopManager)
		{
			comboBox_selectionQuarter.setVisible(false);
			textField_selectionYear.setVisible(false);
			button_compare.setVisible(false);
			comboBox_selectionStore.setVisible(false);
		}
	}

	private void initializeChartPane()
	{
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		barChart_currentChart = new BarChart<String, Number>(xAxis, yAxis);
		barChart_currentChart.setCategoryGap(2.5);
		pane_dataPane.getChildren().add(barChart_currentChart);
		pane_dataPane.heightProperty().addListener(new ChangeListener<Number>() {

			@SuppressWarnings("static-access")
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				if (!(pane_dataPane.getChildren().isEmpty())) {
					if (comparePane == null)
						barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
					else {
						anchorPane_viewStage.setRightAnchor(((Node) pane_dataPane),
								(anchorPane_viewStage.getWidth() - 10) / 2);
						barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
						anchorPane_viewStage.setTopAnchor(((Node) comparePane),
								anchorPane_viewStage.getTopAnchor(pane_dataPane));
						anchorPane_viewStage.setLeftAnchor(((Node) comparePane),
								(anchorPane_viewStage.getWidth() - 10) / 2);
						anchorPane_viewStage.setRightAnchor(((Node) comparePane), 15.0);
						compareChart.setPrefSize(comparePane.getWidth(), comparePane.getHeight());
						button_submit.fire();
						secondSubmitButton.fire();
					}
				}
			}
		});
		pane_dataPane.widthProperty().addListener(new ChangeListener<Number>() {

			@SuppressWarnings("static-access")
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				if (!(pane_dataPane.getChildren().isEmpty())) {
					if (comparePane == null)
						barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
					else {
						anchorPane_viewStage.setRightAnchor(((Node) pane_dataPane),
								(anchorPane_viewStage.getWidth() - 10) / 2);
						barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
						anchorPane_viewStage.setTopAnchor(((Node) comparePane),
								anchorPane_viewStage.getTopAnchor(pane_dataPane));
						anchorPane_viewStage.setLeftAnchor(((Node) comparePane),
								(anchorPane_viewStage.getWidth() - 10) / 2);
						compareChart.setPrefSize(comparePane.getWidth(), comparePane.getHeight());
						button_submit.fire();
						secondSubmitButton.fire();
					}
				}
			}
		});
	}

	private void displayErrorMessage(String errorType)
	{
		Alert errorMessage = new Alert(AlertType.ERROR);
		errorMessage.setTitle("Error Message");
		errorMessage.setContentText(errorType);
		errorMessage.show();
	}

	private int ParseMonthToNum(String monthString)
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

	@SuppressWarnings("static-access")
	private void CompareReportsNewStage()
	{
		if (secondReportQuarter != null) return;
		
		anchorPane_viewStage.setRightAnchor(((Node) pane_dataPane), 380.0);
		anchorPane_viewStage.setTopAnchor(((Node) pane_dataPane), 75.0);

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

		anchorPane_viewStage.getChildren().addAll(secondReportQuarter, secondReportYear, secondReportType,
				secondReportStore, secondSubmitButton);
		anchorPane_viewStage.setTopAnchor(((Node) secondReportQuarter), 50.0);
		anchorPane_viewStage.setTopAnchor(((Node) secondReportYear), 50.0);
		anchorPane_viewStage.setTopAnchor(((Node) secondReportType), 50.0);
		anchorPane_viewStage.setTopAnchor(((Node) secondReportStore), 50.0);
		anchorPane_viewStage.setTopAnchor(((Node) secondSubmitButton), 50.0);

		anchorPane_viewStage.setLeftAnchor(((Node) secondReportQuarter), 12.0);
		anchorPane_viewStage.setLeftAnchor(((Node) secondReportYear), 112.0);
		anchorPane_viewStage.setLeftAnchor(((Node) secondReportType), 187.0);
		anchorPane_viewStage.setLeftAnchor(((Node) secondReportStore), 369.0);
		anchorPane_viewStage.setLeftAnchor(((Node) secondSubmitButton), 466.0);

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		compareChart = new BarChart<String, Number>(xAxis, yAxis);
		compareChart.setCategoryGap(2.5);

		comparePane = new Pane(compareChart);

		anchorPane_viewStage.getChildren().add(comparePane);
		anchorPane_viewStage.setRightAnchor(((Node) comparePane), 15.0);
		anchorPane_viewStage.setTopAnchor(((Node) comparePane), 75.0);
		anchorPane_viewStage.setLeftAnchor(((Node) comparePane), 380.0);
		anchorPane_viewStage.setBottomAnchor(((Node) comparePane), 35.0);

	}

	@FXML
	private void CompareReports(ActionEvent event)
	{
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		CompareReportsNewStage();
	}

	@FXML
	private void ReportTypeChanged(ActionEvent event)
	{
		if (secondReportType == null) return;

		secondReportType.setText(comboBox_selectionReportType.getValue());
	}

	@FXML
	private void ShowSelectionReport(ActionEvent event)
	{
		BarChart<String, Number> barToChange;
		String selectionStore, reportType, quarter;
		int selectionYear;
		try {
			if (event.getSource().equals(button_submit)) {
				barToChange = barChart_currentChart;
				selectionStore = comboBox_selectionStore.getValue();
				selectionYear = Integer.parseInt(textField_selectionYear.getText());
				reportType = comboBox_selectionReportType.getValue();
				quarter = comboBox_selectionQuarter.getValue();
			} else {
				barToChange = compareChart;
				selectionStore = secondReportStore.getValue();
				selectionYear = Integer.parseInt(secondReportYear.getText());
				reportType = secondReportType.getText();
				quarter = secondReportQuarter.getValue();
			}
		}
		catch (NumberFormatException e) {
			displayErrorMessage("The year you entered is invalid!");
			// TODO: log warning
			return;
		}
		catch (Exception e) {
			displayErrorMessage("Faild to read the inputed values");
			// TODO: log warning
			return;
		}

		barToChange.setLegendVisible(false);
		switch (reportType) {
			case "Financial Incomes Report":
				ShowFinancialIncomesOrComplaintsReport("Financial Incomes", barToChange, selectionStore,
						Integer.toString(selectionYear), quarter);
			break;
			case "Complaints Report":
				ShowFinancialIncomesOrComplaintsReport("Complaints", barToChange, selectionStore,
						Integer.toString(selectionYear), quarter);
			break;
			case "Reservations Report":
				barToChange.setLegendVisible(false);
				ShowReservationsReport(barToChange, selectionStore, Integer.toString(selectionYear), quarter);
			break;
			default:
				ShowSatisfactionReport(barToChange, selectionStore, Integer.toString(selectionYear), quarter);
		}
		if(secondSubmitButton != null)
			secondSubmitButton.fire();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ShowFinancialIncomesOrComplaintsReport(String reportTypeName, BarChart barToChange, String storeName,
			String year, String quarter)
	{
		int monthNum = ParseMonthToNum(quarter.substring(0, 3));

		barToChange.getData().clear();
		barToChange.setTitle(storeName + " " + reportTypeName + " Summary ");
		barToChange.getXAxis().setLabel("Month " + year);
		barToChange.getYAxis().setLabel(reportTypeName);

		XYChart.Series barDataSeries = new XYChart.Series();
		XYChart.Data firstMonth = new XYChart.Data(month[monthNum], 10500);
		XYChart.Data secondMonth = new XYChart.Data(month[monthNum + 1], 33211);
		secondMonth.nodeProperty().addListener(new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode)
			{
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;");
				}
			}
		});

		XYChart.Data thirdMonth = new XYChart.Data(month[monthNum + 2], 25900);
		thirdMonth.nodeProperty().addListener(new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode)
			{
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;");
				}
			}
		});

		barDataSeries.getData().add(firstMonth);
		barDataSeries.getData().add(secondMonth);
		barDataSeries.getData().add(thirdMonth);

		barToChange.getData().addAll(barDataSeries);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ShowReservationsReport(BarChart barToChange, String storeName, String year, String quarter)
	{
		int monthNum = ParseMonthToNum(quarter.substring(0, 3));

		barToChange.getData().clear();
		barToChange.setTitle(storeName + " Reservations Summary");
		barToChange.getXAxis().setLabel("Month " + year);
		barToChange.getYAxis().setLabel("Reservations");

		XYChart.Series firstMonth = new XYChart.Series();
		firstMonth.setName("Flower");
		firstMonth.getData().add(new XYChart.Data(month[monthNum], 18));
		firstMonth.getData().add(new XYChart.Data(month[monthNum + 1], 7));
		firstMonth.getData().add(new XYChart.Data(month[monthNum + 2], 3));

		XYChart.Series secondMonth = new XYChart.Series();
		secondMonth.setName("FlowerPot");
		secondMonth.getData().add(new XYChart.Data(month[monthNum], 10));
		secondMonth.getData().add(new XYChart.Data(month[monthNum + 1], 6));
		secondMonth.getData().add(new XYChart.Data(month[monthNum + 2], 15));

		XYChart.Series thirdMonth = new XYChart.Series();
		thirdMonth.setName("BridalBucket");
		thirdMonth.getData().add(new XYChart.Data(month[monthNum], 4));
		thirdMonth.getData().add(new XYChart.Data(month[monthNum + 1], 9));
		thirdMonth.getData().add(new XYChart.Data(month[monthNum + 2], 21));

		barToChange.getData().addAll(firstMonth, secondMonth, thirdMonth);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ShowSatisfactionReport(BarChart barToChange, String storeName, String year, String quarter)
	{

		barToChange.getData().clear();
		barToChange.setTitle(storeName + " Satisfaction Summary");
		barToChange.getXAxis()
				.setLabel(quarter + " " + year);
		barToChange.getYAxis().setLabel("Satisfactions");

		XYChart.Series Question = new XYChart.Series();
		Question.getData().add(new XYChart.Data("Question1", 6.4));
		Question.getData().add(new XYChart.Data("Question2", 5.5));
		Question.getData().add(new XYChart.Data("Question3", 4.2));
		Question.getData().add(new XYChart.Data("Question4", 9.4));
		Question.getData().add(new XYChart.Data("Question5", 4.4));
		Question.getData().add(new XYChart.Data("Question6", 7.4));

		barToChange.getData().addAll(Question);
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// TODO Shimon : Add event handling
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
