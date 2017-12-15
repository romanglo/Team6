
package logger;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * LogFormatter: Decorate message style.
 * 
 */
public class LogFormatter extends Formatter
{

	private final SimpleDateFormat m_localDateFormat = new SimpleDateFormat("HH:mm:ss");

	@Override
	public String format(LogRecord record)
	{

		Date date = new Date(record.getMillis());
		String time = m_localDateFormat.format(date);

		Throwable thrown = record.getThrown();
		String msg;
		if (thrown == null) {
			msg = MessageFormat.format("{0} - {1}/{2}${3}: {4}.\n", time, record.getLevel(),
					record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
		} else {
			msg = MessageFormat.format("{0} - {1}/{2}${3}: {4}. Exception: {5}\n", time, record.getLevel(),
					record.getSourceClassName(), record.getSourceMethodName(), record.getMessage(), thrown.toString());
		}
		return msg;
	}
}
