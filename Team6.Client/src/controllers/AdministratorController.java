
package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;


/**
 *
 * CompanyEmployeeController :
 * TODO Naal: Create class description
 * 
 */
public class AdministratorController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	
	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;
	
	@FXML private Button change_Coordinates;
	
	@FXML private Button update_Status;
	
	@FXML private Button exit_System;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;
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
	 *
	 * @param e
	 * @throws IOException 
	 */
	/* End of --> Initializing methods region */

	/* Client handlers implementation region */
	
	public void changeCustomerCoordinates (ActionEvent e) throws IOException
	{
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator_Coordinates.fxml").openStream());
		@SuppressWarnings("unused") Administrator_CoordinatesController AdministratorCoordinatesController  = loader.getController();
		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Change User Coordinatse");
		primaryStage.show();
		
	}
	
	/**
	 * TODO раам: Auto-generated comment stub - Change it!
	 *
	 * @param e
	 * @throws IOException
	 */
	@SuppressWarnings("javadoc") public void updateUserStatus (ActionEvent e) throws IOException
	{
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator_Update.fxml").openStream());
		Administrator_StatusController Administrator_StatusController  = loader.getController();
		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Change User Status & Privillige");
		primaryStage.show();
		
	}
	
	/**
	 *
	 * @param event
	 * @throws IOException
	 */
	public void closeWindow(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
	}
	
	/**
	 * TODO раам: Auto-generated comment stub - Change it!
	 *
	 * @param e
	 * @throws IOException
	 */
	/*@SuppressWarnings("javadoc") public void coordinatesUpdate (ActionEvent e) throws IOException
	{
		((Node)e.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/boundaries/Administrator_Coordinates.fxml").openStream());
		Administrator_CoordinatesController Administrator_CoordinatesController  = loader.getController();
		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Change User Status & Privillige");
		primaryStage.show();
		
	}*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		// TODO Shimon : Add event handling
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
