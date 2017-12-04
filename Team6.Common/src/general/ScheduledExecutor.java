
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
import common.StartableState;

/**
 *
 * ScheduledExecutor:Abstract class that execute schedule tasks with result, the
 * class implement {@link IStartable} interface.
 * 
 */
public abstract class ScheduledExecutor extends Startable
{

	/**
	 * 
	 * ScheduledExecutionHandle: The handler of the scheduled task result.
	 * 
	 * @param <TData>
	 *            Scheduled task result type.
	 */
	public interface ScheduledExecutionHandle<TData>
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
	 * The default period of the executor.
	 */
	public static int DEFAULT_PERIOD = 1;

	/**
	 * The default time unit of the period.
	 */
	public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.DAYS;

	// region Fields

	private ScheduledExecutorService m_scheduledExecutorService;

	private ExecutorService m_executorService;

	private List<Runnable> m_scheduldTasks;

	private int m_period;

	private TimeUnit m_timeUnit;

	// end region -> Fields

	// region Constructors

	protected ScheduledExecutor(boolean throwable, Logger logger)
	{
		this(throwable, logger, DEFAULT_PERIOD, DEFAULT_TIMEUNIT);
	}

	protected ScheduledExecutor(boolean throwable, Logger logger, int period, TimeUnit timeUnit)
	{
		super(throwable, logger);
		m_period = period;
		m_timeUnit = timeUnit;
		m_scheduldTasks = new ArrayList<>();
		m_scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		m_executorService = Executors.newWorkStealingPool();
	}

	// end region -> Constructors

	// region Public Methods

	/**
	 * 
	 * Add task to the executor, if the executor is in state
	 * {@value StartableState#Ready} or {@value StartableState#Stopped}.
	 *
	 * @param <TData>
	 *            Scheduled task result type.
	 * @param runnable
	 *            The scheduled task - Can not be <code>null</code>.
	 * @param executionHandle
	 *            A handler of the scheduled task result.
	 * @return true if the task added successfully, false if did not.
	 */
	public <TData> boolean addTask(@NotNull Callable<TData> runnable,
			@Nullable ScheduledExecutionHandle<TData> executionHandle)
	{
		if (m_State == StartableState.Running || m_State == StartableState.Error) {
			return false;
		}
		Future<TData> submit = m_executorService.submit(runnable);
		Runnable scheduldTask = new ScheduledTask<TData>(submit, executionHandle);
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
		m_scheduledExecutorService.scheduleAtFixedRate(managerTask, 0, m_period, m_timeUnit);
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

	protected abstract boolean toExecute();

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

		private Future<TData> m_future;

		private ScheduledExecutionHandle<TData> m_executionHandle;

		private ScheduledTask(Future<TData> future, ScheduledExecutionHandle<TData> executionHandle)
		{
			m_future = future;
			m_executionHandle = executionHandle;
		}

		@Override
		public void run()
		{
			TData result;
			try {
				result = m_future.get();
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
