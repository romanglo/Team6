
package common;

import java.util.Date;

/**
 *
 * RealTimeProvider: Provide current time of the system based on
 * {@link Date#Date()}.
 * 
 */
public class RealTimeProvider implements ITimeProvider
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCurrentTime()
	{
		return new Date();
	}
}
