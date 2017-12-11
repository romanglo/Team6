import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ServerConfiguration;
import connectivity.Server;
import db.DbController;
import db.QueryFactory;
import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;
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
	public static void main(String[] args) {
		s_logger = LogManager.getLogger();

		try {
			initializeConfiguration();
			intializeServer();
			initializeDbController();
		} catch (Exception ex) {
			s_logger.log(Level.SEVERE, "Initialization failed!", ex);
		}

		IEntity debugProduct = new ProductEntity(99, "DEBUG", ProductType.BridalBouquet);
		String updateEntityQuery = QueryFactory.generateUpdateEntityQuery(debugProduct);
		s_serverConfiguration.getConnectivityConfiguration().setPort(6666);
		if(s_serverConfiguration.updateResourceFile()) {
			s_logger.info("Configuration resource file updated successfully. " +s_serverConfiguration.toString());
		} else {
			s_logger.warning("Configuration resource file update failed!");
		}
		if (updateEntityQuery != null) {
			s_dbController.executeUpdate(updateEntityQuery);
			s_dbController.printAllProducts();
		}

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
