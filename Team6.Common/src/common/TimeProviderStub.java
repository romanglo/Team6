
package common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 *
 * TimeProviderStub: A Stub of {@link ITimeProvider} which receives a fixed
 * date.
 *
 */
public class TimeProviderStub implements ITimeProvider
{

	private final static DateFormat s_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private Date m_fixedDate;

	/**
	 * @param time
	 *            {@link String} which describes a time, must be in "dd-MM-yyyy
	 *            HH:mm:ss" format
	 * @throws ParseException
	 *             if the received time is not in the requested format.
	 */
	public TimeProviderStub(String time) throws ParseException
	{
		m_fixedDate = s_formatter.parse(time);
	}

	/**
	 * @param time
	 *            the time which the provider would provide.
	 */
	public TimeProviderStub(Date time)
	{
		m_fixedDate = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCurrentTime()
	{
		return m_fixedDate;
	}

}
