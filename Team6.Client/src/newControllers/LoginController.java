
package newControllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import client.ApplicationEntryPoint;
import client.Client;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logger.LogManager;
import newEntities.IEntity;
import newEntities.User;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.LoginData;
import newMessages.Message;
import newMessages.MessagesFactory;

/**
 *
 * LoginController: Create first connection with the server and login to the
 * system, the controller related to 'Login.FXML' and 'LoginSettings.FXML' .
 * 
 */
public class LoginController implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	private @FXML ImageView imageview_background;

	private @FXML ImageView btn_imageview_settings;

	// login fields and buttons :

	private @FXML TextField textField_userName;

	private @FXML PasswordField passwordField_userPassword;

	private @FXML Button btn_settings;

	private @FXML Button btn_login;

	/* End of --> UI Binding Fields region */

	/* Fields */

	private Logger m_logger;

	private Client m_client;

	private Scene m_scene;

	private Stage m_connectingStage;

	private RotateTransition m_settingsButtonRotate;

	private boolean m_canceled;

	/* End of --> Fields region */

	/* UI events region */

	/**
	 * This method is an listener to on action event of login button.
	 *
	 * @param event
	 *            the trigger event.
	 */
	@FXML
	private void onLoginButtonPressed(ActionEvent event)
	{

		String userName = textField_userName.getText().trim();
		String pasword = passwordField_userPassword.getText();

		if ((userName == null || userName.isEmpty()) && (pasword == null || pasword.isEmpty())) {
			showAlertMessage("Please enter your username and password.");
			return;
		}

		if (userName == null || userName.isEmpty()) {
			showAlertMessage("Please enter your username.");
			return;
		}

		if (pasword == null || pasword.isEmpty()) {
			showAlertMessage("Please enter your password.");
			return;
		}

		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);

		if (!m_client.isConnected() && !m_client.createConnectionWithServer()) {
			showAlertMessage("Failed to connect to server! Please check the settings and try again..");
			return;
		}

		displayConnectingWindow();
	}

	/**
	 * This method is an listener to on action event of settings button.
	 *
	 * @param event
	 *            the trigger event.
	 */
	@FXML
	private void onSettingsButtonPressed(ActionEvent event)
	{
		try {
			m_settingsButtonRotate.stop();
			btn_settings.setRotate(0);
			Stage currentStage = (Stage) btn_login.getScene().getWindow();

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/newBoundaries/LoginSettings.fxml"));
			Parent parent = (Parent) fxmlLoader.load();
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/newBoundaries/login.css").toExternalForm());
			Stage nextStage = new Stage();
			nextStage.setScene(scene);
			nextStage.setTitle("Zer-Li Settings");
			nextStage.setResizable(false);
			nextStage.initModality(Modality.WINDOW_MODAL);
			nextStage.initOwner(currentStage);
			InputStream iconResource = getClass().getResourceAsStream("/newBoundaries/images/icon.png");
			if (iconResource != null) {
				Image icon = new Image(iconResource);
				nextStage.getIcons().add(icon);
			}
			setSettingsAnimation(currentStage, nextStage);

			nextStage.showAndWait();
		}
		catch (Exception e) {
			m_logger.log(Level.SEVERE, "Failed on try to load the settings window", e);
			showAlertMessage("The settings can not be changed at this time..");
			return;
		}
	}

	/**
	 * 
	 * This method set fade in animation to window.
	 *
	 * @param currentStage
	 *            the current stage
	 * @param stageToAnimate
	 *            the next stage which will be animated.
	 */
	private void setSettingsAnimation(Stage currentStage, Stage stageToAnimate)
	{

		double screenRightEdge = currentStage.getX() + currentStage.getWidth();
		stageToAnimate.setX(screenRightEdge);
		stageToAnimate.setY(currentStage.getY());
		stageToAnimate.setWidth(0);

		Timeline timeline = new Timeline();

		WritableValue<Double> writableWidth = new WritableValue<Double>() {

			@Override
			public Double getValue()
			{
				return stageToAnimate.getWidth();
			}

			@Override
			public void setValue(Double value)
			{
				stageToAnimate.setX(screenRightEdge - value);
				stageToAnimate.setWidth(value);
			}
		};

		KeyValue kv = new KeyValue(writableWidth, 300d);
		KeyFrame kf = new KeyFrame(Duration.millis(1500), kv);
		timeline.getKeyFrames().addAll(kf);
		timeline.play();
		stageToAnimate.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event)
			{
				Timeline timeline = new Timeline();
				KeyFrame endFrame = new KeyFrame(Duration.millis(1500), new KeyValue(writableWidth, 0.0));
				timeline.getKeyFrames().add(endFrame);
				timeline.setOnFinished(e -> Platform.runLater(() -> stageToAnimate.hide()));
				timeline.play();
				event.consume();
			}
		});

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

		initializeTextFields();

		initialzeButtons();
	}

	/**
	 * The method initialize all the buttons in the controller.
	 */
	private void initialzeButtons()
	{
		btn_login.getStyleClass().add("loginButton");
		btn_settings.getStyleClass().add("settingsButton");

		m_settingsButtonRotate = new RotateTransition(Duration.seconds(2), btn_imageview_settings);
		m_settingsButtonRotate.setByAngle(360);
		m_settingsButtonRotate.setCycleCount(Animation.INDEFINITE);
		m_settingsButtonRotate.setInterpolator(Interpolator.LINEAR);

		btn_settings.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				m_settingsButtonRotate.play();
			}
		});

		btn_settings.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				m_settingsButtonRotate.stop();
				btn_imageview_settings.setRotate(0);
			}
		});
	}

	/**
	 * The method initialize all the text fields in the controller.
	 */
	private void initializeTextFields()
	{
		textField_userName.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (textField_userName.getText().length() > 20) {
					String s = textField_userName.getText().substring(0, 20);
					textField_userName.setText(s);
				}
			}
		});

		passwordField_userPassword.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue)
			{
				if (passwordField_userPassword.getText().length() > 20) {
					String s = passwordField_userPassword.getText().substring(0, 20);
					passwordField_userPassword.setText(s);
				}
			}
		});
	}

	/**
	 * The method initialize all the images in the controller.
	 */
	private void initializeImages()
	{
		InputStream backgoundImage = getClass().getResourceAsStream("/newBoundaries/images/login_background.png");
		if (backgoundImage != null) {
			Image image = new Image(backgoundImage);
			imageview_background.setImage(image);
		}
		InputStream settingsImage = getClass().getResourceAsStream("/newBoundaries/images/settings.png");
		if (settingsImage != null) {
			Image image = new Image(settingsImage);
			btn_imageview_settings.setImage(image);
		}
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
			showAlertMessage("Could not send message to server at the moment,\nplease try again later.");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		if (m_connectingStage != null && m_connectingStage.isShowing()) {
			m_connectingStage.close();
		}
	}

	/* End of --> Implemented methods region */

	/* Private methods region */

	/**
	 * The method show dialog message from {@link Alert} type.
	 *
	 * @param message
	 *            the message to show.
	 */
	private void showAlertMessage(String message)
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
	 * The method perform an actions sequence in case of login failure.
	 *
	 * @param loginData
	 *            The received login data.
	 */
	private void onLoginFailure(LoginData loginData)
	{
		javafx.application.Platform.runLater(() -> {
			passwordField_userPassword.clear();
		});
		m_client.closeConnectionWithServer();
		m_logger.info("Login failed! Login message: " + loginData.toString());
		showAlertMessage("Login Failed! Reason:\n" + loginData.getMessage());
	}

	/**
	 * 
	 * This method try to display the logged on user.
	 *
	 * @param userEntity
	 *            the logged on user.
	 */
	private void showUserWindow(User userEntity)
	{
		URL url = null;

		switch (userEntity.getPrivilege()) {
			case Administrator:
				url = getClass().getResource("/newBoundaries/Administrator.fxml");
			break;

			case CompanyEmployee:
				url = getClass().getResource("/newBoundaries/CompanyEmployee.fxml");
			break;

			case Costumer:
				url = getClass().getResource("/newBoundaries/Costumer.fxml");
			break;

			case CostumerService:
				url = getClass().getResource("/newBoundaries/CostumerServiceEmployee.fxml");
			break;

			case ServiceSpecialist:
				url = getClass().getResource("/newBoundaries/ServiceSpecialist.fxml");
			break;

			case ShopEmployee:
				url = getClass().getResource("/newBoundaries/ShopEmployee.fxml");
			break;

			case ChainManager:
			case ShopManager:
				url = getClass().getResource("/newBoundaries/ShopManager.fxml");
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
		BaseController baseController = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			Parent parent = (Parent) fxmlLoader.load();
			Object controller = fxmlLoader.getController();
			if (controller instanceof BaseController) {
				baseController = (BaseController) controller;
			}
			scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/newBoundaries/application.css").toExternalForm());
		}
		catch (Exception e) {
			String errorString = "Failed on try to load the next scene";
			m_logger.log(Level.SEVERE, errorString, e);
			showAlertMessage(errorString);
			return;
		}
		if (scene != null) {
			javafx.application.Platform.runLater(new NextWindowLoader(scene, baseController));
		}

	}

	/**
	 * This method display an connection window with progress icon.
	 */
	private void displayConnectingWindow()
	{
		if (m_connectingStage == null) {
			Stage currentStage = (Stage) btn_login.getScene().getWindow();

			ProgressIndicator progressIndicator = new ProgressIndicator();
			Label label = new Label();
			label.setStyle(".label " + "{" + " -fx-text-fill: black;" + " -fx-font-weight: bold;"
					+ " -fx-font-size: 20px;" + "}");
			label.setText("Trying to connect ...");
			Button button = new Button();
			button.setStyle(" .button " + "{" + " -fx-background-color: #bfbfbf;" + "	-fx-background-radius: 22;"
					+ "	-fx-text-fill: white;" + "	-fx-border-color: teal;" + "	-fx-border-radius: 22;"
					+ "	-fx-border-width: 3;" + "}" + " .button:hover " + "{" + "     -fx-background-color: white;"
					+ "     -fx-text-fill: teal; " + "}");

			button.setText("Cancel");
			button.setOnAction(event -> {
				m_canceled = true;
				m_connectingStage.close();
			});

			VBox root = new VBox(10, label, progressIndicator, button);
			root.setAlignment(Pos.BASELINE_CENTER);
			Scene scene = new Scene(root, 300, 150);
			m_connectingStage = new Stage();
			m_connectingStage.setScene(scene);
			m_connectingStage.setResizable(false);
			m_connectingStage.initModality(Modality.WINDOW_MODAL);
			m_connectingStage.initStyle(StageStyle.TRANSPARENT);
			m_connectingStage.initOwner(currentStage);
		}
		m_connectingStage.showAndWait();
	}

	/* End of --> Private methods region */

	/* Nested classes region */

	/**
	 * NextWindowLoader: This {@link Runnable} prepare the next window and run it,
	 * must be invoked on MAIN thread.
	 *
	 */
	private class NextWindowLoader implements Runnable
	{

		private Scene m_nextScene;

		private BaseController m_baseController;

		public NextWindowLoader(Scene nextScene, @Nullable BaseController baseController)
		{
			m_nextScene = nextScene;
			m_baseController = baseController;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			Stage nextStage;
			try {
				nextStage = new Stage();
				nextStage.setScene(m_nextScene);
				nextStage.setTitle("Zer-Li");
				InputStream iconResource = LoginController.this.getClass()
						.getResourceAsStream("/newBoundaries/images/icon.png");
				if (iconResource != null) {
					Image icon = new Image(iconResource);
					nextStage.getIcons().add(icon);
				}
				if (m_baseController != null) {
					nextStage.setOnHidden(e -> m_baseController.dispose());
				}
				nextStage.setMinWidth(875);
				nextStage.setMinHeight(600);
				nextStage.show();
			}
			catch (Exception e) {
				String errorString = "Failed on try to load the next window";
				m_logger.severe(errorString + ", excepion: " + e.getMessage());
				showAlertMessage(errorString);
				return;
			}

			Stage stage = (Stage) btn_login.getScene().getWindow();
			if (stage != null) {
				stage.close();
			} else {
				btn_login.getScene().getWindow().hide();
			}
		}

		/* End of --> Nested classes region */
	}
}
