
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
class LogFormatter extends Formatter
{

	private final static SimpleDateFormat s_localDateFormat = new SimpleDateFormat("HH:mm:ss");

	@Override
	public String format(LogRecord record)
	{

		Date date = new Date(record.getMillis());
		String time = s_localDateFormat.format(date);

		String recoredMessage = record.getMessage();
		Throwable thrown = record.getThrown();

		String endWiths = recoredMessage.endsWith(".") || recoredMessage.endsWith("!") ? "" : ".";

		String msg;
		if (thrown == null) {
			msg = MessageFormat.format("{0} - {1}/{2}${3}: {4}{5}\n", time, record.getLevel(),
					record.getSourceClassName(), record.getSourceMethodName(), recoredMessage, endWiths);
		} else {
			msg = MessageFormat.format("{0} - {1}/{2}${3}: {4}{5} Exception: {6}\n", time, record.getLevel(),
					record.getSourceClassName(), record.getSourceMethodName(), recoredMessage, endWiths,
					thrown.toString());
		}

		return msg;
	}
}
