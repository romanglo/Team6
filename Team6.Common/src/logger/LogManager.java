
package logger;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LogManager: a class that implements the Singleton design pattern, and
 * initialize a application log.
 */
public class LogManager
{

	private static Logger s_instance = null;

	private static String s_path = null;

	/**
	 * If it is the first call to the method, the method will create a log to the
	 * application. Else the method will return an existing logger.
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
	 * @return returns the {@link String} path of the current logger if it
	 *         initialized and <code>null </code> if does not.
	 */
	public static String getLoggerPath()
	{
		return s_path;
	}

	/**
	 * 
	 * The method initialize log file in the path :
	 * "APPLICATION-LOCATION/log-CURRENTDATE.log".
	 *
	 * @return A logger instance if the initialize succeed and null if did not.
	 */
	private static Logger logInitialize()
	{

		try {
			Logger logger = Logger.getLogger(LogManager.class.getName());

			// set log name
			LocalDateTime localDateTime = LocalDateTime.now();
			LocalDate localDate = localDateTime.toLocalDate();
			String logName = "log-" + localDate.toString() + ".log";

			// Creating formatter
			Formatter formatter = new LogFormatter();

			// Creating fileHandler and assign to logger
			Handler fileHandler = new FileHandler(logName);
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(formatter);
			logger.addHandler(fileHandler);

			// Setting levels to LOGGER
			logger.setLevel(Level.ALL);

			File logPath = new File(logName);
			logger.config("Log created successfully on 'All' Level. Log path: " + logPath.getAbsolutePath());
			s_path = logPath.getPath();
			return logger;
		}
		catch (Exception exception) {
			return null;
		}
	}
}
