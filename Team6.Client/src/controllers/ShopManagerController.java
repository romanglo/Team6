
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
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
import javafx.scene.layout.Pane;
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
	
	/*Data AnchorPane declaration*/
	@FXML private Pane pane_dataPane;
	/* End of --> Data AnchorPane declaration*/

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	/* End of --> UI Binding Fields region */

	/* Selection area declaration */
	@FXML private ComboBox<String> comboBox_selectionReportType;

	@FXML private ComboBox<String> comboBox_selectionQuarter;

	@FXML private TextField textField_selectionYear;

	@FXML private Button button_submit;
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
	/* End of --> Selection report type ComboBox data lists declaration */

	/* Month ArrayList */
	final String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	/* End of --> Month ArrayList */

	/* Charts declaration */
	@FXML private BarChart<String, Number> barChart_currentChart;

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
		comboBox_selectionReportType.setItems(reportsType);
		comboBox_selectionReportType.setValue("Financial Incomes Report");
		comboBox_selectionQuarter.setItems(quarters);
		comboBox_selectionQuarter.setValue("Jan - Mar");
		textField_selectionYear.setText("2018");
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
				if(!(pane_dataPane.getChildren().isEmpty()))
					barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
			}	
		});
		pane_dataPane.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				if(!(pane_dataPane.getChildren().isEmpty()))
					barChart_currentChart.setPrefSize(pane_dataPane.getWidth(), pane_dataPane.getHeight());
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
	

	@FXML
	private void ShowSelectionReport(ActionEvent event)
	{
		String reportType, quarter;
		int year;
		try {
			reportType = comboBox_selectionReportType.getValue();
			quarter = comboBox_selectionQuarter.getValue();
			year = Integer.parseInt(textField_selectionYear.getText());
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
		

		barChart_currentChart.setLegendVisible(false);
		switch (reportType) {
			case "Financial Incomes Report":
				ShowFinancialIncomesOrComplaintsReport("Financial Incomes");
			break;
			case "Complaints Report":
				ShowFinancialIncomesOrComplaintsReport("Complaints");
			break;
			case "Reservations Report":
				barChart_currentChart.setLegendVisible(true);
				ShowReservationsReport();
			break;
			default:
				ShowSatisfactionReport();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) private void ShowFinancialIncomesOrComplaintsReport(String reportTypeName)
	{
		int monthNum = ParseMonthToNum(comboBox_selectionQuarter.getValue().substring(0, 3));
		
		barChart_currentChart.getData().clear();
		barChart_currentChart.setTitle(reportTypeName + " Summary");
		barChart_currentChart.getXAxis().setLabel("Month " + textField_selectionYear.getText());
		barChart_currentChart.getYAxis().setLabel(reportTypeName);

		XYChart.Series barDataSeries = new XYChart.Series();
		XYChart.Data firstMonth = new XYChart.Data(month[monthNum], 10500);
		
		XYChart.Data secondMonth = new XYChart.Data(month[monthNum + 1], 33211);
		secondMonth.nodeProperty().addListener(new ChangeListener<Node>() {
			  @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
			    if (newNode != null) {
			      newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;"); 
			    }
			  }
			});
		
		XYChart.Data thirdMonth = new XYChart.Data(month[monthNum + 2], 25900);
		thirdMonth.nodeProperty().addListener(new ChangeListener<Node>() {
			  @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
			    if (newNode != null) {
			      newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;"); 
			    }
			  }
			});
		
		barDataSeries.getData().add(firstMonth);
		barDataSeries.getData().add(secondMonth);
		barDataSeries.getData().add(thirdMonth);
		
		
		barChart_currentChart.getData().addAll(barDataSeries);
	}



	@SuppressWarnings({ "rawtypes", "unchecked" }) private void ShowReservationsReport()
	{
		int monthNum = ParseMonthToNum(comboBox_selectionQuarter.getValue().substring(0, 3));
		
		barChart_currentChart.getData().clear();
		barChart_currentChart.setTitle("Reservations Summary");
		barChart_currentChart.getXAxis().setLabel("Month " + textField_selectionYear.getText());
		barChart_currentChart.getYAxis().setLabel("Reservations");
		
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

		barChart_currentChart.getData().addAll(firstMonth, secondMonth, thirdMonth);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" }) private void ShowSatisfactionReport()
	{
		
		barChart_currentChart.getData().clear();
		barChart_currentChart.setTitle("Satisfaction Summary");
		barChart_currentChart.getXAxis().setLabel(comboBox_selectionQuarter.getValue() + " " + textField_selectionYear.getText());
		barChart_currentChart.getYAxis().setLabel("Satisfactions");

		XYChart.Series Question = new XYChart.Series();
		Question.getData().add(new XYChart.Data("Question1", 6.4));
		Question.getData().add(new XYChart.Data("Question2", 5.5));
		Question.getData().add(new XYChart.Data("Question3", 4.2));
		Question.getData().add(new XYChart.Data("Question4", 9.4));
		Question.getData().add(new XYChart.Data("Question5", 4.4));
		Question.getData().add(new XYChart.Data("Question6", 7.4));
		
		barChart_currentChart.getData().addAll(Question);
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
