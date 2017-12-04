
package common;

/**
 *
 * StartableState: Describe the state of {@link IStartable}.
 * 
 */
public enum StartableState {

	/**
	 * {@link IStartable} not Started yet.
	 */
	Ready,

	/**
	 * {@link IStartable} running now.
	 */
	Running,

	/**
	 * {@link IStartable} runs at lease one, and now is stopped.
	 */
	Stopped,

	/**
	 * {@link IStartable} error state, would never run again.
	 */
	Error 
	
	
}
