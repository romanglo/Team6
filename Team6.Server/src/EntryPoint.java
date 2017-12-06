import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ServerConfiguration;
import logs.LogManager;
import utilities.XmlUtilities;

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
			initializeMySqlDriver();
			testDbController();
		} catch (Exception ex) {
			s_logger.log(Level.SEVERE, "initialization failed!", ex);
		}
	}

	private static void testDbController() {
		DbController controller =new DbController(false, s_logger);
		controller.Start();
		controller.printAllProducts();
		controller.Stop();
	}

	private static void initializeConfiguration() {
		String configurationPath = "configuration\\configuration.xml";
		InputStream inputStream = EntryPoint.class.getResourceAsStream(configurationPath);
		if (inputStream == null) {
			s_logger.severe("Configuration file was not found, path: " + configurationPath);
			return;
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		ServerConfiguration serverConfiguration = XmlUtilities.parseXmlToObject(bufferedReader,
				ServerConfiguration.class);

		if (serverConfiguration != null) {
			s_logger.config("Server configuration loaded seccesfuly! " + serverConfiguration.toString());

		} else {
			s_logger.severe("Failed on try to load configuration xml file.");
		}
	}
	
	private static void initializeMySqlDriver() throws Exception{
		String mySqlDriverName = "com.mysql.jdbc.Driver";
        Class.forName(mySqlDriverName).newInstance();
        s_logger.config("Successfully created MySQL driver , driver name: " + mySqlDriverName);	
	}
}
