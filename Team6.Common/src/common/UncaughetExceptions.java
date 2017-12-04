
package common;

import java.util.logging.Level;
import java.util.logging.Logger;

import logs.LogManager;

/**
 *
 * UncaughetExceptions: A class that refers to exceptions that were not caught.
 * 
 */
public class UncaughetExceptions
{

	/**
	 * UncaughetExceptionHandler: Describes a handler of uncaught exceptions.
	 */
	public interface UncaughtExceptionsHandler
	{

		/**
		 * 
		 * The method would invoked when uncaught exception handled.
		 *
		 * @param throwable
		 *            the uncaught exception.
		 */
		void onUncaughtException(Throwable throwable);
	}

	private static UncaughtExceptionsHandler s_handler;

	private static Logger s_logger;
	
	/**
	 * Internal implementation of handling uncaught exception in the applications.
	 */
	private static Thread.UncaughtExceptionHandler internalUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable throwable)
		{
			String msg = "Caughted unhandled exception in: " + thread.toString();
			s_logger.log(Level.SEVERE, msg, throwable);
			if (s_handler != null) {
				s_handler.onUncaughtException(throwable);
			}
			s_logger.log(Level.SEVERE, "The application shutting down dut to uncaught exception.");
			System.exit(1);
		}
	};
	
	
	/**
	 * 
	 * The method start handling uncaught exceptions, on uncaught exception will
	 * call the {@link UncaughtExceptionsHandler} and shutdown the application.
	 *
	 * @param handler
	 *            a handler that would called when occurred uncaught exception.
	 */
	public static void startHandling(UncaughtExceptionsHandler handler)
	{
		s_handler = handler;
		s_logger = LogManager.getLogger();

		Thread.setDefaultUncaughtExceptionHandler(internalUncaughtExceptionHandler);

		s_logger.log(Level.INFO, "Handler to uncaught exceptions setted.");
	}

}
