
package common;

import java.util.Date;

/**
 * 
 *
 * ITimeProvider: The interface describes classes that provide system current
 * time.
 *
 */
public interface ITimeProvider
{

	/**
	 * @return System current {@link Date}.
	 */
	Date getCurrentTime();
}
