
package client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import common.UncaughetExceptions;
import controllers.LoginController;
import entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.LogManager;
import messages.Message;
import messages.MessagesFactory;

/**
 *
 * EntryPoint: The entry point of the application.
 * 
 */
public class ApplicationEntryPoint extends Application
{

	// region Static Fields

	private final static String s_lockFilePath = "ClientLockFile.lock";

	private static File s_file;

	private static FileChannel s_fileChannel;

	private static FileLock s_lockFile;

	// end region -> Static Fields

	/**
	 * The main method of the application, the method ensure only one running
	 * instance of the application, using file lock pattern. *
	 * 
	 * @param args
	 *            Application arguments.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		try {
			s_file = new File(s_lockFilePath);

			// Delete the lock file if it exist.
			if (s_file.exists()) {
				s_file.delete();
			}

			// Try to get the file lock
			s_fileChannel = new RandomAccessFile(s_file, "rw").getChannel();

			// Set to lock file hidden attribute
			Files.setAttribute(Paths.get(s_lockFilePath), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

			// Try to get the file lock
			s_lockFile = s_fileChannel.tryLock();
			if (s_lockFile == null) {
				// File is locked by other application
				s_fileChannel.close();
				System.out.println("Only one instance of Server can run at the same time.");
				System.exit(1);
			}

			// Add shutdown hook to release lock when application shutdown
			Thread shutdownHook = new Thread(new Runnable() {

				@Override
				public void run()
				{
					try {
						if (s_lockFile != null) {
							s_lockFile.release();
							s_fileChannel.close();
							s_file.delete();
						}
					}
					catch (IOException e) {
						System.err.println("An error occurred on try to release and delete instance locks, exception: "
								+ e.getMessage());
					}
				}
			});

			Runtime.getRuntime().addShutdownHook(shutdownHook);

			// Launch the application
			launch(args);
		}
		catch (IOException e) {
			System.err.println("An error occurred on try to start 'Server' process, exception: " + e.getMessage());
			System.exit(1);
		}
	}

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
	@Nullable public static User ConnectedUser = null;

	/* End of --> Fields region */

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

	/**
	 * The method initialize shutdown sequence in case of uncaught exception.
	 * 
	 * @see UncaughetExceptions
	 */
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
		UncaughetExceptions.startHandling(uncaughtExceptionsHandler, true);
	}

	/* End of --> Initializing methods region */

	/* region Application override methods */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/Login.fxml"));
			Parent root = (Parent) loader.load();
			LoginController controller = (LoginController) loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/boundaries/login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setWidth(700);
			primaryStage.setHeight(500);
			primaryStage.setTitle("Zer-Li");
			primaryStage.setResizable(false);
			controller.intializeKeyHandler(scene);

			InputStream iconResource = getClass().getResourceAsStream("/boundaries/images/icon.png");
			if (iconResource != null) {
				Image icon = new Image(iconResource);
				primaryStage.getIcons().add(icon);
			}

			primaryStage.show();
		}
		catch (Exception e) {
			s_logger.log(Level.SEVERE, "Start failed!", e);
			System.exit(1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws Exception
	{
		try {
			disconnectUser();
			disposeConnection();
		}
		catch (Exception e) {
			s_logger.log(Level.SEVERE, "Stopping Failed! ", e);
		}

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
			Client.setClientStatusHandler(null);
			Client.setMessagesHandler(null);

			Client.closeConnectionWithServer();
			s_logger.info("Client disposed successfully");
		}
		Client = null;
	}

	/**
	 * 
	 * The method send disconnection message to the server as part of the shutdown
	 * sequence.
	 *
	 */
	private void disconnectUser()
	{
		if (ConnectedUser != null && Client != null) {
			if (!Client.isConnected()) {
				if (!Client.createConnectionWithServer()) {
					s_logger.severe("Failed on try to create connection with server for send user log out message!");
					return;
				}
			}
			Message logoutMessage = MessagesFactory.createLogoutMessage(ConnectedUser.getUserName(),
					ConnectedUser.getPassword());
			Client.sendMessageToServer(logoutMessage);
		}
	}

	/* End of --> Private disposing methods region */
}
