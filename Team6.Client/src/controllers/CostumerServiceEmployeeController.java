
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.Message;


/**
 *
 * CostumerServiceEmployeeController :
 * 		Costumer service employee Controller include:
 * 			Main costumer service employee UI methods.
 * 			Send message to the server methods. 
 * 
 */
public class CostumerServiceEmployeeController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private ImageView imageview_subtitle;
	

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

	/* UI events region */
	
	
	/**
	 * The function opens add costumer complaint window  
	 *
	 * @param event
	 * 			add costumer complaint button clicked.
	 */
	@FXML
	public void addCostumerComplaintClick(ActionEvent event) 
	{
		try {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmplyee_AddCostumerComplaint.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		primaryStage.setScene(scene);		
		primaryStage.show();
		}
		catch(Exception e)
		{
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}
	}

	/**
	 * The function opens add specialist analysis window  
	 *
	 * @param event
	 * 			Add specialist analysis button clicked.
	 */
	@FXML
	public void addNewSurveyClick(ActionEvent event) 
	{
		try {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmployee_AddNewSurvey.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		primaryStage.setScene(scene);		
		primaryStage.show();
		}
		catch(Exception e)
		{
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}
	}
	
	/**
	 * The function opens treatment an open complaint window  
	 *
	 * @param event
	 * 			treatment An Open Complain button clicked.
	 */
	@FXML
	public void treatmentAnOpenComplain(ActionEvent event) 
	{
		try {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmplyee_TreatmentAnOpenComplain.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		primaryStage.setScene(scene);		
		primaryStage.show();
		}
		catch(Exception e)
		{
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}
	}
	
	
	

	
	/* End of --> UI events region */
	
	// region Private Methods
	
	private void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}
	
	/* End of --> Private Methods region */
	

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
		InputStream costumerSeviceHeadLine = getClass().getResourceAsStream("/boundaries/images/CostumerServiceEmployeeHeadLine.png");
		if (serverGif != null) 
		{
			Image image = new Image(costumerSeviceHeadLine);
			imageview_subtitle.setImage(image);
		}
	}
	
	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
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
		//showInformationMessage("You have disconected from server please wait");
		
	}

	
	
	
	
	
}
