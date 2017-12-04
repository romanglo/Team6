
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
	 * @return the state of the {@link IStartable}, see {@link StartableState} for
	 *         more information on the different states.
	 */
	StartableState getState();

	/**
	 * 
	 * Start the operation that the {@link IStartable} does.
	 *
	 */
	void Start();

	/**
	 * Stop the operation that the {@link IStartable} does.
	 *
	 */
	void Stop();
}
