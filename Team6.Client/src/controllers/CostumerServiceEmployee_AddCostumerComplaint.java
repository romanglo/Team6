
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import newEntities.Complaint;
import newEntities.Costumer;
import newEntities.IEntity;
import newEntities.ShopManager;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;


/**
 *
 * CostumerServiceEmployee_AddCostumerComplaint :
 * CostumerServiceEmployee_AddCostumerComplaint include:
 *				  Add Costumer Complaint UI methods.
 *
 * 
 * 
 */
public class CostumerServiceEmployee_AddCostumerComplaint implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private ImageView imageview_subtitle;
	
	@FXML private TextField IDField;
	
	@FXML private TextArea textarea_costumercomplaint;
	
	@FXML private AnchorPane anchorpane_root;
	
	@FXML private ComboBox<Integer> combobox_shop;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	private List<newEntities.IEntity> m_shopmanager_array;
	
	private ArrayList<Integer> m_managerid_array=new ArrayList<>();
	
	private ObservableList<Integer> list;
	
	private List<IEntity> m_costumer_array;
	
	private Complaint selected_complaint;
	/* End of --> Fields region */

	/* UI events region */
	
	/**
	 * The function back to costumer service employee main window.
	 *
	 * @param event
	 * 			Back button clicked
	 */
	@FXML
	public void backButtonClick(ActionEvent event) 
	{
		try 
		{
		((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmployee.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		primaryStage.setScene(scene);		
		primaryStage.show();
		}
		catch(Exception e)
		{
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}
	}
	
	
	/**
	 * The function checks that the fields aren't empty,
	 * and call the function "setNewComplaint" that sends data to server.
	 *
	 * @param event
	 * 			Save button clicked.
	 */
	@FXML
	public void saveButtonClick(ActionEvent event)
	{
		if(IDField.getText().equals("")||textarea_costumercomplaint.getText().equals(""))
		{
		showInformationMessage("One or more of the fileds are empty");
		}
		else
		{
			selected_complaint= new Complaint();
			selected_complaint.setShopManagerId(combobox_shop.getValue());
			selected_complaint.setComplaint(textarea_costumercomplaint.getText());
			int costumer_id=Integer.parseInt(IDField.getText());
			Costumer entity=new Costumer();
			entity.setId(costumer_id);
			Message msg= MessagesFactory.createGetEntityMessage(entity);
			m_client.sendMessageToServer(msg);
			combobox_shop.setDisable(true);
			combobox_shop.getItems().clear();
			m_shopmanager_array.clear();
			m_managerid_array.clear();
			initializeShopCombobox();
			combobox_shop.setDisable(false);
			textarea_costumercomplaint.clear();
			IDField.clear();
		}
	}
	
	
	/* End of --> UI events region */

	// region Private Methods
	
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
	
	/* End of --> Private Methods region */

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
		initializeShopCombobox();
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
		InputStream costumerSeviceHeadLine = getClass().getResourceAsStream("/boundaries/images/AddCostumerComplaint.png");
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
	
	private void initializeShopCombobox()
	{
		ShopManager entity= new ShopManager();
		Message msg=MessagesFactory.createGetAllEntityMessage(entity);
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
			Costumer entity=(Costumer)((EntityData)msg.getMessageData()).getEntity();
			int id=entity.getId();
			selected_complaint.setCostumerId(id);
			selected_complaint.setCreationDate(new Date());
			selected_complaint.setOpened(true);
			msg=MessagesFactory.createAddEntityMessage(selected_complaint);
			m_client.sendMessageToServer(msg);
		}
		else if (msg.getMessageData() instanceof RespondMessageData)
			{
				if(((RespondMessageData)msg.getMessageData()).getMessageData() instanceof EntityData)
				{
					if(((EntityData)((RespondMessageData)msg.getMessageData()).getMessageData()).getEntity() instanceof Costumer)
						{
							m_logger.severe("The costumer didn't exist");
						}
				}
				else
				{
					if(((RespondMessageData)msg.getMessageData()).isSucceed())
					{
						m_logger.severe("Successfully added complaint");
					}
				}
			}
			else if(msg.getMessageData() instanceof EntitiesListData)
			{
				m_shopmanager_array=((EntitiesListData)msg.getMessageData()).getEntities();
				for(int i=0;i<m_shopmanager_array.size();i++)
				{
					m_managerid_array.add(((ShopManager)m_shopmanager_array.get(i)).getId());
				}
				list = FXCollections.observableArrayList(m_managerid_array);
				combobox_shop.setItems(list);
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
