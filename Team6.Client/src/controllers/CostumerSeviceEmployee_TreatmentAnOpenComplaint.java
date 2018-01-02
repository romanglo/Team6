package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import entities.ComplaintEntity;
import entities.CostumerEntity;
import entities.IEntity;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logger.LogManager;
import messages.EntitiesListData;
import messages.Message;
import messages.MessagesFactory;
import client.Client;


/**
 *
 * CompanyEmployeeController :
 * TODO Shimon: Create class description
 * 
 */
public class CostumerSeviceEmployee_TreatmentAnOpenComplaint implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private ImageView imageview_subtitle;
	
	@FXML private TextField financial_compensation; 
	
	@FXML private ComboBox<String> combobox_id;
	
	@FXML private TextArea textarea_complaint;
	
	@FXML private TextArea textarea_summary;
	

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	private List<IEntity> m_complaint_array;
	
	private ArrayList<String> m_id_array;
	
	private ObservableList<String> list;
	/* End of --> Fields region */

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
		initializeComplaint();
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
		InputStream costumerSeviceHeadLine = getClass().getResourceAsStream("/boundaries/images/TreatmentAnOpenComplain.png");
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
	
	private void initializeComplaint()
	{
		ComplaintEntity entity= new ComplaintEntity(); 
		Message msg = MessagesFactory.createGetAllEntityMessage(entity);
		m_client.sendMessageToServer(msg);
	}

	/* End of --> Initializing methods region */
	
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
	 * Turn On the financial compensation text field 
	 *
	 * @param event
	 * 			click on check box.
	 */
	@FXML
	public void financialCompensation(ActionEvent event) 
	{
		if(financial_compensation.isDisable())
		financial_compensation.setDisable(false);
		else
			financial_compensation.setDisable(true);
	}
	
	
	/**
	 * TODO Send message to server with the update complain.
	 *
	 * @param event
	 * 			Update button clicked
	 */
	@FXML
	public void updateClick (ActionEvent event)
	{
		try {
				int id=Integer.parseInt(combobox_id.getPromptText());
				boolean flag=false;
				int i=0;
				while(!(m_complaint_array.isEmpty())||(flag==true))
				{
					ComplaintEntity temp=(ComplaintEntity) m_complaint_array.get(i);
					if(temp.getId()==id)
						flag=true;
					i++;
				}
				i--;
				ComplaintEntity temp =(ComplaintEntity)m_complaint_array.get(i);
				if(textarea_summary.getText().equals(""))
					showInformationMessage("Summary field is empty");
				else
				{
					if(!(financial_compensation.isDisable()))
					{
						if(financial_compensation.getText().equals(""))
							showInformationMessage("Financial compensation is empty");
						else
						{
							temp.setSumerry(textarea_summary.getText());
							temp.getCostumer().setRefund(temp.getCostumer().getCostumerRefunds()+Integer.parseInt(financial_compensation.getText()));
							temp.setActive(false);
						}
					}
					else
					{
						temp.setSumerry(textarea_summary.getText());
						temp.setActive(false);
					}
				}
				Message msg = MessagesFactory.createUpdateEntityMessage(temp); 
				m_client.sendMessageToServer(msg);
			}
		catch (NumberFormatException e) {
			System.out.println("Failed to parse string to integer. Invalid value");
		}
		catch (Exception ex) {
			System.out.println("Error when sending data for update. Exception: " + ex.getMessage());
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

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) 
	{
		if(msg.getMessageData() instanceof EntitiesListData )
		{
			EntitiesListData entitiesListData = (EntitiesListData)msg.getMessageData();
			m_complaint_array = entitiesListData.getEntities();
			for(int i=0;i<m_complaint_array.size();i++)
			{
				ComplaintEntity temp=(ComplaintEntity)(m_complaint_array.get(i));
				m_id_array.add(Integer.toString(temp.getId()));
			}
			 list = FXCollections.observableArrayList(m_id_array);
			combobox_id.setItems(list);
			
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
