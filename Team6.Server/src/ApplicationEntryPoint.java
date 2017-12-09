
import java.io.IOException;
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

import common.UncaughetExceptions;

/**
 * Server application entry point.
 *
 */
public class ApplicationEntryPoint extends Application {

	private Logger m_logger = null;
	private ServerConfiguration m_serverConfiguration;
	
	/**
	 * Application Database Controller.
	 */
	public static DbController DbContoller;
	
	/**
	 * Application Server Controller.
	 */
	public static Server Server;

	/**
	 * 
	 * The main method of the application.
	 *
	 * @param args Application arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	//region Application Method Overrides
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			 Parent root = FXMLLoader.load(getClass().getResource("ServerUI.fxml"));
			 Scene scene = new Scene(root);
			 primaryStage.setScene(scene);
			 primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws Exception {
		try {
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
	
	// end region ->  Application Method Overrides
	
	// region Private Initialize Methods
	
	private void initializeUncughtExceptionHandler() {
		UncaughetExceptions.UncaughtExceptionsHandler uncaughtExceptionsHandler = new UncaughetExceptions.UncaughtExceptionsHandler() {
			@Override
			public void onUncaughtException(Throwable throwable) {
				try {
					stop();
				} catch (Exception ignored) {
					// Impossible exception.
				}
			}
		};
		UncaughetExceptions.startHandling(uncaughtExceptionsHandler);
	}

	private void intializeServer() throws IOException {
		Server = new Server(m_logger, m_serverConfiguration.getConnectivityConfiguration());
		Server.listen();
		m_logger.info("Server initialized successfully.");
	}
	
	private void initializeDbController() {
		DbContoller = new DbController(m_logger, m_serverConfiguration.getDbConfiguration());
		DbContoller.Start();
		m_logger.info("Database controller initialized successfully.");
	}
	
	//end region -> Private Initialize Methods
	
	//region -> Private Dispose Methods
	
	private void disposeServer() throws IOException {
		if (Server != null) {
			Server.close();
			m_logger.info("Server disposed successfully.");
		}
	}

	private void disposeDbController() {
		if (DbContoller != null) {
			DbContoller.Stop();
			m_logger.info("Database controller disposed successfully.");
		}
	}

	//end region.
}
