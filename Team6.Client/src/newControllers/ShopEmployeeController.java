
package newControllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import newEntities.IEntity;
import newEntities.ShopEmployee;
import newEntities.Survey;
import newEntities.SurveyResult;
import newMessages.EntitiesListData;
import newMessages.EntityData;
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
public class ShopEmployeeController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	
	@FXML private TextField textfiled_id;
	
	@FXML private TextField textfiled_question1;
	
	@FXML private TextField textfiled_question2;
	
	@FXML private TextField textfiled_question3;
	
	@FXML private TextField textfiled_question4;
	
	@FXML private TextField textfiled_question5;
	
	@FXML private TextField textfiled_question6;
	
	@FXML private Spinner<Integer> combobox_answer1;
	
	@FXML private Spinner<Integer> combobox_answer2;
	
	@FXML private Spinner<Integer> combobox_answer3;
	
	@FXML private Spinner<Integer> combobox_answer4;
	
	@FXML private Spinner<Integer> combobox_answer5;
	
	@FXML private Spinner<Integer> combobox_answer6;
	
	private int manager_id;
	
	private Survey correct_survey;
	
	private String[] questions= { "Question1" , "Question2", "Question3" , "Question4" , "Question5" , "Question6" };
	
	private SpinnerValueFactory svf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
	
	private SpinnerValueFactory svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
	
	private SpinnerValueFactory svf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
	
	private SpinnerValueFactory svf4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
	
	private SpinnerValueFactory svf5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
	
	private SpinnerValueFactory svf6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		switch (title) {
			case "Add survey":
				anchorpane_option1.setVisible(true);
				initializesurveys();
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
		return new String[] { "Add survey" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if(messageData instanceof EntityData)
		{
			if(((EntityData) messageData).getEntity() instanceof ShopEmployee)
			{
				manager_id= ((ShopEmployee)((EntityData) messageData).getEntity()).getShopManagerId();
				Survey survey_entity=new Survey();
				msg=MessagesFactory.createGetAllEntityMessage(survey_entity);
				m_Client.sendMessageToServer(msg);
			}
		}
		else if(messageData instanceof EntitiesListData)
			{
				if(((EntitiesListData) messageData).getEntities().get(0) instanceof Survey)
				{
					List<IEntity> temp= ((EntitiesListData) messageData).getEntities();
					for(int i=0;i<temp.size();i++)
					{
						if(((Survey) temp.get(i)).getManagerId()==manager_id)
						{
							if(((Survey) temp.get(i)).getEndDate().after(new Date()))
							{
								correct_survey= (Survey) temp.get(i);
							}
						}

					}
					if(correct_survey==null)
					{
						showInformationMessage("There no surveys for your shop.");
						anchorpane_option1.setVisible(false);
					}
					else
					{
						textfiled_question1.setText(questions[0]);
						textfiled_question2.setText(questions[1]);
						textfiled_question3.setText(questions[2]);
						textfiled_question4.setText(questions[3]);
						textfiled_question5.setText(questions[4]);
						textfiled_question6.setText(questions[5]);
					}
				}
			}
		else if(messageData instanceof RespondMessageData)
		{
			if(((RespondMessageData) messageData).getMessageData() instanceof EntityData)
			{
				if(((EntityData)((RespondMessageData) messageData).getMessageData()).getEntity() instanceof SurveyResult)
				{
				if(((RespondMessageData) messageData).isSucceed())
				{
					m_Logger.severe("Survey added succssefuly");
					javafx.application.Platform.runLater(()-> {	
						 combobox_answer1.getValueFactory().setValue(0);
						 combobox_answer2.getValueFactory().setValue(0);
						 combobox_answer3.getValueFactory().setValue(0);
						 combobox_answer4.getValueFactory().setValue(0);
						 combobox_answer5.getValueFactory().setValue(0);
						 combobox_answer6.getValueFactory().setValue(0);
						 combobox_answer1.setValueFactory(svf1);
						 combobox_answer2.setValueFactory(svf2);
						 combobox_answer3.setValueFactory(svf3);
						 combobox_answer4.setValueFactory(svf4);
						 combobox_answer5.setValueFactory(svf5);
						 combobox_answer6.setValueFactory(svf6);
						 showInformationMessage("Successfully added");
					});

				}
				}
			}
		}
	}

	// end region -> BaseController Implementation
	
	// UI event region
	
	private void initializesurveys()
	{
		String username=m_ConnectedUser.getUserName();
		ShopEmployee shopemployee_entity= new ShopEmployee();
		shopemployee_entity.setUserName(username);
		Message msg=MessagesFactory.createGetEntityMessage(shopemployee_entity);
		m_Client.sendMessageToServer(msg);
		ArrayList<Integer> answers = new ArrayList<Integer>();
		for(int i=1;i<11;i++)
		{
			answers.add(i);
		}
		ObservableList<Integer> list = FXCollections.observableArrayList(answers);
		 combobox_answer1.setValueFactory(svf1);
		 combobox_answer2.setValueFactory(svf2);
		 combobox_answer3.setValueFactory(svf3);
		 combobox_answer4.setValueFactory(svf4);
		 combobox_answer5.setValueFactory(svf5);
		 combobox_answer6.setValueFactory(svf6);
	}
	
	@FXML
	public void addSurvey(ActionEvent event)
	{
		if((combobox_answer1.getValue()==0)||(combobox_answer2.getValue()==0)||(combobox_answer3.getValue()==0)||
				(combobox_answer4.getValue()==0)||(combobox_answer5.getValue()==0)||(combobox_answer6.getValue()==0))
		{
			showInformationMessage("One or more of the answers are 0 its needs to be at least 1.");
		}
		else
		{
		SurveyResult surveyResult= new SurveyResult();
		surveyResult.setFirstAnswer(combobox_answer1.getValue());
		surveyResult.setSecondAnswer(combobox_answer2.getValue());
		surveyResult.setThirdAnswer(combobox_answer3.getValue());
		surveyResult.setFourthanswer(combobox_answer4.getValue());
		surveyResult.setFifthAnswer(combobox_answer5.getValue());
		surveyResult.setSixthAnswer(combobox_answer6.getValue());
		surveyResult.setSurveyId(correct_survey.getId());
		surveyResult.setEnterDate(new Date());
		Message msg=MessagesFactory.createAddEntityMessage(surveyResult);
		m_Client.sendMessageToServer(msg);
		}
	}
	
	
	
	
	// end region -> BUI event region
}
