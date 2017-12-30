package server;

import java.io.File;
import java.io.IOException;
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
import javafx.stage.Stage;
import logger.LogManager;
import common.UncaughetExceptions;

/**
 * Server application entry point.
 *
 */
public class ApplicationEntryPoint extends Application {

	private final static String s_lockFilePath = "ServerLockFile.lock";
	private static File s_file;
	private static FileChannel s_fileChannel;
	private static FileLock s_lockFile;

	/**
	 * 
	 * The main method of the application, the method ensure only one instance of
	 * the application, using file lock pattern.
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
				System.out.println("Only one instance of Server can run.");
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
						System.err.println("An error occured on try to release and delete instance locks, exception: "
								+ e.getMessage());
					}
				}
			});

			Runtime.getRuntime().addShutdownHook(shutdownHook);

			// Launch the application
			launch(args);
		} catch (IOException e) {
			System.err.println("An error occured on try to start 'Server' process, exception: " + e.getMessage());
			System.exit(1);
		}
	}

	// region Fields

	private Logger m_logger;

	private ServerConfiguration m_serverConfiguration;

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
			initializeUncughtExceptionHandler();
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
			disposeDbController();
			disposeServer();
		} catch (Exception ex) {
			m_logger.log(Level.SEVERE, "Disposing failed!", ex);
		}
		m_logger = null;
		m_serverConfiguration = null;

		super.stop();
	}

	private void initializeConfiguration() {
		m_serverConfiguration = ServerConfiguration.getInstance();
		if (m_serverConfiguration.isDefaultConfiguration()) {
			m_logger.warning("Failed on try to read configuration from \"" + ServerConfiguration.CONFIGURATION_PATH
					+ "\". Created default configuration");

		}
		m_logger.config("Server configuration loaded:" + m_serverConfiguration.toString());
	}

	// end region -> Application Method Overrides

	// region Private Initialize Methods

	private void initializeUncughtExceptionHandler() {
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

	private void intializeServer() throws IOException {
		Server = new Server(m_logger, m_serverConfiguration.getConnectivityConfiguration().getPort());
		m_logger.info("Server instance created successfully.");
	}

	private void initializeDbController() {
		DbContoller = new DbController(m_logger, m_serverConfiguration.getDbConfiguration());
		m_logger.info("Database instance created successfully.");
	}

	// end region -> Private Initialize Methods

	// region -> Private Dispose Methods

	private void disposeServer() throws IOException {
		if (Server != null) {
			Server.close();
		}
		m_logger.info("Server disposed successfully.");
		Server = null;
	}

	private void disposeDbController() {
		if (DbContoller != null && DbContoller.isRunning()) {
			DbContoller.Stop();
		}
		m_logger.info("Database controller disposed successfully.");
		DbContoller = null;
	}

	// end region - > Private Dispose Methods

}
