
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logger.LogManager;
import newEntities.Survey;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;


/**
 *
 * CostumerServiceEmployee_AddNewSurvey :
 * TODO Dolev: Create class description
 * 
 */
public class CostumerServiceEmployee_AddNewSurvey implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private ImageView imageview_headline;
	
	@FXML private TextField textfield_question1;
	
	@FXML private TextField textfield_question2;
	
	@FXML private TextField textfield_question3;
	
	@FXML private TextField textfield_question4;
	
	@FXML private TextField textfield_question5;
	
	@FXML private TextField textfield_question6;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	/* End of --> Fields region */

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
		InputStream headline = getClass().getResourceAsStream("/boundaries/images/AddNewSurvey.png");
		if (headline != null) {
			Image image = new Image(headline);
			imageview_headline.setImage(image);
		}
	}
	
	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
	}

	/* End of --> Initializing methods region */
	
	/* UI events region */
	
	@FXML
	private void addNewSurvey(ActionEvent event)
	{
		if(!CheckFields())
			showInformationMessage("One or more of the fields are empty.");
		else
		{
		Survey survey_entity= new Survey();
		survey_entity.setFirstQuestion(textfield_question1.getText());
		survey_entity.setSecondQuestion(textfield_question2.getText());
		survey_entity.setThirdQuestion(textfield_question3.getText());
		survey_entity.setFourthQuestion(textfield_question4.getText());
		survey_entity.setFifthQuestion(textfield_question5.getText());
		survey_entity.setSixthQuestion(textfield_question6.getText());
		Message msg=MessagesFactory.createAddEntityMessage(survey_entity);
		m_client.sendMessageToServer(msg);
		}
	}
	
	/**
	 * The function back to costumer service employee main window.
	 *
	 * @param event
	 * 			Back button clicked
	 */
	@FXML
	public void backButtonClick(ActionEvent event) 
	{
		try 
		{
		((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmployee.fxml"));
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

	/* Private methods region */
	
	private boolean CheckFields()
	{
		if((textfield_question1.getText().equals(""))||(textfield_question2.getText().equals(""))||
				(textfield_question3.getText().equals(""))||(textfield_question4.getText().equals(""))||
				(textfield_question5.getText().equals(""))||(textfield_question6.getText().equals("")))
				{
					return false;
				}
		return true;
	}
	
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
	
	/* End of --> Private methods */
	
	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
			IMessageData messageData = msg.getMessageData();
			if(messageData instanceof RespondMessageData)
			{
				if(((RespondMessageData) messageData).isSucceed())
					m_logger.severe("New survey added to the system");
			}
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
