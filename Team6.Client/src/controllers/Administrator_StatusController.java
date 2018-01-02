
package controllers;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.IEntity;
import entities.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
public class Administrator_StatusController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private Button update_btn;
	
	@FXML private Button back_btn;
	
	@FXML private ChoiceBox<String> usercmb;
	
	@FXML private ChoiceBox<String> cmb_status;
	
	@FXML private ChoiceBox<String> cmb_privillge;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	ObservableList<String> list;
	
	ArrayList<IEntity> arrlist;


	
	String[] statuses = {"Actived", "Blocked"};	/* End of --> Fields region */

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
		ArrayList<String> temp = new ArrayList<>();
		UserEntity entity= new UserEntity();
		Message msg= MessagesFactory.createGetAllEntityMessage(entity);
		m_client.sendMessageToServer(msg);
		for(int i=0;i<arrlist.size();i++)
		{
			UserEntity tempEntity = (UserEntity) arrlist.get(i);
			temp.add(tempEntity.getUserName());	
		}
		list = FXCollections.observableArrayList(temp);
		cmb_privillge.setItems(list);
		
		
	}

	private void initializePrivillge()
	{
		// TODO раам: Auto-generated method stub
		ArrayList<String> al = new ArrayList<String>();	
		al.add("CompanyEmployee");
		al.add("ShopManager");
		al.add("ChainManager");
		al.add("Administrator");
		al.add("ShopEmployee");
		al.add("CostumerService");
		al.add("Costumer");
		al.add("ServiceSpecialist");
		list = FXCollections.observableArrayList(al);
		cmb_privillge.setItems(list);
	}

	private void initializeStatus()
	{
		// TODO раам: Auto-generated method stub
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Blocked");
		al.add("Actived");
		list = FXCollections.observableArrayList(al);
		cmb_status.setItems(list);
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
	 * TODO раам: Auto-generated comment stub - Change it!
	 * @param event 
	 *
	 * @param e
	 * @throws IOException 
	 */
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */
	
	public void backbtn (ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator.fxml").openStream());
		AdministratorController AdministratorController  = loader.getController();
		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Administrator Controller");
		primaryStage.show();
		
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		if(msg.getMessageData() instanceof ArrayList<?>)
		{
		arrlist = (ArrayList<IEntity>) msg.getMessageData();
		}
		//message
		//message-> Data
		//message data cast arrtlist<IEntity>
		
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
