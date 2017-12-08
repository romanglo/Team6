import java.util.logging.Level;
import java.util.logging.Logger;

import configurations.ClientConfiguration;
import logs.LogManager;

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
	private static ClientConfiguration s_clientConfiguration;

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

	private static void initializeConfiguration() {
		s_clientConfiguration = ClientConfiguration.getInstance();
		if (s_clientConfiguration.isDefaultConfiguration()) {
			s_logger.warning("Failed on try to read configuration from \"" + 
					ClientConfiguration.CONFIGURATION_PATH
					+ "\". Created default configuration.");

		}
		s_logger.config("Client configuration loaded:" + s_clientConfiguration.toString());
	}
}
