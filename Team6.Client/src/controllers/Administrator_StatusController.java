
package controllers;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import newEntities.EntitiesEnums;
import newEntities.IEntity;
import newEntities.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntitiesListData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * CompanyEmployeeController : TODO Naal: Create class description
 * 
 */
public class Administrator_StatusController
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private Button update_btn;

	@FXML private Button back_btn;

	@FXML private ComboBox<String> cmb_user;

	@FXML private ComboBox<String> cmb_status;

	@FXML private ComboBox<String> cmb_privillge;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

	User tempEntity;

	ObservableList<String> list;

	ArrayList<IEntity> arrlist;

	ArrayList<String> userNameArr = new ArrayList<String>();

	private User selected_user;

	String[] statuses = { "disconnected", "Blocked" }; /* End of --> Fields region */

	/* UI events region */

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
		initializeStatus();
		initializePrivillge();
		initializeUsers();
	}

	private void initializeUsers()
	{
		User tempEntity = new User();
		Message msg = MessagesFactory.createGetAllEntityMessage(tempEntity);
		m_client.sendMessageToServer(msg);
	}

	/*
	 * private void initializePrivillge() {
	 * 
	 * ArrayList<String> al = new ArrayList<String>(); for(int i = 0 ; i <
	 * userNameArr.size();i++) { al.add(userNameArr.get(i)); } list =
	 * FXCollections.observableArrayList(al); cmb_privillge.setItems(list); }
	 */

	private void initializeStatus()
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add("Blocked");
		al.add("Actived");
		list = FXCollections.observableArrayList(al);
		cmb_status.setItems(list);
	}

	private void initializePrivillge()
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add("administrator");
		al.add("costumer");
		al.add("shop employee");
		al.add("chain manger");
		al.add("shop manger");
		al.add("service specialist");
		al.add("company employee");
		al.add("costumer service");
		list = FXCollections.observableArrayList(al);
		cmb_privillge.setItems(list);
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

	/**
	 * TODO ����: Auto-generated comment stub - Change it!
	 * 
	 * @param event
	 *
	 * @param e
	 * @throws IOException
	 */
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */

	public void backbtn(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator.fxml").openStream());
		AdministratorController AdministratorController = loader.getController();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle("Administrator Controller");
		primaryStage.show();

	}

	/**
	 * TODO ����: Auto-generated comment stub - Change it!
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void updatebtn(ActionEvent event) throws IOException
	{
		String username = cmb_user.getValue();
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) selected_user = (User) arrlist.get(i);
		}

		String privilege = cmb_privillge.getValue();
		switch (privilege) {
			case "administror":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Administrator);
			break;

			case "costumer":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.Costumer);
			break;

			case "shop employee":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopEmployee);
			break;

			case "chain manager":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ChainManager);
			break;

			case "shop manager":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ShopManager);
			break;

			case "service specialist":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.ServiceSpecialist);
			break;

			case "company employee":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CompanyEmployee);
			break;

			case "costumer service":
				selected_user.setPrivilege(EntitiesEnums.UserPrivilege.CostumerService);
			break;
		}

		String status = cmb_status.getValue();
		switch (status) {
			case "Connected":
				selected_user.setStatus(EntitiesEnums.UserStatus.Connected);
			break;

			case "Disconnected":
				selected_user.setStatus(EntitiesEnums.UserStatus.Disconnected);
			break;

			case "Blocked":
				selected_user.setStatus(EntitiesEnums.UserStatus.Blocked);
			break;
		}
		Message msg=MessagesFactory.createUpdateEntityMessage(selected_user);
		m_client.sendMessageToServer(msg);

	}

	/**
	 * TODO ����: Auto-generated comment stub - Change it!
	 *
	 * @param event
	 * @throws IOException
	 */

	@FXML
	public void userSelected(ActionEvent event) throws IOException
	{
		String username = cmb_user.getValue();
		for (int i = 0; i < arrlist.size(); i++) {
			if (((User) arrlist.get(i)).getUserName().equals(username)) selected_user = (User) arrlist.get(i);
		}
		cmb_privillge.setValue((selected_user.getPrivilege().toString()));
		cmb_status.setValue(selected_user.getStatus().toString());
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		{
			if (msg.getMessageData() instanceof EntitiesListData) {
				EntitiesListData entityListData = (EntitiesListData) msg.getMessageData();
				arrlist = (ArrayList<IEntity>) entityListData.getEntities();
				for (int i = 0; i < arrlist.size(); i++) {
					User tempEntity = (User) arrlist.get(i);
					userNameArr.add(tempEntity.getUserName());
				}
				list = FXCollections.observableArrayList(userNameArr);
				cmb_user.setItems(list);
			} else if (msg.getMessageData() instanceof RespondMessageData) {
				RespondMessageData res = (RespondMessageData) msg.getMessageData();
				if (res.isSucceed()) {
					showInformationMessage("User update successed");
				} else {
					showInformationMessage("User update faild please try again");
				}
			}
		}

	}

	private void showInformationMessage(String message)
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
