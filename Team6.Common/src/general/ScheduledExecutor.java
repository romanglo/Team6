
package general;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import common.IStartable;
import common.Startable;

/**
 *
 * ScheduledExecutor: A class that execute schedule tasks in fixed rate with
 * result, the class implement {@link IStartable} interface.
 * 
 */
public class ScheduledExecutor extends Startable
{

	/**
	 * 
	 * ScheduledExecutionHandler: The handler of the scheduled task result.
	 * 
	 * @param <TData>
	 *            Scheduled task result type.
	 */
	public interface IScheduledExecutionHandler<TData>
	{

		/**
		 * 
		 * The method would invoked when the task finish.
		 *
		 * @param executionResult
		 *            Scheduled task result, <code>null</code> if the execution failed.
		 */
		void onScheduledExecution(@Nullable TData executionResult);
	}

	/**
	 * The default period of initial delay.
	 */
	public static final int DEFAULT_DELAY = 0;

	/**
	 * The default period of the executor.
	 */
	public static int DEFAULT_PERIOD = 1;

	/**
	 * The default time unit of the period.
	 */
	public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.DAYS;

	// region Fields

	private ScheduledExecutorService m_scheduledExecutorService;

	private volatile ExecutorService m_executorService;

	private List<Runnable> m_scheduldTasks;

	private int m_period;

	private TimeUnit m_timeUnit;

	private int m_delay;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * Create instance that execute schedule tasks in fixed rate with result. The
	 * execution will happen every {@value ScheduledExecutor#DEFAULT_PERIOD}
	 * {@value ScheduledExecutor#DEFAULT_TIMEUNIT}.
	 * 
	 * @see IStartable
	 * @param throwable
	 *            if true the method {@link IStartable#Start()} will throw
	 *            {@link RuntimeException} on error case. if false only the
	 *            {@link IStartable} state will changed.
	 * @param logger
	 *            A logger to write to it.
	 */
	public ScheduledExecutor(boolean throwable, Logger logger)
	{
		this(throwable, logger, DEFAULT_DELAY, DEFAULT_PERIOD, DEFAULT_TIMEUNIT);
	}

	/**
	 * 
	 * Create instance that execute schedule tasks in fixed rate with result.
	 * 
	 * @see IStartable
	 * @param throwable
	 *            if true the method {@link IStartable#Start()} will throw
	 *            {@link RuntimeException} on error case. if false only the
	 *            {@link IStartable} state will changed.
	 * @param logger
	 *            A logger to write to it.
	 * @param initialDelay
	 *            the time to delay first
	 * @param executionPeriod
	 *            the period between successive executions
	 * @param timeUnit
	 *            the time unit of the initialDelay and period parameters
	 */
	public ScheduledExecutor(boolean throwable, Logger logger, int initialDelay, int executionPeriod, TimeUnit timeUnit)
	{
		super(throwable, logger);
		m_delay = initialDelay;
		m_period = executionPeriod;
		m_timeUnit = timeUnit;
		m_scheduldTasks = new ArrayList<>();
		m_scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		m_executorService = Executors.newWorkStealingPool();
	}

	// end region -> Constructors

	// region Public Methods

	/**
	 * 
	 * Add task to the executor, if the executor is in 'Stopped' state.
	 *
	 * @param <TData>
	 *            Scheduled task result type.
	 * @param callable
	 *            The scheduled task - Can not be <code>null</code>.
	 * @param executionHandle
	 *            A handler of the scheduled task result.
	 * @return true if the task added successfully, false if did not.
	 */
	public <TData> boolean addTask(@NotNull Callable<TData> callable,
			@Nullable IScheduledExecutionHandler<TData> executionHandle)
	{
		if (isRunning()) {
			return false;
		}
		Runnable scheduldTask = new ScheduledTask<TData>(callable, executionHandle);
		synchronized (ScheduledExecutor.class) {
			m_scheduldTasks.add(scheduldTask);
		}
		return true;
	}

	// end region -> Public Methods

	// region Startable Implementation

	@Override
	protected void initialStart()
	{
		final Runnable managerTask = new ManagerTask();
		m_scheduledExecutorService.scheduleAtFixedRate(managerTask, m_delay, m_period, m_timeUnit);
	}

	@Override
	protected void initialStop()
	{
		m_scheduledExecutorService.shutdownNow();
		m_executorService.shutdownNow();
		m_scheduldTasks.clear();
	}

	// end region -> Startable Implementation

	// region Abstract Methods

	/**
	 * The method called when the received period has been passed and the method
	 * check the precondition to execution of the tasks. The default implementation
	 * always returns <code>true</code>, it may be overridden by extending class.
	 * 
	 * @return <code>true</code> to continue execution of the tasks, and
	 *         <code>false</code> for skip the execution this time.
	 */
	protected boolean toExecute()
	{
		return true;
	}

	// end region -> Abstract Methods

	// region Nested Classes

	/**
	 * 
	 * ManagerTask: A {@link Runnable} that manage the execution of the contained
	 * tasks.
	 *
	 */
	private class ManagerTask implements Runnable
	{

		@Override
		public void run()
		{
			boolean skip = !toExecute();
			if (skip) {
				return;
			}
			synchronized (ScheduledExecutor.class) {
				for (Runnable runnable : m_scheduldTasks) {
					m_executorService.execute(runnable);
				}
			}
		}
	}

	/**
	 * 
	 * ScheduledTask: A {@link Runnable} that execute contained {@link Future} and
	 * set the result to the handler.
	 * 
	 * @param <TData>
	 *            The type of the result.
	 */
	private class ScheduledTask<TData> implements Runnable
	{

		private Callable<TData> m_callable;

		private IScheduledExecutionHandler<TData> m_executionHandle;

		private ScheduledTask(Callable<TData> callable, IScheduledExecutionHandler<TData> executionHandle)
		{
			m_callable = callable;
			m_executionHandle = executionHandle;
		}

		@Override
		public void run()
		{
			TData result;
			try {
				Future<TData> future = m_executorService.submit(m_callable);
				result = future.get();

			}
			catch (InterruptedException | ExecutionException e) {
				m_Logger.log(Level.WARNING,
						"Failed on try execute one of the tasks, task name: " + this.getClass().getTypeName(), e);
				result = null;
			}

			if (m_executionHandle != null) {
				m_executionHandle.onScheduledExecution(result);
			}
		}
	}

	// end region -> Nested Classes
}
