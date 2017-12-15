
package common;

/**
 *
 * IStartable: Describes a class that performs a long (can be cyclically)
 * operation that continues until it stops, this class can start and stop an
 * unlimited number of times.
 * 
 */
public interface IStartable
{

	/**
	 * @return <code>true</code> if the {@link IStartable} started and running now,
	 *         and <code>false</code> if in stopped state.
	 */
	boolean isRunning();

	/**
	 * Start the operation that the {@link IStartable} does.
	 */
	void Start();

	/**
	 * Stop the operation that the {@link IStartable} does.
	 */
	void Stop();
}
