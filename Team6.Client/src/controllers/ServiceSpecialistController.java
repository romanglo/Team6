
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logger.LogManager;
import newEntities.IEntity;
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
 * ServiceSpecialistController : ServiceSpecialist can enter summery and update
 * the survey.
 * 
 */
public class ServiceSpecialistController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private ComboBox<String> combobox_id;

	@FXML private TextField textfield_question1;

	@FXML private TextField textfield_question2;

	@FXML private TextField textfield_question3;

	@FXML private TextField textfield_question4;

	@FXML private TextField textfield_question5;

	@FXML private TextField textfield_question6;

	@FXML private TextField textfield_answer1;

	@FXML private TextField textfield_answer2;

	@FXML private TextField textfield_answer3;

	@FXML private TextField textfield_answer4;

	@FXML private TextField textfield_answer5;

	@FXML private TextField textfield_answer6;

	@FXML private TextArea textarea_analysis;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

	private ObservableList<String> m_list;

	private List<IEntity> m_survey_array;

	private ArrayList<String> m_surveysid_array = new ArrayList<String>();

	private SurveyResult selected_survey;

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
		initializesurveys();
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

	private void initializesurveys()
	{
		combobox_id.setValue("");
		SurveyResult shopsurvey_entity = new SurveyResult();
		Message msg = MessagesFactory.createGetAllEntityMessage(shopsurvey_entity);
		m_client.sendMessageToServer(msg);
	}

	/* End of --> Initializing methods region */

	/* UI events region */
	/**
	 * Adds questions and answers in the right place when choosing a survey.
	 *
	 * @param event
	 *            Combo box clicked.
	 */
	@FXML
	public void selectSurvey(ActionEvent event)
	{
		if ((combobox_id.getValue() == null) || (combobox_id.getValue().equals(""))) return;
		int id = Integer.parseInt(combobox_id.getValue());
		for (int i = 0; i < m_survey_array.size(); i++) {
			if (id == ((SurveyResult) m_survey_array.get(i)).getId())
				selected_survey = (SurveyResult) m_survey_array.get(i);
		}
		textfield_answer1.setText(Integer.toString(selected_survey.getAnswer1()));
		textfield_answer2.setText(Integer.toString(selected_survey.getAnswer2()));
		textfield_answer3.setText(Integer.toString(selected_survey.getAnswer3()));
		textfield_answer4.setText(Integer.toString(selected_survey.getAnswer4()));
		textfield_answer5.setText(Integer.toString(selected_survey.getAnswer5()));
		textfield_answer6.setText(Integer.toString(selected_survey.getAnswer6()));
		Survey survey_entity = new Survey();
		survey_entity.setId(selected_survey.getSurveyId());
		Message msg = MessagesFactory.createGetEntityMessage(survey_entity);
		m_client.sendMessageToServer(msg);
	}

	/**
	 * Updating and closing the survey.
	 *
	 * @param event
	 *            save button clicked.
	 */
	@FXML
	public void saveAnalysis(ActionEvent event)
	{
		if ((textarea_analysis.getText().equals("")) || (combobox_id.getValue().equals(""))) {
			showInformationMessage("Specialist analisys area Or survey id is empty");
		} else {
			selected_survey.setSummary(textarea_analysis.getText());
			selected_survey.setClosed(true);
			Message msg = MessagesFactory.createUpdateEntityMessage(selected_survey);
			m_client.sendMessageToServer(msg);
			combobox_id.setDisable(true);
			combobox_id.getItems().clear();
			m_survey_array.clear();
			m_surveysid_array.clear();
			initializesurveys();
			combobox_id.setDisable(false);
			textarea_analysis.clear();
			textfield_answer1.clear();
			textfield_answer2.clear();
			textfield_answer3.clear();
			textfield_answer4.clear();
			textfield_answer5.clear();
			textfield_answer6.clear();
			textfield_question1.clear();
			textfield_question2.clear();
			textfield_question3.clear();
			textfield_question4.clear();
			textfield_question5.clear();
			textfield_question6.clear();
		}
	}

	/* End of --> UI events region */

	/* Private methods region */

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
	/* End of --> Private methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof EntitiesListData) {
			if (((EntitiesListData) messageData).getEntities().get(0) instanceof SurveyResult) {
				m_survey_array = ((EntitiesListData) messageData).getEntities();
				for (int i = 0; i < m_survey_array.size(); i++) {
					if (!(((SurveyResult) m_survey_array.get(i)).isClosed()))
						m_surveysid_array.add(Integer.toString(((SurveyResult) m_survey_array.get(i)).getId()));
				}
				m_list = FXCollections.observableArrayList(m_surveysid_array);
				combobox_id.setItems(m_list);
			}
		} else {
			if (messageData instanceof EntityData) {
				if (((EntityData) messageData).getEntity() instanceof Survey) {
					textfield_question1.setText(((Survey) ((EntityData) messageData).getEntity()).getFirstQuestion());
					textfield_question2.setText(((Survey) ((EntityData) messageData).getEntity()).getSecondQuestion());
					textfield_question3.setText(((Survey) ((EntityData) messageData).getEntity()).getThirdQuestion());
					textfield_question4.setText(((Survey) ((EntityData) messageData).getEntity()).getFourthQuestion());
					textfield_question5.setText(((Survey) ((EntityData) messageData).getEntity()).getFifthQuestion());
					textfield_question6.setText(((Survey) ((EntityData) messageData).getEntity()).getSixthQuestion());
				}
			} else {
				if (messageData instanceof RespondMessageData) {
					if ((((RespondMessageData) messageData).isSucceed())) {
						m_logger.severe("successfully updated");
					}
				}
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
