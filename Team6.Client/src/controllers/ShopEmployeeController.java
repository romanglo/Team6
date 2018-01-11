
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.Answers;
import entities.IEntity;
import entities.SurveyEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;
import messages.MessagesFactory;


/**
 *
 * CompanyEmployeeController :
 * TODO Naal: Create class description
 * 
 */
public class ShopEmployeeController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private ImageView imageview_subtitle;
	
	@FXML private TextField textfiled_id;
	
	@FXML private TextField textfiled_question1;
	
	@FXML private TextField textfiled_question2;
	
	@FXML private TextField textfiled_question3;
	
	@FXML private TextField textfiled_question4;
	
	@FXML private TextField textfiled_question5;
	
	@FXML private TextField textfiled_question6;
	
	@FXML private ComboBox<String> combobox_answer1;
	
	@FXML private ComboBox<String> combobox_answer2;
	
	@FXML private ComboBox<String> combobox_answer3;
	
	@FXML private ComboBox<String> combobox_answer4;
	
	@FXML private ComboBox<String> combobox_answer5;
	
	@FXML private ComboBox<String> combobox_answer6;
	
	@FXML private Button button_checkifexistenc;
	

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	private ObservableList<String> list;
	
	private SurveyEntity entity;
	/* End of --> Fields region */

	/* UI events region */
	
	@FXML
	public void surveyExistenceCheck(ActionEvent event) throws Exception
	{
		if(textfiled_id.getText().equals(""))
			showInformationMessage("Survey id text filed is empty");
			else
		{
			int id=Integer.parseInt(textfiled_id.getText());
			SurveyEntity entity= new SurveyEntity(id);
			Message msg = MessagesFactory.createGetEntityMessage(entity);
			m_client.sendMessageToServer(msg);
		}
	}
	
	@FXML
	public void addSurvey(ActionEvent event)
	{
		
		if(!checkFileds())
			showInformationMessage("One or more of the fileds are empty");
		else
		{
			if(textfiled_id.isDisable())		// There is existing survey.
			{
				int answer1=Integer.parseInt(combobox_answer1.getValue());
				int answer2=Integer.parseInt(combobox_answer2.getValue());
				int answer3=Integer.parseInt(combobox_answer3.getValue());
				int answer4=Integer.parseInt(combobox_answer4.getValue());
				int answer5=Integer.parseInt(combobox_answer5.getValue());
				int answer6=Integer.parseInt(combobox_answer6.getValue());
				Answers answer_entity= new Answers(answer1, answer2, answer3, answer4, answer5, answer6);
				entity.AddAnswers(answer_entity);
				Message msg= MessagesFactory.createUpdateEntityMessage(entity);
				m_client.sendMessageToServer(msg);
			}
			else								// There isn't existing survey
			{
				int answer1=Integer.parseInt(combobox_answer1.getValue());
				int answer2=Integer.parseInt(combobox_answer2.getValue());
				int answer3=Integer.parseInt(combobox_answer3.getValue());
				int answer4=Integer.parseInt(combobox_answer4.getValue());
				int answer5=Integer.parseInt(combobox_answer5.getValue());
				int answer6=Integer.parseInt(combobox_answer6.getValue());
				String[]questions=new String[6];
				questions[0]=textfiled_question1.getText();
				questions[1]=textfiled_question2.getText();
				questions[2]=textfiled_question3.getText();
				questions[3]=textfiled_question4.getText();
				questions[4]=textfiled_question5.getText();
				questions[5]=textfiled_question6.getText();
				Answers answer_entity= new Answers(answer1, answer2, answer3, answer4, answer5, answer6);
				//entity=new SurveyEntity(questions);
				entity.AddAnswers(answer_entity);
				Message msg =MessagesFactory.createEntityMessage(entity);
				m_client.sendMessageToServer(msg);
			}
		}
	}
	
	@FXML
	public void clearFileds(ActionEvent event)
	{
		button_checkifexistenc.setDisable(false);
		textfiled_id.setText("");
		textfiled_id.setDisable(false);
		textfiled_question1.setText("");
		textfiled_question1.setDisable(false);
		textfiled_question2.setText("");
		textfiled_question2.setDisable(false);
		textfiled_question3.setText("");
		textfiled_question3.setDisable(false);
		textfiled_question4.setText("");
		textfiled_question4.setDisable(false);
		textfiled_question5.setText("");
		textfiled_question5.setDisable(false);
		textfiled_question6.setText("");
		textfiled_question6.setDisable(false);
		combobox_answer1.setValue(null);
		combobox_answer2.setValue(null);
		combobox_answer3.setValue(null);
		combobox_answer4.setValue(null);
		combobox_answer5.setValue(null);
		combobox_answer6.setValue(null);
	}
		
	/* private methods region */
	
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
	
	private boolean checkFileds()
	{
		return ((combobox_answer1.getValue()!=null)&&(combobox_answer2.getValue()!=null)&&(combobox_answer3.getValue()!=null)&&
				(combobox_answer4.getValue()!=null)&&(combobox_answer5.getValue()!=null)&&(combobox_answer6.getValue()!=null)
				&&(!(textfiled_question1.getText().equals("")))&&(!(textfiled_question2.getText().equals("")))&&
				(!(textfiled_question3.getText().equals("")))&&(!(textfiled_question4.getText().equals("")))&&
				(!(textfiled_question5.getText().equals("")))&&(!(textfiled_question6.getText().equals(""))));
	}

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
		answersinitialize();
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
		InputStream costumerSeviceHeadLine = getClass().getResourceAsStream("/boundaries/images/AddNewSurvey.png");
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
	
	private void answersinitialize()
	{
		ArrayList<String> answers = new ArrayList<String>();
		for(int i=1;i<11;i++)
		{
			answers.add(Integer.toString(i));
		}
		 list = FXCollections.observableArrayList(answers);
		 combobox_answer1.setItems(list);
		 combobox_answer2.setItems(list);
		 combobox_answer3.setItems(list);
		 combobox_answer4.setItems(list);
		 combobox_answer5.setItems(list);
		 combobox_answer6.setItems(list);
		
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		if(msg.getMessageData() instanceof IEntity)
		{
			entity= (SurveyEntity)msg.getMessageData();
			// TODO Check The option that survey not exist.
			textfiled_id.setDisable(true);
			button_checkifexistenc.setDisable(true);
			String[] question= entity.getQuestion();
			textfiled_question1.setText(question[1]);
			textfiled_question2.setText(question[2]);
			textfiled_question3.setText(question[3]);
			textfiled_question4.setText(question[4]);
			textfiled_question5.setText(question[5]);
			textfiled_question6.setText(question[6]);
			textfiled_question1.setDisable(true);
			textfiled_question2.setDisable(true);
			textfiled_question3.setDisable(true);
			textfiled_question4.setDisable(true);
			textfiled_question5.setDisable(true);
			textfiled_question6.setDisable(true);
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
