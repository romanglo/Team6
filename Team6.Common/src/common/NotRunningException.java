/**
 * Package Name: common Created: 05-12-2017
 */

package common;

/**
 *
 * NotRunningException: Unchecked exception which thrown when a
 * {@link IStartable} that is not in the {@link StartableState#Running} state is
 * used.
 * 
 * @see RuntimeException
 * 
 */
public class NotRunningException extends RuntimeException
{

	/**
	 * Auto generated serial version ID
	 */
	private static final long serialVersionUID = 6304722223306588170L;

	/**
	 * Constructs a new runtime exception with the default detail message: "A
	 * instance that implements IStartable interface that is not in the 'Running'
	 * state is used".
	 *
	 */
	public NotRunningException()
	{
		super("A instance that implements IStartable interface that is not in the 'Running' state is used.");
	}

	/**
	 * 
	 * Constructs a new runtime exception with the specified detail message.
	 *
	 * @param msg
	 *            detail message.
	 */
	public NotRunningException(String msg)
	{
		super(msg);
	}

	/**
	 * Constructs a new runtime exception with the detail message that
	 * describes the throwing instance.
	 *
	 * @param startable
	 *            the instance that used without be in 'Running' state.
	 * 
	 * @see Startable
	 */
	public NotRunningException(Startable startable)
	{
		super(startable.m_Id + " that implements IStartable interface used not in " + startable.m_State + " state.");
	}

}
