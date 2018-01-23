
package newControllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.ParseConversionEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import newEntities.Costumer;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.ShopEmployee;
import newEntities.ShopManager;
import newEntities.User;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * ExampleController: TODO Yoni
 * 
 * 
 */
public class AdministratorController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	private @FXML AnchorPane anchorpane_option21;

	private @FXML AnchorPane anchorpane_option2;

	private @FXML Button button_get;
	
	private @FXML Button button_update;
	
	private @FXML Button button_update1;
	
	private @FXML ComboBox<String> comboBox_user;
	
	private @FXML ComboBox<String> comboBox_status;
	
	private @FXML ComboBox<String> comboBox_privillge;
	
	private @FXML TextField textField_username;

	private @FXML TextField textField_email;

	private @FXML TextField textField_password;

	private @FXML TextField textField_branch;
	
	private @FXML TextField textField_shopManagerId;
	
	private @FXML Label label_branch;

	private @FXML Label label_shopManagerId;
	
	private @FXML Label label_privillege;
	
	private @FXML Label label_status;
	
	private @FXML Label label_password;
	
	private @FXML Label label_email;
	
	private User selected_user;
	
	private String title;
	
	private String[] window = { "User Status", "User Coordinates" };

	ArrayList<IEntity> arrlist;
	
	ObservableList<String> list;

	User tempEntity;

	User userEntity;

	ShopEmployee shopEmployee = new ShopEmployee();

	ShopManager shopManager = new ShopManager();

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		initializeStatus();
		initializePrivillge();
	}
	
	protected void initializeUsers()
	{
		User tempEntity = new User();
		Message msg = MessagesFactory.createGetAllEntityMessage(tempEntity);
		m_Client.sendMessageToServer(msg);
	}
	
	protected void initializeStatus()
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add("Blocked");
		al.add("Actived");
		list = FXCollections.observableArrayList(al);
		comboBox_status.setItems(list);
		comboBox_status.setVisible(false);
		label_status.setVisible(false);
	}

	protected void initializePrivillge()
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add("administrator");
		al.add("shop employee");
		al.add("chain manager");
		al.add("shop manager");
		al.add("service specialist");
		al.add("company employee");
		al.add("costumer service");
		list = FXCollections.observableArrayList(al);
		comboBox_privillge.setItems(list);
		comboBox_privillge.setVisible(false);
		label_privillege.setVisible(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		this.title=title;
		switch (title) {
			case "User Status":
				comboBox_user.setValue(null);
				comboBox_privillge.setValue(null);
				comboBox_status.setValue(null);
				comboBox_privillge.setVisible(false);
				comboBox_status.setVisible(false);
				label_privillege.setVisible(false);
				label_status.setVisible(false);
				button_update1.setVisible(false);
				anchorpane_option1.setVisible(true);
				anchorpane_option21.setVisible(false);
				initializeUsers();
				
			break;

			case "User Coordinates":
				anchorpane_option1.setVisible(false);
				anchorpane_option21.setVisible(true);
				textField_username.setText("Enter Username");
				textField_email.setVisible(false);
				textField_password.setVisible(false);
				textField_branch.setVisible(false);
				textField_shopManagerId.setVisible(false);
				label_email.setVisible(false);
				label_password.setVisible(false);
				label_branch.setVisible(false);
				label_shopManagerId.setVisible(false);
				button_update.setVisible(false);

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
		return window;//new String[] { "User Status", "User Coordinates" };
	}

	protected void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}
	
	/**
	 *
	 * @param event : send update message to the server when update button pressed
	 * @throws IOException :
	 */
	public void updatebtn1(ActionEvent event) throws IOException
	{
		String username = comboBox_user.getValue();
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) {
				selected_user = (User) arrlist.get(i);
				break;
			}
		}

		String privilege = comboBox_privillge.getValue().toString();
		if(privilege=="administrator")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Administrator);
		else if(privilege=="costumer")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Costumer);
		else if(privilege=="shop employee")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopEmployee);
		else if(privilege=="chain manager")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ChainManager);
		else if(privilege=="shop manager"||privilege=="ShopManager")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopManager);
		else if(privilege=="service specialist")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ServiceSpecialist);
		else if(privilege=="company employee")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CompanyEmployee);
		else if(privilege=="costumer service")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CostumerService);	
		//msg=MessagesFactory.createUpdateEntityMessage(selected_user);
		//m_Client.sendMessageToServer(msg);
		
		String status = comboBox_status.getValue().toString();
		if(status.equals("Actived")) {
			if(selected_user.getStatus().equals(EntitiesEnums.UserStatus.Blocked))
			selected_user.setStatus(EntitiesEnums.UserStatus.Disconnected);
		}
		else if(status.equals("Blocked"))
			selected_user.setStatus(EntitiesEnums.UserStatus.Blocked);
		Message msg=MessagesFactory.createUpdateEntityMessage(selected_user);
		m_Client.sendMessageToServer(msg);
	}
	
	/**
	 *
	 * @param event : get the selected user from comboBox_user
	 * @throws IOException :
	 */
	@FXML
	public void userSelected(ActionEvent event) throws IOException
	{
		String username = comboBox_user.getValue() == null ? "" :comboBox_user.getValue() ;
		button_update1.setVisible(true);
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) selected_user = (User) arrlist.get(i);
		}
		comboBox_privillge.setValue((selected_user.getPrivilege().toString()));
		if(selected_user.getStatus().equals(EntitiesEnums.UserStatus.Connected)||selected_user.getStatus().equals(EntitiesEnums.UserStatus.Disconnected))
			comboBox_status.setValue("Actived");
		else
			comboBox_status.setValue("Blocked");
		comboBox_status.setVisible(true);
		label_status.setVisible(true);
		if(username.equals("costumer")) {
			comboBox_privillge.setVisible(false);
			label_privillege.setVisible(false);
		}else
		{
			comboBox_privillge.setVisible(true);
			label_privillege.setVisible(true);
		}
	}


	/**
	 *
	 * @param event : request all the system users from the server when get button pressed
	 */
	public void getBtn(ActionEvent event)
	{
		User tempEntity = new User();
		
		Message msg = MessagesFactory.createGetAllEntityMessage(tempEntity);
		m_Client.sendMessageToServer(msg);
	}

	/**
	 *
	 * @param event : send update message to the server when update button pressed
	 * @throws IOException :
	 */
	public void updatebtn(ActionEvent event) throws IOException
	{
		if (textField_email.getText().equals("")) showInformationMessage("Please enter email.");
		else if (textField_password.getText().equals("")) showInformationMessage("Please enter password.");
		else if (textField_username.getText().equals("")) showInformationMessage("Please enter username to edit.");
		else {
			switch (tempEntity.getPrivilege()) {
				case ShopEmployee:
					ShopEmployee shopEmployeeEntity = new ShopEmployee();
					shopEmployeeEntity.setUserName(tempEntity.getUserName());
					shopEmployeeEntity.setEmail(textField_email.getText());
					shopEmployeeEntity.setPassword(textField_password.getText());
					shopEmployeeEntity.setPrivilege(tempEntity.getPrivilege());
					shopEmployeeEntity.setStatus(tempEntity.getStatus());
					shopEmployeeEntity.setShopManagerId(Integer.parseInt(textField_shopManagerId.getText()));
					shopEmployeeEntity.setId(shopEmployee.getId());
					Message shopEmployeeEntityMessage = MessagesFactory.createUpdateEntityMessage(shopEmployeeEntity);
					m_Client.sendMessageToServer(shopEmployeeEntityMessage);
					//shopEmployeeEntityMessage = MessagesFactory.createAddEntityMessage(shopEmployeeEntity);
					//m_Client.sendMessageToServer(shopEmployeeEntityMessage);
				break;
				case ShopManager:
					shopManager.setUserName(tempEntity.getUserName());
					shopManager.setEmail(textField_email.getText());
					shopManager.setPassword(textField_password.getText());
					shopManager.setName(textField_branch.getText());
					shopManager.setPrivilege(tempEntity.getPrivilege());
					shopManager.setStatus(tempEntity.getStatus());
					Message shopManagerEntityMessage = MessagesFactory.createUpdateEntityMessage(shopManager);
					m_Client.sendMessageToServer(shopManagerEntityMessage);
				break;
				default:
					userEntity.setEmail(textField_email.getText());
					userEntity.setPassword(textField_password.getText());
					Message entityMessage = MessagesFactory.createUpdateEntityMessage(userEntity);
					m_Client.sendMessageToServer(entityMessage);
				break;

			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		if(title.equals(window[0]))
		{
			if(msg.getMessageData() instanceof EntitiesListData)
			{
			EntitiesListData entityListData=(EntitiesListData) msg.getMessageData();
			arrlist = (ArrayList<IEntity>) entityListData.getEntities();
			ArrayList<String> al = new ArrayList<String>();
			User user = new User();
			for(int i=0;i<arrlist.size();i++)
			{		
				user = (User) arrlist.get(i);
				al.add(user.getUserName());	
			}
			javafx.application.Platform.runLater(()-> {
				list = FXCollections.observableArrayList(al);
				if(list!=null)
					comboBox_user.setItems(list);
			});
			}
			else if(msg.getMessageData() instanceof RespondMessageData)
			{
				RespondMessageData res = (RespondMessageData)msg.getMessageData();
				if(res.isSucceed())
				{
					showInformationMessage("User update successed");
				}
				else if(!res.isSucceed())
				{
					showInformationMessage("User update faild please try again");
					m_Logger.warning("Failed when sending a message to the server.");
				}else
				{
					m_Logger.warning(
							"Received message data not of the type requested, requested: " + EntitiesListData.class.getName());
				}
			}
			else
				{
					m_Logger.warning("Received message data not of the type requested.");
					return;
				}
		}
		else{
		if(msg.getMessageData() instanceof EntityData)
		{	
			EntityData entityData =(EntityData) msg.getMessageData();
			if(entityData.getEntity() instanceof ShopEmployee)
			{
			shopEmployee  = (ShopEmployee) entityData.getEntity();
			textField_shopManagerId.setText(Integer.toString(shopEmployee.getShopManagerId()));
			}
			if(entityData.getEntity() instanceof ShopManager)
			{
			shopManager  = (ShopManager) entityData.getEntity();
			textField_branch.setText(shopManager.getName());
			}
			
		}
		else if(msg.getMessageData() instanceof EntitiesListData)
		{
		EntitiesListData entityListData=(EntitiesListData) msg.getMessageData();
		arrlist = (ArrayList<IEntity>) entityListData.getEntities();
		String name = textField_username.getText();
		if(name.equals("")) 
		{
			textField_email.setVisible(false);
			textField_password.setVisible(false);
			textField_branch.setVisible(false);
			textField_shopManagerId.setVisible(false);
			label_email.setVisible(false);
			label_password.setVisible(false);
			label_branch.setVisible(false);
			label_shopManagerId.setVisible(false);
			button_update.setVisible(false);
			showInformationMessage("Please enter username");
		}
		for(int i=0;i<arrlist.size();i++)
		{
			tempEntity = (User) arrlist.get(i);
			if(tempEntity.getUserName().equals(name))
			{
				button_update.setVisible(true);
				label_password.setVisible(true);
				label_email.setVisible(true);
				textField_email.setVisible(true);
				textField_password.setVisible(true);
				if(tempEntity.getPrivilege().equals(EntitiesEnums.UserPrivilege.ShopEmployee))
				{
					shopEmployee.setUserName(tempEntity.getUserName());
					shopEmployee.setEmail(tempEntity.getEmail());
					shopEmployee.setPassword(tempEntity.getPassword());
					shopEmployee.setPrivilege(tempEntity.getPrivilege());
					shopEmployee.setStatus(tempEntity.getStatus());
					textField_email.setText(tempEntity.getEmail());
					textField_password.setText(tempEntity.getPassword());
					label_shopManagerId.setVisible(true);
					textField_shopManagerId.setVisible(true);
					label_branch.setVisible(false);
					textField_branch.setVisible(false);
					msg=MessagesFactory.createGetEntityMessage(shopEmployee);
					m_Client.sendMessageToServer(msg);
					break;
				} 
				else if(tempEntity.getPrivilege().equals(EntitiesEnums.UserPrivilege.ShopManager))
				{
					shopManager.setUserName(tempEntity.getUserName());
					textField_email.setText(tempEntity.getEmail());
					textField_password.setText(tempEntity.getPassword());
					label_shopManagerId.setVisible(false);
					textField_shopManagerId.setVisible(false);
					label_branch.setVisible(true);
					textField_branch.setVisible(true);
					msg=MessagesFactory.createGetEntityMessage(shopManager);
					m_Client.sendMessageToServer(msg);
					break;
				}
				else
				{
					textField_email.setText(tempEntity.getEmail());
					textField_password.setText(tempEntity.getPassword());
					userEntity=tempEntity;
					label_shopManagerId.setVisible(false);
					textField_shopManagerId.setVisible(false);
					label_branch.setVisible(false);
					textField_branch.setVisible(false);
					break;
				}
				
				
			}
				
		}
		if(!tempEntity.getUserName().equals(name)&&!name.equals(""))
		{
			button_update.setVisible(false);
			textField_email.setVisible(false);
			textField_password.setVisible(false);
			textField_branch.setVisible(false);
			textField_shopManagerId.setVisible(false);
			label_email.setVisible(false);
			label_password.setVisible(false);
			label_branch.setVisible(false);
			label_shopManagerId.setVisible(false);
			showInformationMessage("User don't exist");
		}
	}
		
		else 
			if(msg.getMessageData() instanceof RespondMessageData)
			{
				RespondMessageData res = (RespondMessageData)msg.getMessageData();
				if(res.isSucceed())
				{
					showInformationMessage("User update successed");
				}
				else if(!res.isSucceed())
				{
					showInformationMessage("User update faild please try again");
					m_Logger.warning("Failed when sending a message to the server.");
				}else
				{
					m_Logger.warning(
							"Received message data not of the type requested, requested: " + EntitiesListData.class.getName());
				}
			}
			else
				{
					m_Logger.warning("Received message data not of the type requested.");
					return;
				}
		}
}

	// end region -> BaseController Implementation

}
