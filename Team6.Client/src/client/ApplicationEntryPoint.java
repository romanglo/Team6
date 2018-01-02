
package client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import common.UncaughetExceptions;
import controllers.LoginController;
import entities.UserEntity;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logger.LogManager;

/**
 *
 * EntryPoint: The entry point of the application.
 * 
 */
public class ApplicationEntryPoint extends Application
{
	/* Fields region */

	private static Logger s_logger = null;

	/**
	 * Client connection configuration.
	 */
	public static ClientConfiguration ClientConfiguration;

	/**
	 * Client connection management instance.
	 */
	public static Client Client;

	/**
	 * The connected user, can be <code>null</code> if no connected user exists.
	 */
	@Nullable public static UserEntity ConnectedUser = null;

	/* End of --> Fields region */

	/**
	 * Client application entry point
	 *
	 * @param args
	 *            - Application arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

	/* Initializing methods region */

	/**
	 * Method creates connection to the server with the configured IP and port.
	 *
	 * @throws IOException
	 *             - Connection error.
	 * @throws NumberFormatException
	 *             - Conversion error.
	 */
	public static void initializeConnection() throws IOException, NumberFormatException
	{
		Client = new Client(s_logger, ClientConfiguration.getIp(), ClientConfiguration.getPort());
		s_logger.info("Client initialized successfully.");
	}

	/**
	 * Method reads from the configuration file and sets the configuration.
	 *
	 */
	private static void initializeConfiguration()
	{
		ClientConfiguration = client.ClientConfiguration.getInstance();
		if (ClientConfiguration.isDefaultConfiguration()) {
			s_logger.warning("Failed on try to read configuration from \""
					+ client.ClientConfiguration.CONFIGURATION_PATH + "\". Created default configuration.");
		}
		s_logger.config("Client configuration loaded:" + ClientConfiguration.toString());
	}

	private void initializeUncughtExceptionHandler()
	{
		UncaughetExceptions.UncaughtExceptionsHandler uncaughtExceptionsHandler = new UncaughetExceptions.UncaughtExceptionsHandler() {

			@Override
			public void onUncaughtException(Throwable throwable)
			{
				try {
					stop();
				}
				catch (Exception ignored) {

				}
			}
		};
		UncaughetExceptions.startHandling(uncaughtExceptionsHandler, false);
	}

	/* End of --> Initializing methods region */

	/* region Application override methods */

	/**
	 * Method opens the UI of the client.
	 */
	@Override
	public void start(Stage primaryStage)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/Login.fxml"));
			Parent root = (Parent)loader.load();
			LoginController controller = (LoginController)loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setWidth(700);
			primaryStage.setHeight(500);
			primaryStage.setTitle("Zer-Li");
			primaryStage.setResizable(false);
			controller.intializeKeyHandler(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			s_logger.log(Level.SEVERE, "Start failed!", e);
			System.exit(1);
		}
	}

	@Override
	public void init() throws Exception
	{
		s_logger = LogManager.getLogger();

		try {
			initializeUncughtExceptionHandler();
			initializeConfiguration();
			initializeConnection();
		}
		catch (Exception e) {
			s_logger.log(Level.SEVERE, "Initialization failed!", e);
			System.exit(1);
		}
		super.init();
	}

	@Override
	public void stop() throws Exception
	{
		disposeConnection();
		super.stop();
	}

	/* End of --> Application override methods region */

	/* Private disposing methods region */

	/**
	 * Method closes the connection with the server.
	 *
	 */
	private static void disposeConnection()
	{
		if (Client != null) {
			Client.closeConnectionWithServer();
			s_logger.info("Client disposed successfully");
		}
		Client = null;
	}

	/* End of --> Private disposing methods region */
}
