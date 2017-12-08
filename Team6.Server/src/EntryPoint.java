import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import logs.LogManager;

/**
 * Package Name: 
 * Created:		 13-11-2017
 */

/**
 *
 * EntryPoint: The class that have the main function.
 * 
 */
public class EntryPoint {

	private static Logger s_logger = null;
	private static ServerConfiguration s_serverConfiguration;
	private static DbController s_dbController;
	private static Server s_server;

	/**
	 * Server application entry point
	 *
	 * @param args
	 *            application arguments
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		s_logger = LogManager.getLogger();

		try {
			initializeConfiguration();
			intializeServer();
			initializeDbController();
		} catch (Exception ex) {
			s_logger.log(Level.SEVERE, "Initialization failed!", ex);
		}

		System.out.println("Press enter to continue..");
		new Scanner(System.in).next();

		try {
			disposeDbController();
			disposeServer();
		} catch (Exception ex) {
			s_logger.log(Level.SEVERE, "Disposing failed!", ex);
		}
	}

	private static void intializeServer() throws IOException {
		s_server = new Server(s_logger, s_serverConfiguration.getConnectivityConfiguration());
		s_server.listen();
		s_logger.info("Server initialized successfully.");
	}

	private static void disposeServer() throws IOException {
		if (s_server != null) {
			s_server.close();
			s_logger.info("Server disposed successfully.");
		}
	}

	private static void initializeDbController() {
		s_dbController = new DbController(s_logger, s_serverConfiguration.getDbConfiguration());
		s_dbController.Start();
		s_logger.info("Database controller initialized successfully.");
	}

	private static void disposeDbController() {
		if (s_dbController != null) {
			s_dbController.printAllProducts();
			s_dbController.Stop();
			s_logger.info("Database controller disposed successfully.");
		}
	}

	private static void initializeConfiguration() {
		s_serverConfiguration = ServerConfiguration.getInstance();
		if (s_serverConfiguration.isDefaultConfiguration()) {
			s_logger.warning("Failed on try to read configuration from \"" + ServerConfiguration.CONFIGURATION_PATH
					+ "\". Created default configuration");

		}
		s_logger.config("Server configuration loaded:" + s_serverConfiguration.toString());
	}

}
