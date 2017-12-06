package logs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class LogFormatter extends Formatter
{

	private final SimpleDateFormat m_localDateFormat = new SimpleDateFormat("HH:mm:ss");

	@Override
	public String format(LogRecord record)
	{

		Date date = new Date(record.getMillis());
		String time = m_localDateFormat.format(date);

		String msg = time + " - " + record.getLevel() + '/'
				+ record.getSourceClassName() +'$'+record.getSourceMethodName()+ ": " +record.getMessage() +'.';
			
		Throwable thrown = record.getThrown();
		if(thrown!=null) {
			msg += ". Exception: " + thrown.toString();
		}
		
		msg+= '\n';
		
		return msg;
	}
}
