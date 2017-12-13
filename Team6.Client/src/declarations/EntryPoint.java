package declarations;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ClientConfiguration;
import logs.LogManager;
import connectivity.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * EntryPoint: 
 * The entry point of the application.
 * 
 */
public class EntryPoint extends Application {

	private static Logger s_logger = null;
	
	/**
	 * Client connection configuration.
	 */
	public static ClientConfiguration s_clientConfiguration;
	
	/**
	 * Client connection management instance.
	 */
	public static Client clientController;

	/**
	 * Client application entry point
	 *
	 * @param args - Application arguments
	 */
	public static void main(String[] args) {
		s_logger = LogManager.getLogger();

		try {
			initializeConfiguration();
			initializeConnection();
			launch(args);
		}
		catch (Exception ex) {
			s_logger.log(Level.SEVERE, "Initialization failed!", ex);
		}

	}
	
	/**
	 * Method closes the connection with the server.
	 *
	 */
	public static void disposeConnection() {
		if (clientController != null) {
			clientController.close();
			s_logger.info("Client disposed successfully");
		}
	}
	
	/**
	 * Method creates connection to the server with the configured IP and port.
	 *
	 * @throws IOException - Connection error.
	 * @throws NumberFormatException - Conversion error.
	 */
	public static void initializeConnection() throws IOException, NumberFormatException {
		clientController = new Client(s_logger, s_clientConfiguration.getIp(), 
				Integer.parseInt(s_clientConfiguration.getPort()));
		s_logger.info("Client initialized successfully.");
	}
	
	/**
	 * Method reads from the configuration file and sets the configuration.
	 *
	 */
	private static void initializeConfiguration() {
		s_clientConfiguration = ClientConfiguration.getInstance();
		if (s_clientConfiguration.isDefaultConfiguration()) {
			s_logger.warning("Failed on try to read configuration from \"" + 
					ClientConfiguration.CONFIGURATION_PATH + "\". Created default configuration.");
		}
		s_logger.config("Client configuration loaded:" + s_clientConfiguration.toString());
	}
	
	
	/**
	 * Method opens the UI of the client.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(440);
			primaryStage.setMinHeight(450);
			primaryStage.setTitle("Zer-Li Client");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
