import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import configuration.ClientConfiguration;
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
	 * @param args application arguments
	 */
	public static void main(String[] args) {
		s_logger = LogManager.getLogger();

		try {
			initializeConfiguration();
		} catch (Exception ex) {

		}
		s_logger.info("Server closing..");
	}

	private static void initializeConfiguration() {
		InputStream inputStream = EntryPoint.class.getResourceAsStream("/Configuration/configuration.xml"); 
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		ClientConfiguration serverConfiguration = XmlUtilities.parseXmlToObject(bufferedReader,ClientConfiguration.class);

		
		if (serverConfiguration != null) {
			s_logger.config("Server configuration loaded seccesfuly! " + serverConfiguration.toString());

		} else {
			s_logger.severe("Failed on try to load configuration xml file.");
		}
	}
}
