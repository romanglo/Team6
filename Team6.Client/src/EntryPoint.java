import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ClientConfiguration;
import logs.LogManager;
import utilities.XmlUtilities;

/**
 * Package Name: Created: 13-11-2017
 */

/**
 *
 * EntryPoint: The class that have the main function.
 * 
 */
public class EntryPoint
{

	private static Logger s_logger = null;

	/**
	 * Server application entry point
	 *
	 * @param args
	 *            application arguments
	 */
	public static void main(String[] args)
	{
		s_logger = LogManager.getLogger();

		try {
			initializeConfiguration();
		}
		catch (Exception ex) {
			s_logger.log(Level.SEVERE, "initialization failed!", ex);
		}

	}

	private static void initializeConfiguration() throws Exception 
	{
		String configurationPath = "configuration.xml";
		InputStream inputStream = EntryPoint.class.getResourceAsStream(configurationPath);
		if (inputStream == null) {
			s_logger.severe("Configuration XML file was not found, path: " + configurationPath);
			return;
		}

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		ClientConfiguration clientConfiguration = XmlUtilities.parseXmlToObject(bufferedReader,ClientConfiguration.class);

		if (clientConfiguration != null) {
			s_logger.config("Server configuration loaded seccesfuly! " + clientConfiguration.toString());

		} else {
			s_logger.severe("Failed on try to load configuration xml file.");
		}
	}
}
