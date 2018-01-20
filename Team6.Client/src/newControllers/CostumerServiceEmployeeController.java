
package newControllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sun.media.jfxmediaimpl.platform.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import newEntities.Complaint;
import newEntities.Costumer;
import newEntities.IEntity;
import newEntities.ShopManager;
import newEntities.Survey;
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
public class CostumerServiceEmployeeController extends BaseController
{

	// region Fields
	
	private final String[]optinons_side =new String[] { "Add complaint", "Treat complaint","Add surveys" };
	
	private @FXML AnchorPane anchorpane_addcomplaint;

	private @FXML AnchorPane anchorpane_treatcomplaint;

	private @FXML AnchorPane anchorpane_addsurveys;
	
	//---------------------------------------------------Add complaint region fields-------------------------------------------------------//
	
	private @FXML TextField m_addcomplaint_textfield_id;
	
	private @FXML TextArea m_addcomplaint_textarea_costumercomplaint;
	
	private @FXML ComboBox<Integer> combobox_shop;
	
	private List<newEntities.IEntity> m_addcomplaint_shopmanager_array;
	
	private ArrayList<Integer> m_addcomplaint_managerid_array=new ArrayList<>();
	
	private ObservableList<Integer> m_addcomplaint_list;
	
	private Complaint m_addcomplaint_selected_complaint;
	
	private String correct_title;

	//---------------------------------------------------end region-> Add Complaint fields -------------------------------------------------------//
	
	//---------------------------------------------------Treat complaint region fields-------------------------------------------------------//
	
	private @FXML ComboBox<String> m_treatcomplaint_combobox_id;
	
	private @FXML  TextArea m_treatcomplaint_textarea_complaint;
	
	private @FXML  TextArea m_treatcomplaint_textarea_summary;
	
	private @FXML  CheckBox m_treatcomplaint_checkbox_financial;
	
	private @FXML  TextField m_treatcomplaint_financial_compensation;
	
	private List<IEntity> m_treatcomplaint_complaint_array;

	private ArrayList<String> m_treatcomplaint_id_array = new ArrayList<>();

	private ObservableList<String> m_treatcomplaint_list;

	private Complaint m_treatcomplaint_complaint_fun;

	private float m_treatcomplaint_refund;
	
	//---------------------------------------------------end region-> Treat complaint fields -------------------------------------------------------//

	//---------------------------------------------------Open/Close survey region fields-------------------------------------------------------//
	
	private @FXML ComboBox<String> opensurvey_combobox_shopname;
	
	private @FXML Button opensurvey_button_openclose_survey; 
	
	private @FXML TextField opensurvey_textfield_opendate;
	
	private @FXML DatePicker opensurvey_datepicker_enddate;
	
	private List<IEntity> m_addsurvey_shopmanager_array;

	private ArrayList<String> m_addsurvey_names_array = new ArrayList<>();

	private ObservableList<String> m_addsurvey_list;
	
	private List<IEntity> m_addsurvey_surveys_array;
	
	private int shopid=0;
	
	private Survey Corrct_survey;
	
	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");
	
	
	
	//---------------------------------------------------end region-> Open/Close survey fields -------------------------------------------------------//

	
	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{

	}
	
	//---------------------------------------------------Add Complaint methods region-------------------------------------------------------//
	
	/**
	 * The function checks that the fields aren't empty,
	 * and call the function "setNewComplaint" that sends data to server.
	 *
	 * @param event
	 * 			Save button clicked.
	 */
	@FXML
	public void addComplaint_saveButtonClick(ActionEvent event)
	{
		if(m_addcomplaint_textfield_id.getText().equals("")||m_addcomplaint_textarea_costumercomplaint.getText().equals(""))
		{
		showInformationMessage("One or more of the fileds are empty");
		}
		else
		{
			m_addcomplaint_selected_complaint= new Complaint();
			m_addcomplaint_selected_complaint.setShopManagerId(combobox_shop.getValue());
			m_addcomplaint_selected_complaint.setComplaint(m_addcomplaint_textarea_costumercomplaint.getText());
			int costumer_id=Integer.parseInt(m_addcomplaint_textfield_id.getText());
			Costumer entity=new Costumer();
			entity.setId(costumer_id);
			Message msg= MessagesFactory.createGetEntityMessage(entity);
			m_Client.sendMessageToServer(msg);
			combobox_shop.setDisable(true);
			combobox_shop.setValue(null);
			combobox_shop.setDisable(false);
			
			m_addcomplaint_textarea_costumercomplaint.clear();
			m_addcomplaint_textfield_id.clear();
		}
	}
	
	private void addcomplaint_initializeShopCombobox()
	{
		ShopManager entity= new ShopManager();
		Message msg=MessagesFactory.createGetAllEntityMessage(entity);
		m_Client.sendMessageToServer(msg);
	}
	
	
	
	
	//---------------------------------------------------end region-> Add Complaint methods -------------------------------------------------------//

	//---------------------------------------------------Treat complaint methods region-------------------------------------------------------//
	
	private void treatComplaint_initializeComplaint()
	{
		Complaint entity = new Complaint();
		Message msg = MessagesFactory.createGetAllEntityMessage(entity);
		m_Client.sendMessageToServer(msg);
	}
	
	@FXML
	public void treatComplaint_financialCompensation(ActionEvent event)
	{
		if (m_treatcomplaint_financial_compensation.isDisable()) m_treatcomplaint_financial_compensation.setDisable(false);
		else m_treatcomplaint_financial_compensation.setDisable(true);
	}
	
	/**
	 * TODO Send message to server with the update complain.
	 *
	 * @param event
	 *            Update button clicked
	 */
	@FXML
	public void treatComplaint_updateClick(ActionEvent event)
	{
		if ((m_treatcomplaint_combobox_id.getValue()==null)) 
		{
			showInformationMessage("complaint id field is empty");
			return;
		}
		if(m_treatcomplaint_textarea_summary.getText().equals(""))
		{
			showInformationMessage("Summery field is empty");
			return;
		}
		int com_id = Integer.parseInt(m_treatcomplaint_combobox_id.getValue());
		int i = 0;
		while (((Complaint) m_treatcomplaint_complaint_array.get(i)).getId() != com_id) {
			i++;
		}
		((Complaint) m_treatcomplaint_complaint_array.get(i)).setSummary(m_treatcomplaint_textarea_summary.getText());
		if (!(m_treatcomplaint_financial_compensation.isDisable())) {
			if (m_treatcomplaint_financial_compensation.getText().equals("")) {
				showInformationMessage("refund field is empty please enter refund or disable it.");
				return;
			}
			m_treatcomplaint_complaint_fun = (Complaint) m_treatcomplaint_complaint_array.get(i);
			m_treatcomplaint_complaint_fun.setSummary(m_treatcomplaint_textarea_summary.getText());
			try {
			m_treatcomplaint_refund = Float.parseFloat(m_treatcomplaint_financial_compensation.getText());
			}catch(NumberFormatException  e)
			{
				showInformationMessage("Cant Convert to float please enter number");
				return;
			}
			Costumer cos_entity = new Costumer();
			cos_entity.setId(m_treatcomplaint_complaint_fun.getCostumerId());
			Message msg = MessagesFactory.createGetEntityMessage(cos_entity);
			m_Client.sendMessageToServer(msg);
			tri();
		} else {
			((Complaint) m_treatcomplaint_complaint_array.get(i)).setOpened(false);
			Message msgg = MessagesFactory.createUpdateEntityMessage(m_treatcomplaint_complaint_array.get(i));
			m_Client.sendMessageToServer(msgg);
			tri();
		}
	}
	
	@FXML
	public void treatComplaint_setIdInCombobox()
	{
		m_treatcomplaint_list = FXCollections.observableArrayList(m_treatcomplaint_id_array);
		m_treatcomplaint_combobox_id.setItems(m_treatcomplaint_list);
	}
	
	@FXML
	public void treatComplaint_chooseComplaint(ActionEvent event)
	{
		if ((m_treatcomplaint_combobox_id.getValue() == null) || (m_treatcomplaint_combobox_id.getValue().equals(""))) return;
		int id = Integer.parseInt(m_treatcomplaint_combobox_id.getValue());
		Complaint comp = null;
		for (int i = 0; i < m_treatcomplaint_complaint_array.size(); i++) {
			Complaint temp = (Complaint) m_treatcomplaint_complaint_array.get(i);
			if (temp.getId() == id) comp = (Complaint) m_treatcomplaint_complaint_array.get(i);
		}
		m_treatcomplaint_textarea_complaint.setText(comp.getComplaint());
	}
	
	@FXML
	private void tri()
	{
		m_treatcomplaint_combobox_id.setDisable(true);
		m_treatcomplaint_combobox_id.getItems().clear();
		m_treatcomplaint_complaint_array.clear();
		m_treatcomplaint_id_array.clear();
		treatComplaint_initializeComplaint();
		m_treatcomplaint_combobox_id.setDisable(false);
		m_treatcomplaint_textarea_complaint.clear();
		m_treatcomplaint_textarea_summary.clear();
		m_treatcomplaint_financial_compensation.clear();
		m_treatcomplaint_financial_compensation.setDisable(true);
		m_treatcomplaint_checkbox_financial.setSelected(false);
	}
	
	
	
	//---------------------------------------------------end region-> Treat complaint methods -------------------------------------------------------//
	
	
	//---------------------------------------------------Open/Close survey methods region-------------------------------------------------------//
	
	private void openSurveys_initializeshopes()
	{
		ShopManager entity = new ShopManager();
		Message msg = MessagesFactory.createGetAllEntityMessage(entity);
		opensurvey_datepicker_enddate.setDayCellFactory(picker -> new DateCell() {

			@Override
			public void updateItem(LocalDate date, boolean empty)
			{
				super.updateItem(date, empty);
				setDisable(empty || date.isBefore(LocalDate.now()));
			}
		});
		opensurvey_textfield_opendate.setDisable(false);
		m_Client.sendMessageToServer(msg);
	}
	
	@FXML
	private void openSurveys_selectShop(ActionEvent event)
	{
		String shopname=opensurvey_combobox_shopname.getValue();
		shopid=0;
		for(int i=0;i<m_addsurvey_shopmanager_array.size();i++)
		{
			if(((ShopManager)m_addsurvey_shopmanager_array.get(i)).getName().equals(shopname))
			{
				shopid=((ShopManager)m_addsurvey_shopmanager_array.get(i)).getId();
			}
		}
		
		Survey survey_entity= new Survey();
		Message msg=MessagesFactory.createGetAllEntityMessage(survey_entity);
		m_Client.sendMessageToServer(msg);
	}
	
	@FXML
	private void openSurveys_AddSurvey(ActionEvent event)
	{
		String state=opensurvey_button_openclose_survey.getText();
		if(state.equals("Update end date"))
		{
			LocalDate localDate = opensurvey_datepicker_enddate.getValue();
			Corrct_survey.setEndDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));	
			Message msg=MessagesFactory.createUpdateEntityMessage(Corrct_survey);
			m_Client.sendMessageToServer(msg);
		}
		else
		{
			Survey survey_entity=new Survey();
			LocalDate localDate = opensurvey_datepicker_enddate.getValue();
			survey_entity.setStartDate(new Date());
			survey_entity.setEndDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			survey_entity.setManagerId(shopid);
			Message msg=MessagesFactory.createAddEntityMessage(survey_entity);
			m_Client.sendMessageToServer(msg);
		}
	}
	
	private void clear()
	{
		javafx.application.Platform.runLater(()-> {
			 opensurvey_button_openclose_survey.setText("Open");
			 opensurvey_combobox_shopname.setValue("");
			 opensurvey_textfield_opendate.setDisable(false);
			 opensurvey_textfield_opendate.setText("");
			 opensurvey_datepicker_enddate.setValue(null);
		});
	}
	
	
	
	
	
	
	//---------------------------------------------------Open/Close survey-> Treat complaint methods -------------------------------------------------------//
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		correct_title=title;
		if(title.equals(optinons_side[0]))
		{
			anchorpane_addsurveys.setVisible(false);
			anchorpane_treatcomplaint.setVisible(false);
			anchorpane_addcomplaint.setVisible(true);
			addcomplaint_initializeShopCombobox();

			return true;
		}
		else if(title.equals(optinons_side[1]))
		{
			anchorpane_addsurveys.setVisible(false);
			anchorpane_addcomplaint.setVisible(false);
			anchorpane_treatcomplaint.setVisible(true);
			treatComplaint_initializeComplaint();
			
			return true;
		}
		else if(title.equals(optinons_side[2]))
		{

			anchorpane_addsurveys.setVisible(true);
			anchorpane_addcomplaint.setVisible(false);
			anchorpane_treatcomplaint.setVisible(false);
			javafx.application.Platform.runLater(()-> {
			});
			m_addsurvey_names_array.clear();
			opensurvey_textfield_opendate.setDisable(false);
			opensurvey_textfield_opendate.setText("");
			openSurveys_initializeshopes();
			return true;
		}
		return false;
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getSideButtonsNames()
	{
		return optinons_side ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		if(correct_title.equals(optinons_side[0]))
		{
			if(msg.getMessageData() instanceof EntityData)
			{
				Costumer entity=(Costumer)((EntityData)msg.getMessageData()).getEntity();
				int id=entity.getId();
				m_addcomplaint_selected_complaint.setCostumerId(id);
				m_addcomplaint_selected_complaint.setCreationDate(new Date());
				m_addcomplaint_selected_complaint.setOpened(true);
				msg=MessagesFactory.createAddEntityMessage(m_addcomplaint_selected_complaint);
				m_Client.sendMessageToServer(msg);
			}
			else if (msg.getMessageData() instanceof RespondMessageData)
				{
					if(((RespondMessageData)msg.getMessageData()).getMessageData() instanceof EntityData)
					{
						if(((EntityData)((RespondMessageData)msg.getMessageData()).getMessageData()).getEntity() instanceof Costumer)
							{
								showInformationMessage("The costumer didnt exist");
							}
						else
						{
							if(((RespondMessageData)msg.getMessageData()).isSucceed())
							{
								m_Logger.severe("Successfully added complaint");
								addcomplaint_initializeShopCombobox();
							}
						}
					}
					
				}
				else if(msg.getMessageData() instanceof EntitiesListData)
				{
					m_addcomplaint_shopmanager_array=((EntitiesListData)msg.getMessageData()).getEntities();
					m_addcomplaint_managerid_array.clear();
					combobox_shop.getItems().clear();
					for(int i=0;i<m_addcomplaint_shopmanager_array.size();i++)
					{
						m_addcomplaint_managerid_array.add(((ShopManager)m_addcomplaint_shopmanager_array.get(i)).getId());
					}
					m_addcomplaint_list = FXCollections.observableArrayList(m_addcomplaint_managerid_array);
					combobox_shop.setItems(m_addcomplaint_list);
				}
		}
		else if(correct_title.equals(optinons_side[1]))
		{
			boolean flag = true;
			IMessageData messageData = msg.getMessageData();
			if (messageData instanceof EntitiesListData) // Happens when initializeComplaint works fill m_complaint_array.
			{
				EntitiesListData entitiesListData = (EntitiesListData) msg.getMessageData();
				m_treatcomplaint_complaint_array = entitiesListData.getEntities();
				m_treatcomplaint_id_array.clear();
				for (int i = 0; i < m_treatcomplaint_complaint_array.size(); i++) {
					Complaint temp = (Complaint) (m_treatcomplaint_complaint_array.get(i));
					if (temp.isOpened()) {
						String s = Integer.toString(temp.getId());
						m_treatcomplaint_id_array.add(s);
					}
				}
				javafx.application.Platform.runLater(()-> {
					treatComplaint_setIdInCombobox();
				});
				
			} else if (messageData instanceof EntityData) // Happens when it need to update refund.
			{
				Costumer cos_entity = (Costumer) ((EntityData) messageData).getEntity();
				cos_entity.setBalance(cos_entity.getBalance() + m_treatcomplaint_refund);
				msg = MessagesFactory.createUpdateEntityMessage(cos_entity);
				m_Client.sendMessageToServer(msg);
			} else if (msg.getMessageData() instanceof RespondMessageData) // Respond from server
			{
				RespondMessageData res = (RespondMessageData) msg.getMessageData();
				if (((EntityData) (res.getMessageData())).getEntity() instanceof Costumer) // Respond about costumer refund
				{
					if (!(res.isSucceed())) flag = false;
					else {
						m_treatcomplaint_complaint_fun.setOpened(false);
						msg = MessagesFactory.createUpdateEntityMessage(m_treatcomplaint_complaint_fun);
						m_Client.sendMessageToServer(msg);
					}
				} else {
					if (flag == false) {
						m_Logger.severe("Can't Update complaint please try again");
					} else {
						m_Logger.severe("Update succssed");
						tri();
					}
				}
			}
		}
		else if(correct_title.equals(optinons_side[2]))
		{
			int flag=0;
			IMessageData messageData = msg.getMessageData();
			if(messageData instanceof EntitiesListData)
			{
				if(((EntitiesListData) messageData).getEntities().get(0) instanceof ShopManager)
				{
					m_addsurvey_shopmanager_array=((EntitiesListData) messageData).getEntities();
					for(int i=0;i<m_addsurvey_shopmanager_array.size();i++)
					{
						m_addsurvey_names_array.add(((ShopManager)m_addsurvey_shopmanager_array.get(i)).getName());
					}
					javafx.application.Platform.runLater(()-> {
						opensurvey_combobox_shopname.getItems().clear();
						m_addsurvey_list = FXCollections.observableArrayList(m_addsurvey_names_array);
						opensurvey_combobox_shopname.setItems(m_addsurvey_list);
					});

				}
				else if(((EntitiesListData) messageData).getEntities().get(0) instanceof Survey)
				{
					m_addsurvey_surveys_array=((EntitiesListData) messageData).getEntities();
					for(int i=0;i<m_addsurvey_surveys_array.size();i++)
					{
						if(((Survey)m_addsurvey_surveys_array.get(i)).getManagerId()==shopid)
						{
							if(((Survey)m_addsurvey_surveys_array.get(i)).getEndDate().after(new Date()))  // There is open survey
							{
								Corrct_survey=(Survey)m_addsurvey_surveys_array.get(i);
								flag=1;
							}
						}
					}
					if(flag==1)
					{
						opensurvey_textfield_opendate.setText(s_dateForamt.format(Corrct_survey.getStartDate()));
						
						opensurvey_datepicker_enddate.setValue(Corrct_survey.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
						opensurvey_textfield_opendate.setDisable(true);
						javafx.application.Platform.runLater(()-> {
							 opensurvey_button_openclose_survey.setText("Update end date");
						});
					}
					else
					{
						javafx.application.Platform.runLater(()-> {
							 opensurvey_button_openclose_survey.setText("Open");
						});
						opensurvey_textfield_opendate.setText(s_dateForamt.format(new Date()));
						opensurvey_datepicker_enddate.setValue(null);
						opensurvey_textfield_opendate.setDisable(true);
					}
				}
			}
			else if(messageData instanceof RespondMessageData)
			{
				if(((RespondMessageData) messageData).isSucceed())
				{
					m_Logger.severe("work");
					clear();
				}
			}
		}
	}

	// end region -> BaseController Implementation
}
