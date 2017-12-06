import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ServerConfiguration;
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
	private static ServerConfiguration s_serverConfiguration;

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
		DbController controller =new DbController(s_serverConfiguration.getDbConfiguration());
		controller.Start();
		controller.printAllProducts();
		controller.Stop();
	}

	private static void initializeConfiguration() {
		String configurationPath = "configuration.xml";
		InputStream inputStream = EntryPoint.class.getResourceAsStream(configurationPath);
		if (inputStream == null) {
			s_logger.severe("Configuration XML file was not found, path: " + configurationPath);
			return;
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		s_serverConfiguration = XmlUtilities.parseXmlToObject(bufferedReader,
				ServerConfiguration.class);

		if (s_serverConfiguration != null) {
			s_logger.config("Server configuration loaded seccesfuly! " + s_serverConfiguration.toString());

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
