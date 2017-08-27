package personal.bw.shopper.untils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.text.DateFormat.MEDIUM;
import static java.text.DateFormat.getDateInstance;
import static java.util.Calendar.*;

public class CalendarConverter
{
	private final static DateFormat DATE_FORMAT = getDateInstance(MEDIUM);

	public String toString(int day, int month, int year)
	{
		return toString(new GregorianCalendar(year, month, day).getTime());
	}

	public String toString(Date date)
	{
		return DATE_FORMAT.format(date);
	}

	public int toDay(String stringDate)
	{
		Calendar calendar = toCalendar(stringDate);
		return calendar.get(DAY_OF_MONTH);
	}

	public int toMonth(String stringDate)
	{
		Calendar calendar = toCalendar(stringDate);
		return calendar.get(MONTH);
	}

	public int toYear(String stringDate)
	{
		Calendar calendar = toCalendar(stringDate);
		return calendar.get(YEAR);
	}

	public Date toDate(String stringDate)
	{
		try
		{
			return DATE_FORMAT.parse(stringDate);
		} catch (ParseException e)
		{
			e.printStackTrace();
			return new Date();
		}
	}

	public Calendar toCalendar(String stringDate)
	{
		Date date = toDate(stringDate);
		Calendar calendar = getInstance();
		calendar.setTime(date);
		return calendar;
	}
}
