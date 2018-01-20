
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
	 * Updating and closing the survey.
	 *
	 * @param event
	 *            save button clicked.
	 */


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
				if (messageData instanceof RespondMessageData) {
					if ((((RespondMessageData) messageData).isSucceed())) {
						m_logger.severe("successfully updated");
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
