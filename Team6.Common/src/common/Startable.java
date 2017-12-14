
package common;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

/**
 *
 * Startable: Base abstract implementation to {@link IStartable} interface.
 * 
 */
public abstract class Startable implements IStartable
{

	// region Fields

	protected final String m_Id;

	protected boolean m_running;

	protected Logger m_Logger;

	private boolean m_throwable;

	// end region -> Fields

	// region Constructors

	/**
	 * @param throwable
	 *            if true the method {@link IStartable#Start()} will throw
	 *            {@link RuntimeException} on error case. if false only the
	 *            {@link StartableState} will changed.
	 * @param logger
	 *            A logger to write to it.
	 */
	protected Startable(boolean throwable, @NotNull Logger logger)
	{
		m_throwable = throwable;

		m_Logger = logger;

		m_Id = getClass().getName();

		m_running = false;
		
	}

	// end region -> Constructors

	// region Getters

	/**
	 *
	 * @return true if the method will throw exception in {@link Startable#Start()}
	 *         on error case.
	 */
	public boolean isThrowable()
	{
		return m_throwable;
	}

	// end region -> Getters

	// region IStartable Implementation

	/**
	 * {@inheritDoc}
	 */
	public boolean isRunning()
	{
		return m_running;
	}

	/**
	 * {@inheritDoc} The method
	 */
	@Override
	public void Start() throws RuntimeException
	{
		if (m_running) {
			m_Logger.log(Level.INFO, "Start method of " + m_Id + " called when the state is already 'Running'.");
			return;
		}

		m_Logger.log(Level.INFO, m_Id + " starting..");
		try {
			initialStart();
			m_running = true;
			m_Logger.log(Level.INFO, m_Id + " started successfully");
		}
		catch (Exception e) {
			m_running = false;
			String errorMessage = m_Id + " failed on try to start, the state is 'Stopped'.";
			m_Logger.log(Level.INFO, errorMessage, e);
			if (m_throwable) {
				throw new RuntimeException(errorMessage, e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void Stop()
	{
		if (m_running) {
			m_Logger.log(Level.INFO, "Start method of " + m_Id + " called when the state is already 'Stopped'.");
			return;
		}

		m_Logger.log(Level.INFO, m_Id + " stopping..");
		try {
			initialStop();
			m_Logger.log(Level.INFO, m_Id + " stopped successfully.");
		}
		catch (Exception e) {
			String errorMessage = m_Id + " failed on try to stop, the state is 'Stopped'.";
			m_Logger.log(Level.INFO, errorMessage, e);
		}
		finally {
			m_running = false;
		}
	}

	// end region -> IStartable Implementation

	// region Abstract Methods

	/**
	 * The method contain the real login in stop operation.
	 */
	protected abstract void initialStop() throws Exception;

	/**
	 * The method contain the real login in start operation.
	 */
	protected abstract void initialStart() throws Exception;

	// end region -> Abstract Methods
}
