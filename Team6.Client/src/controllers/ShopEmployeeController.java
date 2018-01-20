
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
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
import newEntities.IEntity;
import newEntities.ShopEmployee;
import newEntities.SurveyResult;
import newEntities.Survey;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;


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
	
	@FXML private ComboBox<Integer> combobox_answer1;
	
	@FXML private ComboBox<Integer> combobox_answer2;
	
	@FXML private ComboBox<Integer> combobox_answer3;
	
	@FXML private ComboBox<Integer> combobox_answer4;
	
	@FXML private ComboBox<Integer> combobox_answer5;
	
	@FXML private ComboBox<Integer> combobox_answer6;
	
	@FXML private ComboBox<Integer> combobox_surveyida;
	

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	private ObservableList<Integer> list;
	
	private SurveyResult entity;
	
	private List<IEntity> m_surveys_array;
	
	private ArrayList<Integer> m_surveysid_array= new ArrayList();
;
	
	private int m_shop;
	
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
			Survey entity= new Survey();//id
			Message msg = MessagesFactory.createGetEntityMessage(entity);
			m_client.sendMessageToServer(msg);
		}
	}
	@FXML
	public void selectSurvey(ActionEvent event)
	{	
		int surveyid=combobox_surveyida.getValue();
		for(int i=0;i<m_surveys_array.size();i++)
		{
			if(((SurveyResult)m_surveys_array.get(i)).getId()==surveyid)
			{
				entity=((SurveyResult)m_surveys_array.get(i));
			}
		}
		Survey survey_entity=new Survey();
		survey_entity.setId(entity.getSurveyId());
		Message msg=MessagesFactory.createGetEntityMessage(survey_entity);
		m_client.sendMessageToServer(msg);
	}
	
	@FXML
	public void clear(ActionEvent event)
	{
		
	}
	
	@FXML
	public void addSurvey(ActionEvent event)
	{
		if(checkFileds())
		{
		SurveyResult cur_shopservey=null;
		int surv_id=combobox_surveyida.getValue();
		for(int i=0;i<m_surveys_array.size();i++)
		{
			if(((SurveyResult)m_surveys_array.get(i)).getId()==surv_id)
				cur_shopservey =(SurveyResult)m_surveys_array.get(i);
		}
		cur_shopservey.setAnswer1(combobox_answer1.getValue());
		cur_shopservey.setAnswer2(combobox_answer2.getValue());
		cur_shopservey.setAnswer3(combobox_answer3.getValue());
		cur_shopservey.setAnswer4(ccombobox_answer4.getValue());
		cur_shopservey.setAnswer5(combobox_answer5.getValue());
		cur_shopservey.setAnswer6(combobox_answer6.getValue());
		cur_shopservey.setNumberOfAnswers(cur_shopservey.getNumberOfAnswers()+1);
		Message msg=MessagesFactory.createUpdateEntityMessage(cur_shopservey);
		m_client.sendMessageToServer(msg);
		}
		else
		{
			showInformationMessage("One or more of the fileds is empty");
		}
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
		initializeshop();
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
		ArrayList<Integer> answers = new ArrayList<Integer>();
		for(int i=1;i<11;i++)
		{
			answers.add(i);
		}
		 list = FXCollections.observableArrayList(answers);
		 combobox_answer1.setItems(list);
		 combobox_answer2.setItems(list);
		 combobox_answer3.setItems(list);
		 combobox_answer4.setItems(list);
		 combobox_answer5.setItems(list);
		 combobox_answer6.setItems(list);
	}
	private void initializeSurveys()
	{
		SurveyResult sur_entity= new SurveyResult();
		sur_entity.setShopManagerId(m_shop);
		Message msg=MessagesFactory.createGetAllEntityMessage(sur_entity);
		m_client.sendMessageToServer(msg);
	}
	
	private void initializeshop()
	{
		ShopEmployee shopemp=new ShopEmployee();
		shopemp.setUserName(ApplicationEntryPoint.ConnectedUser.getUserName());
		Message msg=MessagesFactory.createGetEntityMessage(shopemp);
		m_client.sendMessageToServer(msg);
	}

	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		if(msg.getMessageData() instanceof EntityData)
		{
			if(((EntityData)msg.getMessageData()).getEntity() instanceof ShopEmployee)
			{
				ShopEmployee s=(ShopEmployee) ((EntityData)msg.getMessageData()).getEntity();
				m_shop=s.getShopManagerId();
				initializeSurveys();
			}
			else if(((EntityData)msg.getMessageData()).getEntity() instanceof Survey)
				{
					Survey s=(Survey) ((EntityData)msg.getMessageData()).getEntity();
					textfiled_question1.setText(s.getFirstQuestion());
					textfiled_question2.setText(s.getSecondQuestion());
					textfiled_question3.setText(s.getThirdQuestion());
					textfiled_question4.setText(s.getFourthQuestion());
					textfiled_question5.setText(s.getFifthQuestion());
					textfiled_question6.setText(s.getSixthQuestion());
				}
		}
		else
		{
			if (msg.getMessageData() instanceof EntitiesListData)
			{
				EntitiesListData entitiesListData = (EntitiesListData)msg.getMessageData();
				m_surveys_array = entitiesListData.getEntities();
				for(int i=0;i<m_surveys_array.size();i++)
				{
						m_surveysid_array.add(((SurveyResult)m_surveys_array.get(i)).getId());
				}
			list = FXCollections.observableArrayList(m_surveysid_array);
			combobox_surveyida.setItems(list);	
			}
			else if(msg.getMessageData() instanceof RespondMessageData)
			{
				if(((RespondMessageData)msg.getMessageData()).isSucceed())
				{
					m_logger.severe("Update sucssed");
				}
				else
					m_logger.severe("Update faild");
			}
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
