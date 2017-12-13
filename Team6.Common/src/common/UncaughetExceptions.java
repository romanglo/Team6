
package common;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

import logger.LogManager;

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

	private static boolean s_shutdownOnUncaughtException = true;

	/**
	 * Internal implementation of handling uncaught exception in the applications.
	 */
	private static Thread.UncaughtExceptionHandler internalUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable throwable)
		{
			String msg = "Caughted unhandled exception in: " + thread.toString();

			Logger logger = LogManager.getLogger();

			logger.log(Level.SEVERE, msg, throwable);

			if (s_handler != null) {
				s_handler.onUncaughtException(throwable);
			}

			if (s_shutdownOnUncaughtException) {
				logger.log(Level.SEVERE, "The application shutting down due to uncaught exception.");
				System.exit(1);
			}
		}
	};

	/**
	 * 
	 * The method start handling uncaught exceptions, on uncaught exception will
	 * call the {@link UncaughtExceptionsHandler} and could shutdown the application.
	 *
	 * @param handler
	 *            a handler that would called when occurred uncaught exception.
	 * @param shutdownOnUncaughtException
	 *            if true the application will shutdown in case of uncaught
	 *            exception.
	 */
	public static void startHandling(@Nullable UncaughtExceptionsHandler handler, boolean shutdownOnUncaughtException)
	{
		s_handler = handler;

		UncaughetExceptions.s_shutdownOnUncaughtException = shutdownOnUncaughtException;

		Thread.setDefaultUncaughtExceptionHandler(internalUncaughtExceptionHandler);
	}

}
