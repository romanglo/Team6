
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.sun.media.jfxmediaimpl.platform.Platform;

import client.ApplicationEntryPoint;
import client.Client;
import newEntities.IEntity;
import newEntities.User;
import javafx.animation.RotateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logger.LogManager;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.LoginData;
import newMessages.Message;
import newMessages.MessagesFactory;

/**
 *
 * LoginController: Create first connection with the server and login to the
 * system.
 * 
 */
public class LoginController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	@FXML private ImageView imageview_background;

	@FXML private ImageView imageview_usericon;

	@FXML private ImageView btn_imageview_settings;

	// login fields and buttons :

	@FXML private TextField textField_userName;

	@FXML private PasswordField passwordField_userPassword;

	@FXML private Button btn_login;

	/* End of --> UI Binding Fields region */

	/* Fields */

	private Logger m_logger;

	private Client m_client;

	private Scene m_scene;

	private Stage m_connectingStage;

	private boolean m_canceled;
	/* End of --> Fields region */

	/* UI events region */

	@FXML
	private void onLoginButtonPressed(ActionEvent event)
	{

		String userName = textField_userName.getText().trim();
		String pasword = passwordField_userPassword.getText();

		if ((userName == null || userName.isEmpty()) && (pasword == null || pasword.isEmpty())) {
			showInformationMessage("Please enter your username and password.");
			return;
		}

		if (userName == null || userName.isEmpty()) {
			showInformationMessage("Please enter your username.");
			return;
		}

		if (pasword == null || pasword.isEmpty()) {
			showInformationMessage("Please enter your password.");
			return;
		}

		if (!m_client.isConnected() && !m_client.createConnectionWithServer()) {
			showInformationMessage("Failed to connect to server! Please check the settings and try again..");
			return;
		}

		displayConnectingWindow();

	}

	@FXML
	private void onSettingsButtonPressed(ActionEvent event)
	{
		try {
			Stage currentStage = (Stage) btn_login.getScene().getWindow();

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/boundaries/LoginSettings.FXML"));
			Parent parent = (Parent) fxmlLoader.load();
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
			Stage nextStage = new Stage();
			nextStage.setScene(scene);
			nextStage.setTitle("Zer-Li Settings");
			nextStage.setResizable(false);
			nextStage.initModality(Modality.WINDOW_MODAL);
			nextStage.initOwner(currentStage);
			nextStage.showAndWait();
		}
		catch (Exception e) {
			m_logger.severe("Failed on try to load the settings window" + ", excepion: " + e.getMessage());
			showInformationMessage("The settings can not be changed at this time..");
			return;
		}
	}

	/* End of --> UI events region */

	/**
	 * 
	 * This method should be called by this stage creator! The method set listener
	 * to key events..
	 *
	 * @param thisScene
	 *            The scene of this stage.
	 */
	public void intializeKeyHandler(Scene thisScene)
	{

		m_scene = thisScene;
		thisScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			final KeyCombination enterCombination = new KeyCodeCombination(KeyCode.ENTER);

			@Override
			public void handle(KeyEvent event)
			{
				if (enterCombination.match(event)) {
					btn_login.fire();
				}
			}
		});
	}

	/* Initializing methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		m_canceled = false;
		m_logger = LogManager.getLogger();
		m_client = ApplicationEntryPoint.Client;

		initializeImages();

		initializeClientHandler();
	}

	private void initializeImages()
	{
		InputStream backgoundImage = getClass().getResourceAsStream("/boundaries/images/login_background.jpg");
		if (backgoundImage != null) {
			Image image = new Image(backgoundImage);
			imageview_background.setImage(image);
		}
		InputStream userImage = getClass().getResourceAsStream("/boundaries/images/default_user.png");
		if (userImage != null) {
			Image image = new Image(userImage);
			imageview_usericon.setImage(image);
		}
		InputStream settingsImage = getClass().getResourceAsStream("/boundaries/images/settings.png");
		if (settingsImage != null) {
			Image image = new Image(settingsImage);
			btn_imageview_settings.setImage(image);
		}
	}

	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);

	}

	/* End of --> Initializing methods region */

	/* Implemented methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg) throws Exception
	{
		if (m_canceled) {
			m_logger.info("Received response to login requset but the login progress canceled by the user.");
			m_canceled = false;
			return;
		}
		
		javafx.application.Platform.runLater(() -> m_connectingStage.close());

		IMessageData messageData = msg.getMessageData();

		if (messageData instanceof LoginData) {
			onLoginFailure((LoginData) messageData);
			return;
		}

		if (!(messageData instanceof EntityData)) {
			m_logger.warning("Received not expected MessageData! Expected : EntityData, received "
					+ messageData.getClass().getName());
			return;
		}

		IEntity entity = ((EntityData) messageData).getEntity();
		if (!(entity instanceof User)) {
			m_logger.warning(
					"Received not expected IEntity! Expected : UserEntity, received " + entity.getClass().getName());
			return;
		}

		User user = (User) entity;
		m_logger.info("An user connected successfully! User details: " + user.toString());
		showUserWindow(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{

		try {
			String userName = textField_userName.getText().trim();
			String pasword = passwordField_userPassword.getText();

			Message msg = MessagesFactory.createLoginMessage(userName, pasword);
			m_client.sendMessageToServer(msg);
		}
		catch (Exception ex) {
			m_logger.warning("Error when sending get request, excpetion: " + ex.getMessage());
			showInformationMessage("Could not send message to server at the moment,\nplease try again later.");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		// TODO ROMAN : check if try to connect in this moment and back to regular
		// window.
	}

	/* End of --> Implemented methods region */

	/* Private methods region */

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

	private void onLoginFailure(LoginData loginData)
	{
		javafx.application.Platform.runLater(() -> {
			passwordField_userPassword.clear();
		});
		m_client.closeConnectionWithServer();
		m_logger.info("Login failed! Login message: " + loginData.toString());
		showInformationMessage("Login Failed! Reason:\n" + loginData.getMessage());
	}

	private void showUserWindow(User userEntity)
	{
		URL url = null;

		switch (userEntity.getPrivilege()) {
			case Administrator:
				url = getClass().getResource("/boundaries/Administrator.FXML");
			break;

			case ChainManager:
				url = getClass().getResource("/boundaries/ChainManager.FXML");
			break;

			case CompanyEmployee:
				url = getClass().getResource("/boundaries/CompanyEmployee.FXML");
			break;

			case Costumer:
				url = getClass().getResource("/boundaries/Costumer.FXML");
			break;

			case CostumerService:
				url = getClass().getResource("/boundaries/CostumerServiceEmployee.FXML");
			break;

			case ServiceSpecialist:
				url = getClass().getResource("/boundaries/ServiceSpecialist.FXML");
			break;

			case ShopEmployee:
				url = getClass().getResource("/boundaries/ShopEmployee.FXML");
			break;

			case ShopManager:
				url = getClass().getResource("/boundaries/ShopManager.FXML");
			break;

			default:
				m_logger.warning("Received unreconized user privilege.");
				return;
		}

		m_client.setClientStatusHandler(null);
		m_client.setMessagesHandler(null);
		m_scene.setOnKeyPressed(null);

		ApplicationEntryPoint.ConnectedUser = userEntity;

		Scene scene = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			Parent parent = (Parent) fxmlLoader.load();
			scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
		}
		catch (Exception e) {
			String errorString = "Failed on try to load the next scene";
			m_logger.severe(errorString + ", excepion: " + e.getMessage());
			showInformationMessage(errorString);
			return;
		}
		if (scene != null) {
			javafx.application.Platform.runLater(new NextWindowLoader(scene));
		}

	}

	private void displayConnectingWindow()
	{
		if (m_connectingStage == null) {
			Stage currentStage = (Stage) btn_login.getScene().getWindow();

			ProgressIndicator progressIndicator = new ProgressIndicator();
			Label label = new Label("Trying to connect ...");

			Button button = new Button();
			button.setText("Cancel");
			button.setOnAction(event -> {
				m_canceled = true;
				m_connectingStage.close();
			});

			VBox root = new VBox(10, label, progressIndicator, button);
			root.setAlignment(Pos.BASELINE_CENTER);
			Scene scene = new Scene(root, 300, 150);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
			m_connectingStage = new Stage();
			m_connectingStage.setScene(scene);
			m_connectingStage.setTitle("Connecting");
			m_connectingStage.setResizable(false);
			m_connectingStage.initModality(Modality.WINDOW_MODAL);
			m_connectingStage.initStyle(StageStyle.TRANSPARENT);
			m_connectingStage.initOwner(currentStage);
		}
		m_connectingStage.showAndWait();
	}
	
	/* End of --> Private methods region */

	/* Nested classes region */

	private class NextWindowLoader implements Runnable
	{

		private Scene m_nextScene;

		public NextWindowLoader(Scene nextScene)
		{
			m_nextScene = nextScene;
		}

		@Override
		public void run()
		{
			Stage nextStage;
			try {
				nextStage = new Stage();
				nextStage.setScene(m_nextScene);
				nextStage.setTitle("Zer-Li");
				nextStage.show();
			}
			catch (Exception e) {
				String errorString = "Failed on try to load the next window";
				m_logger.severe(errorString + ", excepion: " + e.getMessage());
				showInformationMessage(errorString);
				return;
			}

			Stage stage = (Stage) btn_login.getScene().getWindow();
			if (stage != null) {
				stage.close();
			} else {
				btn_login.getScene().getWindow().hide();
			}
		}
	}
	
	/* End of --> Nested classes region */

}
