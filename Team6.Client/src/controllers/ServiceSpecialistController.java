
package controllers;

import java.util.ArrayList;
import java.util.List;

import entities.IEntity;
import entities.SurveyResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import messages.EntitiesListData;
import messages.IMessageData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 *
 * ServiceSpecialistController: manages the service specialist UI.
 * 
 * 
 */
public class ServiceSpecialistController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	private @FXML ImageView imageview_gif;

	private @FXML ImageView imageview_title;

	private @FXML ComboBox<String> combobox_id;

	private @FXML TextField textfield_question1;

	private @FXML TextField textfield_question2;

	private @FXML TextField textfield_question3;

	private @FXML TextField textfield_question4;

	private @FXML TextField textfield_question5;

	private @FXML TextField textfield_question6;

	private @FXML TextField textfield_answer1;

	private @FXML TextField textfield_answer2;

	private @FXML TextField textfield_answer3;

	private @FXML TextField textfield_answer4;

	private @FXML TextField textfield_answer5;

	private @FXML TextField textfield_answer6;

	private @FXML TextArea textarea_analysis;

	private ObservableList<String> m_list;

	private List<IEntity> m_survey_array;

	private ArrayList<String> m_surveysid_array = new ArrayList<String>();

	private SurveyResult selected_survey;

	private String[] questions = { "Question1", "Question2", "Question3", "Question4", "Question5", "Question6", };

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		textarea_analysis.textProperty().addListener((observable, old_value, new_value) -> {
			if (new_value.length() == 501) {
				textarea_analysis.setText(old_value);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		switch (title) {
			case "Add analysis":
				anchorpane_option1.setVisible(true);
				javafx.application.Platform.runLater(() -> {
					initializesurveys();
				});
			break;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getSideButtonsNames()
	{
		return new String[] { "Add analysis" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof EntitiesListData) {
			if (((EntitiesListData) messageData).getEntities().get(0) instanceof SurveyResult) {
				m_survey_array = ((EntitiesListData) messageData).getEntities();
				m_surveysid_array.clear();
				for (int i = 0; i < m_survey_array.size(); i++) {
					if (((SurveyResult) m_survey_array.get(i)).getSummary() == null)
						m_surveysid_array.add(Integer.toString(((SurveyResult) m_survey_array.get(i)).getId()));
				}
				javafx.application.Platform.runLater(() -> {
					setInCombobox();
				});

			}
		} else {
			if (messageData instanceof RespondMessageData) {
				if ((((RespondMessageData) messageData).isSucceed())) {
					m_Logger.severe("successfully updated");
					javafx.application.Platform.runLater(() -> {
						initializesurveys();
						showInformationMessage("Successfully added");
					});

				}
			}
		}
	}

	// end region -> BaseController Implementation

	// UI events region

	/**
	 * Update the complaints add specialist analysis
	 *
	 * @param event
	 * 			save button clicked.
	 */
	@FXML
	public void saveAnalysis(ActionEvent event)
	{
		if ((textarea_analysis.getText().equals("")) || (combobox_id.getValue().equals(""))) {
			showInformationMessage("Specialist analisys area and/or survey ID are empty");
		} else {
			selected_survey.setSummary(textarea_analysis.getText());
			Message msg = MessagesFactory.createUpdateEntityMessage(selected_survey);
			m_Client.sendMessageToServer(msg);
		}
	}

	private void initializesurveys()
	{
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
		m_surveysid_array.clear();
		textarea_analysis.clear();
		SurveyResult shopsurvey_entity = new SurveyResult();
		Message msg = MessagesFactory.createGetAllEntityMessage(shopsurvey_entity);
		m_Client.sendMessageToServer(msg);
	}

	@FXML
	private void selectSurvey(ActionEvent event)
	{
		if (combobox_id.getValue() == null) {
			return;
		}
		if (combobox_id.getValue().equals("")) return;
		int id = Integer.parseInt(combobox_id.getValue());

		SurveyResult correct_survey = null;
		for (int i = 0; i < m_survey_array.size(); i++) {
			if (((SurveyResult) m_survey_array.get(i)).getId() == id) {
				correct_survey = (SurveyResult) m_survey_array.get(i);
			}
		}

		selected_survey = correct_survey;
		textfield_answer1.setText(Integer.toString(correct_survey.getFirstAnswer()));
		textfield_answer2.setText(Integer.toString(correct_survey.getSecondAnswer()));
		textfield_answer3.setText(Integer.toString(correct_survey.getThirdAnswer()));
		textfield_answer4.setText(Integer.toString(correct_survey.getFourthAnswer()));
		textfield_answer5.setText(Integer.toString(correct_survey.getFifthAnswer()));
		textfield_answer6.setText(Integer.toString(correct_survey.getSixthAnswer()));
		textfield_question1.setText(questions[0]);
		textfield_question2.setText(questions[1]);
		textfield_question3.setText(questions[2]);
		textfield_question4.setText(questions[3]);
		textfield_question5.setText(questions[4]);
		textfield_question6.setText(questions[5]);
	}

	private void setInCombobox()
	{
		javafx.application.Platform.runLater(() -> {
			m_list = FXCollections.observableArrayList(m_surveysid_array);
			combobox_id.getItems().clear();
			combobox_id.setItems(m_list);
		});
	}

	// End of -> UI event region
}