
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

	protected StartableState m_State;

	protected Logger m_Logger;

	private boolean m_throwable;

	// end region -> Fields

	// region Constructors

	/**
	 * @param throwable
	 *            if true the method {@link IStartable#Start()} and
	 *            {@link IStartable#Stop()} will throw {@link RuntimeException} on
	 *            error case. if false only the {@link StartableState} will changed.
	 * @param logger
	 *            A logger to write to it.
	 */
	protected Startable(boolean throwable, @NotNull Logger logger)
	{
		m_throwable = throwable;
		
		m_Logger = logger;

		m_Id = getClass().getName();

		m_State = StartableState.Ready;
	}

	// end region -> Constructors

	// region Getters

	/**
	 *
	 * @return true if the method will throw exception in {@link Startable#Start()}
	 *         and {@link Startable#Stop()} if necessary.
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
	@Override
	public StartableState getState()
	{
		return m_State;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void Start() throws RuntimeException
	{
		if (m_State == StartableState.Running || m_State == StartableState.Error) {
			m_Logger.log(Level.INFO, "Start method of " + m_Id + " called when the state is: " + m_State);
			return;
		}

		m_Logger.log(Level.INFO, m_Id + " starting..");
		try {
			initialStart();
			m_State = StartableState.Running;
			m_Logger.log(Level.INFO, m_Id + " started successfully, the state is: " + m_State);
		}
		catch (Exception e) {
			m_State = StartableState.Error;
			String errorMessage = m_Id + " starting failed, the state is: " + m_State;
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
		if (m_State != StartableState.Running) {
			m_Logger.log(Level.INFO, "Stop method of " + m_Id + " called when the state is: " + m_State);
			return;
		}

		m_Logger.log(Level.INFO, m_Id + " stopping..");
		try {
			initialStop();
			m_State = StartableState.Stopped;
			m_Logger.log(Level.INFO, m_Id + " stopping successfully, the state is: " + m_State);
		}
		catch (Exception e) {
			m_State = StartableState.Error;
			String errorMessage = m_Id + " starting failed, the state is: " + m_State;
			m_Logger.log(Level.INFO, errorMessage, e);
			if (m_throwable) {
				throw new RuntimeException(errorMessage, e);
			}
		}
	}

	// end region -> IStartable Implementation

	// region Abstract Methods

	/**
	 * The method contain the real login in stop operation.
	 */
	protected abstract void initialStop();

	/**
	 * The method contain the real login in start operation.
	 */
	protected abstract void initialStart();

	// end region -> Abstract Methods
}
