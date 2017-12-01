
package Logs;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * LogManager: a class that implements the Singleton design pattern, and
 * initialize a application log.
 */
public class LogManager
{

	private static Logger s_instance;

	/**
	 * If it is the first call to the method, the method will create a log to the application.
	 * Else the method will return an existing logger.
	 *
	 * @return a logger.
	 */
	public static Logger getLogger()
	{
		if (s_instance == null) {
			// Single Checked
			synchronized (LogManager.class) {
				if (s_instance == null) {
					// Double checked
					s_instance = logInitialize();

					/*
					 * if the log initialization failed. set log object that will "Swallow"
					 * messages, and will not make NullPointerException in the application
					 */
					if (s_instance == null) {
						s_instance = Logger.getLogger(LogManager.class.getName());
					}
				}
			}
		}
		return s_instance;
	}

	/**
	 * 
	 * The method initialize log file in the path : "APPLICATION-LOCATION/logs/log-CURRENTDATE.log". 
	 *
	 * @return A logger instance if the initialize succeed and null if did not.
	 */
	private static Logger logInitialize()
	{

		try {
			Logger logger = Logger.getLogger(LogManager.class.getName());

			// set logs folder.
			File logDir = new File(".\\logs");

			// if the directory does not exist, create it
			if (!logDir.exists() && logDir.mkdir()) {
				return null;
			}

			// set log name
			LocalDateTime localDateTime = LocalDateTime.now();
			LocalDate localDate = localDateTime.toLocalDate();
			String logName = "log-" + localDate.toString() + ".log";

			String logPath = logDir.getPath() + '\\' + logName;

			File f = new File(logPath);
			// if already exist log from today, delete him.
			if (f.exists() && !f.delete()) {
				return null;
			}

			// Creating fileHandler
			Handler fileHandler = new FileHandler(logPath);

			// Assigning handlers to LOGGER object
			logger.addHandler(fileHandler);
			fileHandler.setLevel(Level.ALL);

			// Setting levels to handlers and LOGGER
			boolean debugging = isRunningInDebugMode();
			if (debugging) {
				// Creating consoleHandler
				Handler consoleHandler = new ConsoleHandler();
				logger.addHandler(consoleHandler);
				consoleHandler.setLevel(Level.ALL);
			}

			logger.setLevel(Level.ALL);

			logger.config("Log create succesfuly on 'All' Level. Log path: " + f.getAbsolutePath());

			return logger;
		}
		catch (Exception exception) {
			return null;
		}
	}

	/**
	 * 
	 * The method checks if the application runs in debug mode.
	 *
	 * @return true if the application runs in debug mode.
	 */
	private static boolean isRunningInDebugMode()
	{
		final Pattern debugPattern = Pattern.compile("-Xdebug|jdwp");

		for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			if (debugPattern.matcher(arg).find()) {
				return true;
			}
		}
		return false;
	}
}
