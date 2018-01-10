
package controllers;

import java.awt.Label;
import java.awt.TextField;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import newEntities.IEntity;
import newEntities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logger.LogManager;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;



/**
 *
 * CompanyEmployeeController :
 * TODO Naal: Create class description
 * 
 */
public class Administrator_CoordinatesController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private javafx.scene.control.TextField username;

//	@FXML private TextField customerId;

	@FXML private javafx.scene.control.TextField email;

	@FXML private javafx.scene.control.TextField password;
	
	@FXML private Label customerIdLabel;
	
	@FXML private Label usernameLabel;
	
	@FXML private Label emailLabel;
	
	@FXML private Label passwordLabel;
	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
	
	//private User userEntity;
	
	ArrayList<IEntity> arrlist;

	User tempEntity;
	
	User userEntity;
	/* End of --> Fields region */
	
	/* Private methods region*/
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
	/* End of --> Private methods reegion*/

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


	/* End of --> Initializing methods region */

	/* Client handlers implementation region */
	/**
	 * TODO раам: Auto-generated comment stub - Change it!
	 *
	 * @param event : get button pressed, send message to server to return user information
	 */
	public void getBtn(ActionEvent event)
	{
		User tempEntity = new User();
		Message msg= MessagesFactory.createGetAllEntityMessage(tempEntity);
		m_client.sendMessageToServer(msg);
	}
	/**
	 *
	 * @param event : back button pressed 
	 * 
	 * @throws IOException :
	 */
	public void backbtn (ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator.fxml").openStream());		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Administrator Controller");
		primaryStage.show();
		
	}
	
	/**
	 * @param event : update button pressed
	 * @throws IOException : 
	 *
	 */
	public void updatebtn (ActionEvent event) throws IOException
	{
		if(email.getText().equals(""))
			showInformationMessage("Please enter email.");
		else if(password.getText().equals(""))
			showInformationMessage("Please enter password.");
		else if (username.getText().equals(""))
			showInformationMessage("Please enter username to edit.");
		else
		{
			tempEntity.setEmail(email.getText());
			tempEntity.setPassword(password.getText());
			Message entityMessage = MessagesFactory
					.createUpdateEntityMessage(tempEntity);
			m_client.sendMessageToServer(entityMessage);
		}


	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
			if(msg.getMessageData() instanceof EntitiesListData)
			{
			EntitiesListData entityListData=(EntitiesListData) msg.getMessageData();
			arrlist = (ArrayList<IEntity>) entityListData.getEntities();
			String name = username.getText();
			if(name.equals("")) 
			{
				showInformationMessage("Please enter username");
			}
			for(int i=0;i<arrlist.size();i++)
			{
				tempEntity = (User) arrlist.get(i);
				if(tempEntity.getUserName().equals(name))
				{
					userEntity=tempEntity;
					break;
				}
					
			}
			if(userEntity!=null) 
				{
					email.setText(tempEntity.getEmail());
					password.setText(tempEntity.getPassword());	
				}
			else if(!name.equals(""))
				{
					showInformationMessage("User not exist!");
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
						m_logger.warning("Failed when sending a message to the server.");
					}else
					{
						m_logger.warning(
								"Received message data not of the type requested, requested: " + EntitiesListData.class.getName());
					}
				}
				else
					{
						m_logger.warning("Received message data not of the type requested.");
						return;
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
