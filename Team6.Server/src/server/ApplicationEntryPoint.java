package server;

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

import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.LogManager;
import common.UncaughetExceptions;

/**
 * Server application entry point.
 *
 */
public class ApplicationEntryPoint extends Application {

	// region Static Fields

	private final static String s_lockFilePath = "ServerLockFile.lock";
	private static File s_file;
	private static FileChannel s_fileChannel;
	private static FileLock s_lockFile;

	// end region -> static Fields.

	/**
	 * 
	 * The main method of the application, the method ensure only one running
	 * instance of the application, using file lock pattern.
	 *
	 * @param args
	 *            Application arguments.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
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
				public void run() {
					try {
						if (s_lockFile != null) {
							s_lockFile.release();
							s_fileChannel.close();
							s_file.delete();
						}
					} catch (IOException e) {
						System.err.println("An error occurred on try to release and delete instance locks, exception: "
								+ e.getMessage());
					}
				}
			});

			Runtime.getRuntime().addShutdownHook(shutdownHook);

			// Launch the application
			launch(args);
		} catch (IOException e) {
			System.err.println("An error occurred on try to start 'Server' process, exception: " + e.getMessage());
			System.exit(1);
		}
	}

	// region Fields

	private Logger m_logger;

	private ServerConfiguration m_serverConfiguration;

	private ServerScheduledExecutor m_serverScheduledExecutor;

	/**
	 * Application Database Controller.
	 */
	public static DbController DbContoller;

	/**
	 * Application Server Controller.
	 */
	public static Server Server;

	// end region -> Fields

	// region Application Method Overrides

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerXML.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root, 450, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(450);
			primaryStage.setMinHeight(500);
			primaryStage.setTitle("Zer-Li Server");
			m_serverScheduledExecutor.Start();

			InputStream iconResource = getClass().getResourceAsStream("icon.png");
			if (iconResource != null) {
				Image icon = new Image(iconResource);
				primaryStage.getIcons().add(icon);
			}
			
			primaryStage.show();
		} catch (Exception ex) {
			m_logger.log(Level.SEVERE, "UI start failed!", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws Exception {
		try {
			m_logger = LogManager.getLogger();
			initializeUncaughtExceptionHandler();
			initializeConfiguration();
			initializeDbController();
			intializeServer();
		} catch (Exception ex) {
			m_logger.log(Level.SEVERE, "Initialization failed!", ex);
		}
		super.init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws Exception {

		try {
			disposeScheduledExecutor();
			disposeDbController();
			disposeServer();
			disposeConfiguration();
		} catch (Exception ex) {
			m_logger.log(Level.SEVERE, "Disposing failed!", ex);
		}
		m_logger = null;
		m_serverConfiguration = null;

		super.stop();
	}

	/**
	 * 
	 * The method dispose the {@link ServerConfiguration} and also try to update
	 * {@link ServerConfiguration} resource file.
	 *
	 */
	private void disposeConfiguration() {
		if (m_serverConfiguration.updateResourceFile()) {
			m_logger.info("Configuration resource file updated successfully! Saved configuration: "
					+ m_serverConfiguration.toString());
		} else {
			m_logger.info(
					"Failed on try to updated configuration resource file, for more information look up in the log. Configuration: "
							+ m_serverConfiguration.toString());
		}
	}

	/**
	 * 
	 * The method call first time to {@link ServerConfiguration#getInstance()}} and
	 * initialize the {@link ServerConfiguration}.
	 *
	 */
	private void initializeConfiguration() {
		m_serverConfiguration = ServerConfiguration.getInstance();
		if (m_serverConfiguration.isDefaultConfiguration()) {
			m_logger.warning("Failed on try to read configuration from \"" + ServerConfiguration.CONFIGURATION_PATH
					+ "\". Created default configuration");

		}
		m_logger.info("Server configuration loaded:" + m_serverConfiguration.toString());
	}

	// end region -> Application Method Overrides

	// region Private Initialize Methods

	/**
	 * The method initialize shutdown sequence in case of uncaught exception.
	 * 
	 * @see UncaughetExceptions
	 */
	private void initializeUncaughtExceptionHandler() {
		UncaughetExceptions.UncaughtExceptionsHandler uncaughtExceptionsHandler = new UncaughetExceptions.UncaughtExceptionsHandler() {
			@Override
			public void onUncaughtException(Throwable throwable) {
				try {
					stop();
				} catch (Exception ignored) {

				}
			}
		};
		UncaughetExceptions.startHandling(uncaughtExceptionsHandler, false);
	}

	/**
	 * The method create instance of {@link Server} using
	 * {@link ServerConfiguration}.
	 */
	private void intializeServer() {
		Server = new Server(m_logger, m_serverConfiguration.getConnectivityConfiguration().getPort());
		m_logger.info("Server instance created successfully.");
	}

	/**
	 * The method create instance of {@link DbController} using
	 * {@link ServerConfiguration}.
	 */
	private void initializeDbController() {
		DbContoller = new DbController(m_logger, m_serverConfiguration.getDbConfiguration());
		m_serverScheduledExecutor = new ServerScheduledExecutor(DbContoller);
		m_logger.info("Database instance created successfully.");
	}

	// end region -> Private Initialize Methods

	// region -> Private Dispose Methods

	/**
	 * 
	 * The method close connection if opened and dispose {@link Server}.
	 *
	 * @throws IOException
	 *             if an I/O error occurs while closing the server socket.
	 */
	private void disposeServer() throws IOException {
		if (Server != null) {
			Server.close();
		}
		m_logger.info("Server disposed successfully.");
		Server = null;
	}

	/**
	 * 
	 * The method close connection with {@link DbController}.
	 *
	 */
	private void disposeDbController() {
		if (DbContoller != null && DbContoller.isRunning()) {
			DbContoller.Stop();
		}
		m_logger.info("Database controller disposed successfully.");
		DbContoller = null;
	}

	/**
	 * The method shutdown the {@link ServerScheduledExecutor}.
	 *
	 */
	private void disposeScheduledExecutor() {
		if (m_serverScheduledExecutor != null && m_serverScheduledExecutor.isRunning()) {
			m_serverScheduledExecutor.Stop();
		}
		m_logger.info("ServerScheduledExecturo disposed successfully.");
	}
	// end region - > Private Dispose Methods

}
