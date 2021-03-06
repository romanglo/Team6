
package controllers;

import java.util.ArrayList;

import entities.EntitiesEnums;
import entities.IEntity;
import entities.ShopEmployee;
import entities.ShopManager;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import messages.EntitiesListData;
import messages.EntityData;
import messages.Message;
import messages.MessagesFactory;
import messages.RespondMessageData;

/**
 * AdministratorController : Managed the Administrator UI.
 * 
 * @see BaseController
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

	private @FXML ComboBox<String> comboBox_user1;

	// private @FXML TextField textField_username;

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

	private ArrayList<IEntity> arrlist;

	private ObservableList<String> list;

	private ShopEmployee shopEmployee = new ShopEmployee();

	private ShopManager shopManager = new ShopManager();

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{
		initializeStatus();
		initializePrivilege();

	}

	/**
	 * The method ask all users from server.
	 */
	private void initializeUsers()
	{
		User tempEntity = new User();
		Message msg = MessagesFactory.createGetAllEntityMessage(tempEntity);
		m_Client.sendMessageToServer(msg);
		textField_email.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 41) textField_email.setText(oldValue);
		});
		textField_password.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 21) textField_password.setText(oldValue);
		});
		textField_branch.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 21) textField_branch.setText(oldValue);
		});
		textField_shopManagerId.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty() && !(newValue.charAt(newValue.length() - 1) >= '0'
					&& newValue.charAt(newValue.length() - 1) <= '9'))
				textField_shopManagerId.setText(oldValue);
		});
	}

	/**
	 * The method initialize the status combo box.
	 */
	private void initializeStatus()
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add("Blocked");
		al.add("Activated");
		list = FXCollections.observableArrayList(al);
		comboBox_status.setItems(list);
		comboBox_status.setVisible(false);
		label_status.setVisible(false);
	}

	/**
	 * The method initialize the privilege combo box.
	 */
	private void initializePrivilege()
	{
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Administrator");
		arrayList.add("Shop employee");
		arrayList.add("Chain manager");
		arrayList.add("Shop manager");
		arrayList.add("Service specialist");
		arrayList.add("Company employee");
		arrayList.add("Costumer service");
		list = FXCollections.observableArrayList(arrayList);
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
		this.title = title;
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
				comboBox_user1.setValue(null);
				textField_email.setVisible(false);
				textField_password.setVisible(false);
				textField_branch.setVisible(false);
				textField_shopManagerId.setVisible(false);
				label_email.setVisible(false);
				label_password.setVisible(false);
				label_branch.setVisible(false);
				label_shopManagerId.setVisible(false);
				button_update.setVisible(false);
				anchorpane_option1.setVisible(false);
				anchorpane_option21.setVisible(true);
				initializeUsers();

			break;

			default:
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return window : return the window name to open the right window
	 */
	@Override
	protected String[] getSideButtonsNames()
	{
		return window;
	}

	/**
	 * @param event
	 *            : send update message to the server when update button pressed
	 */
	@FXML
	private void updatebtn1(ActionEvent event)
	{
		String username = comboBox_user.getValue();
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) {
				selected_user = (User) arrlist.get(i);
				break;
			}
		}

		String privilege = comboBox_privillge.getValue().toString();
		if (privilege == "Administrator") selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Administrator);
		else if (privilege == "Costumer") selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Costumer);
		else if (privilege == "Shop employee") selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopEmployee);
		else if (privilege == "Chain manager") selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ChainManager);
		else if (privilege == "Shop manager" || privilege == "ShopManager")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopManager);
		else if (privilege == "Service specialist")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ServiceSpecialist);
		else if (privilege == "Company employee")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CompanyEmployee);
		else if (privilege == "Costumer service")
			selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CostumerService);

		String status = comboBox_status.getValue().toString();
		if (status.equals("Activated")) {
			if (selected_user.getStatus().equals(EntitiesEnums.UserStatus.Blocked))
				selected_user.setStatus(EntitiesEnums.UserStatus.Disconnected);
		} else if (status.equals("Blocked")) selected_user.setStatus(EntitiesEnums.UserStatus.Blocked);
		Message msg = MessagesFactory.createUpdateEntityMessage(selected_user);
		m_Client.sendMessageToServer(msg);
	}

	/**
	 *
	 * @param event
	 *            : get the selected user from comboBox_user
	 */
	@FXML
	private void userSelected(ActionEvent event)
	{
		String username = comboBox_user.getValue() == null ? "" : comboBox_user.getValue();
		button_update1.setVisible(true);
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) selected_user = (User) arrlist.get(i);
		}
		comboBox_privillge.setValue((selected_user.getPrivilege().toString()));
		if (selected_user.getStatus().equals(EntitiesEnums.UserStatus.Connected)
				|| selected_user.getStatus().equals(EntitiesEnums.UserStatus.Disconnected))
			comboBox_status.setValue("Activated");
		else comboBox_status.setValue("Blocked");
		comboBox_status.setVisible(true);
		label_status.setVisible(true);
		if (selected_user.getPrivilege().equals(EntitiesEnums.UserPrivilege.Costumer)) {
			comboBox_privillge.setVisible(false);
			label_privillege.setVisible(false);
		} else {
			comboBox_privillge.setVisible(true);
			label_privillege.setVisible(true);
		}
	}

	/**
	 *
	 * @param event
	 *            : get the selected user from comboBox_user1
	 */
	@FXML
	private void userSelected1(ActionEvent event)
	{
		if (comboBox_user1 == null) {
			return;
		}
		Message msg;
		String username = comboBox_user1.getValue() == null ? "" : comboBox_user1.getValue();
		button_update.setVisible(true);
		label_password.setVisible(true);
		label_email.setVisible(true);
		textField_email.setVisible(true);
		textField_password.setVisible(true);

		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) selected_user = (User) arrlist.get(i);
		}
		if (selected_user != null) {
			textField_email.setText(selected_user.getEmail());
			textField_password.setText(selected_user.getPassword());
			if (selected_user.getPrivilege().equals(EntitiesEnums.UserPrivilege.ShopManager)) {
				ShopManager shopManager = new ShopManager();
				shopManager.setUserName(selected_user.getUserName());
				shopManager.setEmail(selected_user.getEmail());
				shopManager.setPassword(selected_user.getPassword());
				shopManager.setPrivilege(selected_user.getPrivilege());
				shopManager.setStatus(selected_user.getStatus());
				msg = MessagesFactory.createGetEntityMessage(shopManager);
				m_Client.sendMessageToServer(msg);
			} else if (selected_user.getPrivilege().equals(EntitiesEnums.UserPrivilege.ShopEmployee)) {
				ShopEmployee shopEmployee = new ShopEmployee();
				shopEmployee.setUserName(selected_user.getUserName());
				shopEmployee.setEmail(selected_user.getEmail());
				shopEmployee.setPassword(selected_user.getPassword());
				shopEmployee.setPrivilege(selected_user.getPrivilege());
				shopEmployee.setStatus(selected_user.getStatus());
				msg = MessagesFactory.createGetEntityMessage(shopEmployee);
				m_Client.sendMessageToServer(msg);
			} else {
				msg = MessagesFactory.createGetEntityMessage(selected_user);
				m_Client.sendMessageToServer(msg);
			}

		}

	}

	@FXML
	private void updatebtn(ActionEvent event)
	{
		if(comboBox_user1.getValue()==null)
		{
			return;
		}
		if(comboBox_user1.getValue().equals("Select username"))
		{
			showAlertMessage("Please select user.", AlertType.INFORMATION);
			return;
		}
		if (textField_email.getText().equals("")) {
			showAlertMessage("Please enter email.", AlertType.INFORMATION);
			return;
		}  if (textField_password.getText().equals("")) {
			showAlertMessage("Please enter password.", AlertType.INFORMATION);
			return;
		}  if(textField_branch.isVisible())
		{
			if(textField_branch.getText().equals(""))
			{
				showAlertMessage("Please enter branch.", AlertType.INFORMATION);
				return;
			}
		}
		if(textField_shopManagerId.isVisible())
		{
			if(textField_shopManagerId.getText().equals(""))
			{
				showAlertMessage("Please enter Shop Manager id.", AlertType.INFORMATION);
				return;
			}
		}
			switch (selected_user.getPrivilege()) {
				case ShopEmployee:
					ShopEmployee shopEmployeeEntity = new ShopEmployee();
					shopEmployeeEntity.setUserName(selected_user.getUserName());
					shopEmployeeEntity.setEmail(textField_email.getText());
					shopEmployeeEntity.setPassword(textField_password.getText());
					shopEmployeeEntity.setPrivilege(selected_user.getPrivilege());
					shopEmployeeEntity.setStatus(selected_user.getStatus());
					shopEmployeeEntity.setShopManagerId(Integer.parseInt(textField_shopManagerId.getText()));
					shopEmployeeEntity.setId(shopEmployee.getId());
					Message shopEmployeeEntityMessage = MessagesFactory.createUpdateEntityMessage(shopEmployeeEntity);
					m_Client.sendMessageToServer(shopEmployeeEntityMessage);

					selected_user.setEmail(textField_email.getText());
					selected_user.setPassword(textField_password.getText());
					Message entityMessage = MessagesFactory.createUpdateEntityMessage(selected_user);
					m_Client.sendMessageToServer(entityMessage);
				break;
				case ShopManager:
					shopManager.setUserName(selected_user.getUserName());
					shopManager.setEmail(textField_email.getText());
					shopManager.setPassword(textField_password.getText());
					shopManager.setName(textField_branch.getText());
					shopManager.setPrivilege(selected_user.getPrivilege());
					shopManager.setStatus(selected_user.getStatus());
					Message shopManagerEntityMessage = MessagesFactory.createUpdateEntityMessage(shopManager);
					m_Client.sendMessageToServer(shopManagerEntityMessage);

					selected_user.setEmail(textField_email.getText());
					selected_user.setPassword(textField_password.getText());
					Message entityMessage1 = MessagesFactory.createUpdateEntityMessage(selected_user);
					m_Client.sendMessageToServer(entityMessage1);
				break;
				default:
					selected_user.setEmail(textField_email.getText());
					selected_user.setPassword(textField_password.getText());
					Message entityMessage2 = MessagesFactory.createUpdateEntityMessage(selected_user);
					m_Client.sendMessageToServer(entityMessage2);
				break;

			}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{
		if (title.equals(window[0])) {
			if (msg.getMessageData() instanceof EntitiesListData) {
				EntitiesListData entityListData = (EntitiesListData) msg.getMessageData();
				arrlist = (ArrayList<IEntity>) entityListData.getEntities();
				ArrayList<String> al = new ArrayList<String>();
				User user = new User();
				for (int i = 0; i < arrlist.size(); i++) {
					user = (User) arrlist.get(i);
					al.add(user.getUserName());
				}
				javafx.application.Platform.runLater(() -> {
					list = FXCollections.observableArrayList(al);
					if (list != null) comboBox_user.setItems(list);
				});
			} else if (msg.getMessageData() instanceof RespondMessageData) {
				RespondMessageData res = (RespondMessageData) msg.getMessageData();
				if (res.isSucceed()) {
					showAlertMessage("User update succeed", AlertType.INFORMATION);
				} else if (!res.isSucceed()) {
					showAlertMessage("User update faild please try again", AlertType.WARNING);
					m_Logger.warning("Failed when sending a message to the server.");
				} else {
					m_Logger.warning("Received message data not of the type requested, requested: "
							+ EntitiesListData.class.getName());
				}
			} else {
				m_Logger.warning("Received message data not of the type requested.");
				return;
			}
		} else {
			if (msg.getMessageData() instanceof EntityData) {
				EntityData entityData = (EntityData) msg.getMessageData();
				if (comboBox_user1.getValue() == null) return;
				if (entityData.getEntity() instanceof ShopEmployee) {
					shopEmployee = (ShopEmployee) entityData.getEntity();
					textField_shopManagerId.setText(Integer.toString(shopEmployee.getShopManagerId()));
					textField_shopManagerId.setVisible(true);
					textField_branch.setVisible(false);
					label_shopManagerId.setVisible(true);
					label_branch.setVisible(false);
				} else if (entityData.getEntity() instanceof ShopManager) {
					shopManager = (ShopManager) entityData.getEntity();
					textField_branch.setText(shopManager.getName());
					textField_shopManagerId.setVisible(false);
					textField_branch.setVisible(true);
					label_shopManagerId.setVisible(false);
					label_branch.setVisible(true);
				} else {
					textField_shopManagerId.setVisible(false);
					textField_branch.setVisible(false);
					label_shopManagerId.setVisible(false);
					label_branch.setVisible(false);
				}

			} else if (msg.getMessageData() instanceof EntitiesListData) {
				EntitiesListData entityListData = (EntitiesListData) msg.getMessageData();
				arrlist = (ArrayList<IEntity>) entityListData.getEntities();
				ArrayList<String> al = new ArrayList<String>();
				User user = new User();
				for (int i = 0; i < arrlist.size(); i++) {
					user = (User) arrlist.get(i);
					al.add(user.getUserName());
				}
				javafx.application.Platform.runLater(() -> {
					comboBox_user1.getItems().clear();
					list = FXCollections.observableArrayList(al);
					if (list != null) comboBox_user1.setItems(list);
				});
			}

			else if (msg.getMessageData() instanceof RespondMessageData) {
				RespondMessageData res = (RespondMessageData) msg.getMessageData();
				if (res.isSucceed()) {
					if (res.getMessageData() instanceof EntityData) {
						if ((((EntityData) res.getMessageData()).getEntity() instanceof ShopManager)) {
							showAlertMessage("Shop Manger update succeed", AlertType.INFORMATION);
						} else if ((((EntityData) res.getMessageData()).getEntity() instanceof ShopEmployee)) {
							showAlertMessage("Shop Employee update succeed", AlertType.INFORMATION);
						} else if ((((EntityData) res.getMessageData()).getEntity() instanceof User)) {
							showAlertMessage("User update succeed", AlertType.INFORMATION);
						}
						User entity = new User();
						msg = MessagesFactory.createGetAllEntityMessage(entity);
						m_Client.sendMessageToServer(msg);
						selected_user=null;
						textField_password.clear();
						textField_email.clear();
						textField_shopManagerId.clear();
						textField_branch.clear();
					}
				} else if (!res.isSucceed()) {
					showAlertMessage("User update faild please try again", AlertType.WARNING);
					m_Logger.warning("Failed when sending a message to the server.");
				} else {
					m_Logger.warning("Received message data not of the type requested, requested: "
							+ EntitiesListData.class.getName());
				}
			} else {
				m_Logger.warning("Received message data not of the type requested.");
				return;
			}
		}
	}

	// end region -> BaseController Implementation

}
